package com.mvc.dyvault.sdk.controller.web;

import com.github.pagehelper.PageInfo;
import com.mvc.dyvault.common.bean.vo.Result;
import com.mvc.dyvault.common.constant.RedisConstant;
import com.mvc.dyvault.common.sdk.dto.BusinessTxSearchDTO;
import com.mvc.dyvault.common.sdk.dto.DevDTO;
import com.mvc.dyvault.common.sdk.vo.BusinessOrderVO;
import com.mvc.dyvault.common.sdk.vo.BusinessTxCountVO;
import com.mvc.dyvault.common.sdk.vo.DevVO;
import com.mvc.dyvault.common.sdk.vo.ShopVO;
import com.mvc.dyvault.common.util.BaseContextHandler;
import com.mvc.dyvault.sdk.controller.BaseController;
import com.mvc.dyvault.sdk.util.ExcelException;
import com.mvc.dyvault.sdk.util.ExcelUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Cleanup;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author qiyichen
 * @create 2018/11/19 19:48
 */
@Api(tags = "business api")
@RestController
@RequestMapping("business")
public class BusinessWebController extends BaseController {

    private static LinkedHashMap<String, String> txMap = new LinkedHashMap<>();
    private static LinkedHashMap<String, String> countMap = new LinkedHashMap<>();

    @ApiOperation("get develop setting")
    @GetMapping("develop")
    public Result<DevVO> getDevSetting() {
        DevVO result = businessService.getDevSetting(getUserId());
        return new Result<>(result);
    }

    @ApiOperation("set develop setting")
    @PostMapping("develop")
    public Result<Boolean> getDevSetting(@RequestBody DevDTO devDTO) {
        Boolean result = businessService.setDevSetting(getUserId(), devDTO);
        return new Result<>(result);
    }

    @ApiOperation("search tx list")
    @GetMapping("")
    public Result<PageInfo<BusinessOrderVO>> getList(@ModelAttribute BusinessTxSearchDTO businessTxSearchDTO) {
        PageInfo<BusinessOrderVO> result = businessService.getList(getUserId(), businessTxSearchDTO);
        return new Result<>(result);
    }

    @ApiOperation("export search tx list")
    @GetMapping("excel")
    public void getListExcel(HttpServletResponse response, @RequestParam String sign, @ModelAttribute BusinessTxSearchDTO businessTxSearchDTO) throws IOException, ExcelException {
        getUserIdBySign(sign);
        PageInfo<BusinessOrderVO> result = businessService.getList(getUserId(), businessTxSearchDTO);
        response.setContentType("application/octet-stream;charset=ISO8859-1");
        response.addHeader("Content-Disposition", "attachment; filename=" + String.format("business_transaction_%s.xls", System.currentTimeMillis()));
        @Cleanup OutputStream os = response.getOutputStream();
        ExcelUtil.listToExcel(result.getList(), getTransactionMap(), "BusinessTransactionTable", os);
    }

    @ApiOperation("get business count")
    @GetMapping("count")
    public Result<List<BusinessTxCountVO>> getTxCount(@RequestParam Long startedAt, @RequestParam Long stopAt) {
        List<BusinessTxCountVO> result = businessService.getTxCount(getUserId(), startedAt, stopAt);
        return new Result<>(result);
    }

    @ApiOperation("get business count excel")
    @GetMapping("count/excel")
    public void getTxCountExcel(HttpServletResponse response, @RequestParam String sign, @RequestParam Long startedAt, @RequestParam Long stopAt) throws IOException, ExcelException {
        getUserIdBySign(sign);
        List<BusinessTxCountVO> result = businessService.getTxCount(getUserId(), startedAt, startedAt);
        response.setContentType("application/octet-stream;charset=ISO8859-1");
        response.addHeader("Content-Disposition", "attachment; filename=" + String.format("business_transaction_count_%s.xls", System.currentTimeMillis()));
        @Cleanup OutputStream os = response.getOutputStream();
        ExcelUtil.listToExcel(result, getTransactionCountMap(), "BusinessTransactionCountTable", os);
    }

    private LinkedHashMap<String, String> getTransactionCountMap() {
        if (countMap.isEmpty()) {
            countMap.put("createdAt", "时间");
            countMap.put("txCount", "充值订单数");
            countMap.put("cnyStr", "累计法币金额");
            countMap.put("tokenValue", "数字货币金额");
        }
        return countMap;
    }


    private LinkedHashMap<String, String> getTransactionMap() {
        if (txMap.isEmpty()) {
            txMap.put("orderNumber", "商户充值订单号");
            txMap.put("selfOrderNumber", "划帐订单号");
            txMap.put("createdAt", "下单时间");
            txMap.put("payAt", "到账时间");
            txMap.put("buyUsername", "Topay注册手机号");
            txMap.put("buyUserId", "用户ID");
            txMap.put("amount", "法币金额");
            txMap.put("tokenValue", "数字货币金额");
            txMap.put("statusStr", "状态");
        }
        return txMap;
    }

    protected BigInteger getUserIdBySign(String sign) {
        Assert.isTrue(!StringUtils.isEmpty(sign) && sign.length() > 32, "请登录后下载");
        String str = sign.substring(32);
        BigInteger userId = new BigInteger(str);
        String key = RedisConstant.EXPORT + userId;
        String result = redisTemplate.opsForValue().get(key);
        Assert.isTrue(null != result && result.equalsIgnoreCase(sign), "请登录后下载");
        BaseContextHandler.set("userId", userId);
        redisTemplate.delete(key);
        return userId;
    }

}
