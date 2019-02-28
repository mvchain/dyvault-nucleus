package com.mvc.dyvault.console.job;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.mvc.dyvault.common.bean.CommonToken;
import com.mvc.dyvault.common.bean.CommonTokenHistory;
import com.mvc.dyvault.common.bean.vo.ExchangeRateVO;
import com.mvc.dyvault.common.bean.vo.ExchangeResponse;
import com.mvc.dyvault.common.constant.RedisConstant;
import com.mvc.dyvault.console.constant.BusinessConstant;
import com.mvc.dyvault.console.service.*;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.NumberUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * TODO 暂时使用简单定时,后续修改为服务调度
 *
 * @author qiyichen
 * @create 2018/12/7 17:21
 */
@Component
@Log4j
public class Job {

    @Autowired
    StringRedisTemplate redisTemplate;
    @Autowired
    AdminWalletService adminWalletService;
    @Autowired
    CommonTokenService commonTokenService;
    @Autowired
    UsdtService usdtService;
    @Autowired
    CommonTokenPriceService commonTokenPriceService;

    RestTemplate restTemplate = new RestTemplate();

    /**
     * 生成usdt和eth的价格（从第三方拉取）
     *
     * @return
     */
    @Scheduled(cron = "${scheduled.usdt}")
    private void updateUsdtPrice() {
        String usdtUrl = "https://data.block.cc/api/v1/price?symbol=USDT";
        String ethUrl = "https://data.block.cc/api/v1/price?symbol=ETH";
        String btcUrl = "https://data.block.cc/api/v1/price?symbol=BTC";
        JSONObject usdtResult = null;
        JSONObject ethResult = null;
        JSONObject btcResult = null;
        try {
            usdtResult = restTemplate.getForObject(usdtUrl, JSONObject.class);
            ethResult = restTemplate.getForObject(ethUrl, JSONObject.class);
            btcResult = restTemplate.getForObject(btcUrl, JSONObject.class);
        } catch (RestClientException e) {
            log.warn(e.getMessage());
            return;
        }
        BigDecimal usdtPrice = parseValue(usdtResult);
        BigDecimal ethPrice = parseValue(ethResult);
        BigDecimal btcPrice = parseValue(btcResult);
        commonTokenPriceService.saveHistory(BusinessConstant.BASE_TOKEN_ID_USDT, usdtPrice);
        commonTokenPriceService.saveHistory(BusinessConstant.BASE_TOKEN_ID_ETH, ethPrice);
        commonTokenPriceService.saveHistory(BusinessConstant.BASE_TOKEN_ID_BTC, btcPrice);
    }

    /**
     * @param obj
     * @return cny value
     */
    private BigDecimal parseValue(JSONObject obj) {
        try {
            ExchangeRateVO usdRate = getRate();
            if (null != usdRate) {
                Object data = ((ArrayList<LinkedHashMap>) obj.get("data")).get(0).get("price");
                BigDecimal number = NumberUtils.parseNumber(String.valueOf(data), BigDecimal.class);
                return number.multiply(BigDecimal.valueOf(usdRate.getValue()));
            }
            return null;
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }

    private ExchangeRateVO getRate() {
        ExchangeRateVO usdRate = null;
        List<ExchangeRateVO> rate = new ArrayList<>(3);
        String key = RedisConstant.EXCHANGE_RATE;
        var result = redisTemplate.opsForValue().get(key);
        if (null == result) {
            String url = "http://web.juhe.cn:8080/finance/exchange/rmbquot?key=eca6dd52126d970462ed32a77f72a636&type=1";
            String responseStr = restTemplate.getForObject(url, String.class);
            ExchangeResponse response = JSON.parseObject(responseStr, new TypeReference<ExchangeResponse>() {
            });
            if (response.getResultcode() == 200) {
                rate.add(getRate(response.getResult(), "CNY"));
                usdRate = getRate(response.getResult(), "USD");
                rate.add(usdRate);
                rate.add(getRate(response.getResult(), "EUR"));
                result = JSON.toJSONString(rate);
                redisTemplate.opsForValue().set(key, result, 12, TimeUnit.HOURS);
            }
        } else {
            List<ExchangeRateVO> list = JSON.parseArray(result, ExchangeRateVO.class);
            usdRate = list.stream().filter(obj ->"$ USD".equalsIgnoreCase( obj.getName())).collect(Collectors.toList()).get(0);

        }
        return usdRate;
    }

    public ExchangeRateVO getRate(Map<String, JSONObject> map, String name) {
        ExchangeRateVO vo = new ExchangeRateVO();
        if ("cny".equalsIgnoreCase(name)) {
            vo.setValue(1f);
            vo.setName("¥ " + name.toUpperCase());
        } else if ("USD".equalsIgnoreCase(name)) {
            Float value = Float.valueOf(map.get("美元").get("bankConversionPri").toString());
            vo.setValue(value / 100);
            vo.setName("$ " + name.toUpperCase());
        } else if ("EUR".equalsIgnoreCase(name)) {
            Float value = Float.valueOf(map.get("欧元").get("bankConversionPri").toString());
            vo.setValue(value / 100);
            vo.setName(name.toUpperCase());
            vo.setName("€ " + name.toUpperCase());
        }
        return vo;
    }

}
