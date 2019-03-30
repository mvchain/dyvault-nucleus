package com.mvc.dyvault.console.service;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mvc.dyvault.common.bean.*;
import com.mvc.dyvault.common.bean.dto.BusinessSearchDTO;
import com.mvc.dyvault.common.bean.dto.BusinessTransactionSearchDTO;
import com.mvc.dyvault.common.bean.vo.BusinessDetailVO;
import com.mvc.dyvault.common.bean.vo.BusinessSimpleVO;
import com.mvc.dyvault.common.sdk.dto.CallbackDTO;
import com.mvc.dyvault.common.sdk.dto.ConfirmOrderDTO;
import com.mvc.dyvault.common.sdk.vo.BusinessTxCountVO;
import com.mvc.dyvault.common.sdk.vo.OrderDetailVO;
import com.mvc.dyvault.common.util.ConditionUtil;
import com.mvc.dyvault.common.util.MessageConstants;
import com.mvc.dyvault.console.bean.ToPayEntity;
import com.mvc.dyvault.console.bean.ToPayResponse;
import com.mvc.dyvault.console.common.AbstractService;
import com.mvc.dyvault.console.common.BaseService;
import com.mvc.dyvault.console.constant.BusinessConstant;
import com.mvc.dyvault.console.dao.BusinessTransactionMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BusinessTransactionService extends AbstractService<BusinessTransaction> implements BaseService<BusinessTransaction> {

    @Autowired
    BusinessTransactionMapper businessTransactionMapper;
    @Autowired
    AppUserService appUserService;
    @Autowired
    ShopService shopService;
    @Autowired
    CommonTokenService tokenService;
    @Autowired
    CommonTokenPriceService tokenPriceService;
    @Autowired
    BusinessSupplierService supplierService;
    @Autowired
    AppUserBalanceService balanceService;
    @Autowired
    AppOrderService appOrderService;
    @Autowired
    JPushService pushService;

    RestTemplate restTemplate = new RestTemplate();

    public List<BusinessSimpleVO> getBusinessList(BusinessSearchDTO pageDTO, BigInteger userId) {
        PageHelper.startPage(1, pageDTO.getPageSize(), "id desc");
        Condition condition = new Condition(BusinessTransaction.class);
        Example.Criteria criteria = condition.createCriteria();
        if (null != pageDTO.getId() && !pageDTO.getId().equals(BigInteger.ZERO)) {
            ConditionUtil.andCondition(criteria, "id < ", pageDTO.getId());
        }
        if (pageDTO.getStatus() != 0) {
            ConditionUtil.andCondition(criteria, "status = ", pageDTO.getStatus());
        }
        ConditionUtil.andCondition(criteria, String.format("(buy_user_id = %s or sell_user_id = %s )", userId, userId));
        List<BusinessSimpleVO> result = findByCondition(condition).stream().map(obj -> {
            BusinessSimpleVO vo = new BusinessSimpleVO();
            BeanUtils.copyProperties(obj, vo);
            return vo;

        }).collect(Collectors.toList());
        return null == result || result.size() == 0 ? new ArrayList<BusinessSimpleVO>() : result;
    }

    public BusinessDetailVO getBusiness(BigInteger id, BigInteger userId) {
        BusinessDetailVO result = new BusinessDetailVO();
        BusinessTransaction tx = findById(id);
        if (null == tx || !(tx.getBuyUserId().equals(userId) || tx.getSellUserId().equals(userId))) {
            return result;
        }
        BeanUtils.copyProperties(tx, result);
        result.setLimitTime(getLimitTime(tx));
        result.setShopId(shopService.findOneBy("userId", tx.getSellUserId()).getId());
        return result;
    }

    private Long getLimitTime(BusinessTransaction tx) {
        Long time = tx.getLimitTime() - (System.currentTimeMillis() - tx.getCreatedAt());
        return time >= 0 ? time : 0L;
    }

    public OrderDetailVO getDetail(BigInteger userId, BigInteger id) {
        BusinessTransaction tx = findById(id);
        if (!(tx.getSellUserId().equals(userId) || tx.getBuyUserId().equals(userId))) {
            return null;
        }
        OrderDetailVO result = new OrderDetailVO();
        BeanUtils.copyProperties(tx, result);
        result.setLimitTime(getLimitTime(tx));
        result.setShopId(shopService.findOneBy("userId", tx.getSellUserId()).getId());
        return result;
    }

    public OrderDetailVO checkOrderExist(BigInteger userId) {
        Condition condition = new Condition(BusinessTransaction.class);
        Example.Criteria criteria = condition.createCriteria();
        ConditionUtil.andCondition(criteria, "status = ", 1);
        ConditionUtil.andCondition(criteria, String.format("(buy_user_id = %s or sell_user_id = %s )", userId, userId));
        List<BusinessTransaction> tx = findByCondition(condition);
        if (tx.size() > 0) {
            OrderDetailVO result = new OrderDetailVO();
            BeanUtils.copyProperties(tx.get(0), result);
            result.setShopId(shopService.findOneBy("userId", tx.get(0).getSellUserId()).getId());
            result.setLimitTime(getLimitTime(tx.get(0)));
            return result;
        }
        return null;
    }

    public BigInteger confirmOrder(BigInteger userId, ConfirmOrderDTO confirmOrderDTO) {
        Long time = System.currentTimeMillis();
        checkSign(confirmOrderDTO);
        BusinessShop shop = shopService.findById(confirmOrderDTO.getShopId());
        BusinessTransaction tx = new BusinessTransaction();
        BeanUtils.copyProperties(confirmOrderDTO, tx);
        AppUser user = appUserService.findById(userId);
        tx.setOrderType(1);
        tx.setOrderStatus(0);
        tx.setStatus(1);
        tx.setCreatedAt(time);
        tx.setBuyUserId(userId);
        tx.setBuyUsername(user.getNickname());
        tx.setSellUsername(shop.getShopName());
        tx.setCellphone(user.getCellphone());
        tx.setSellUserId(shop.getUserId());
        tx.setUserId(userId);
        tx.setUpdatedAt(time);
        tx.setLimitTime(60 * 1000 * 15L);
        tx.setPayAt(time);
        tx.setAutoSend(1);
        tx.setRemitShopId(confirmOrderDTO.getRemitShopId());
        tx.setRemitUserId(confirmOrderDTO.getRemitUserId());
        tx.setSelfOrderNumber(getOrderNumber());
        save(tx);
        notifyTx(tx);
        return tx.getId();
    }

    private void checkSign(ConfirmOrderDTO confirmOrderDTO) {

    }

    public Boolean updateStatus(BigInteger userId, BigInteger id, Integer status, String payAccount) {
        BusinessTransaction tx = findById(id);
        if (!tx.getUserId().equals(userId)) {
            return false;
        }
        if (status == 1) {
            tx.setPayAccount(payAccount);
            tx.setStatus(1);
            tx.setOrderStatus(1);
            tx.setPayAt(System.currentTimeMillis());
            tx.setUpdatedAt(System.currentTimeMillis());
            update(tx);
            updateCache(tx.getId());
        } else if (status == 4) {
            tx.setOrderStatus(4);
            tx.setStatus(status);
            tx.setUpdatedAt(System.currentTimeMillis());
            update(tx);
            updateCache(tx.getId());
        }
        sendPush(tx.getRemitUserId(), tx.getId(), tx.getStatus());
        return true;
    }


    private void sendPush(BigInteger userId, BigInteger id, Integer status) {
        if (status == 2 || status == 4) {
            HashMap<String, String> extra = new HashMap<>();
            extra.put("type", "ORDER_STATUS");
            extra.put("id", String.valueOf(id));
            extra.put("status", String.valueOf(status));
            pushService.send("order msg", extra, userId.toString());
        }

    }

    public List<BusinessTxCountVO> getBusinessCount(BigInteger id, Long startedAt, Long stopAt) {
        startedAt = null == startedAt ? 0L : startedAt;
        stopAt = null == stopAt ? System.currentTimeMillis() : stopAt;
        List<BusinessTxCountVO> result = businessTransactionMapper.getBusinessCount(id, startedAt, stopAt);
        return result;
    }

    public Boolean cancel(BigInteger userId, BigInteger txId) {
        BusinessTransaction tx = findById(txId);
        if (null == tx) {
            return false;
        }
        tx.setStatus(4);
        tx.setOrderStatus(4);
        tx.setUpdatedAt(System.currentTimeMillis());
        tx.setStopAt(System.currentTimeMillis());
        update(tx);
        updateCache(tx.getId());
        notifyTx(tx);
        return true;
    }

    private void notifyTx(BusinessTransaction tx) {
        BusinessShop shop = shopService.findOneBy("userId", tx.getSellUserId());
        if (null != shop) {
            sendToShop(tx);
        }
        sendPush(tx.getRemitUserId(), tx.getId(), tx.getStatus());
    }

    public PageInfo<BusinessTransaction> getSupplierTxList(BigInteger id, BusinessTransactionSearchDTO businessTransactionSearchDTO) {
        PageHelper.startPage(businessTransactionSearchDTO.getPageNum(), businessTransactionSearchDTO.getPageSize(), "id desc");
        Condition condition = new Condition(BusinessTransaction.class);
        Example.Criteria criteria = condition.createCriteria();
        ConditionUtil.andCondition(criteria, "order_number = ", businessTransactionSearchDTO.getOrderNumber());
        ConditionUtil.andCondition(criteria, "pay_account = ", businessTransactionSearchDTO.getPayAccount());
        List<BusinessTransaction> list = findByCondition(condition);
        list.forEach(obj -> {
            obj.setCellphone(appUserService.findById(obj.getBuyUserId()).getCellphone());
        });
        PageInfo result = new PageInfo(list);
        return result;
    }

    public ToPayResponse createOrder(ToPayEntity toPayEntity) {
        ToPayResponse response = check(toPayEntity);
        if (response.getStatus() != 200) {
            return response;
        }
        BusinessSupplier supplier = supplierService.findOneById(BigInteger.ZERO);
        BusinessShop shop = shopService.findOneBy("appKey", toPayEntity.getAppKey());
        CommonTokenPrice price = tokenPriceService.findById(toPayEntity.getTokenId());
        ConfirmOrderDTO dto = new ConfirmOrderDTO();
        BigDecimal priceValue = price.getTokenPrice().multiply(BigDecimal.ONE.add(BigDecimal.valueOf(supplier.getPriceDifferences() / 100)));
        dto.setAmount(toPayEntity.getCny());
        dto.setOrderNumber(toPayEntity.getOrderNumber());
        dto.setPrice(priceValue);
        dto.setRemitShopId(supplier.getId());
        dto.setRemitUserId(supplier.getUserId());
        dto.setSellUsername("官方认证商户");
        dto.setShopId(shop.getId());
        dto.setTokenId(toPayEntity.getTokenId());
        dto.setTokenName(tokenService.getTokenName(toPayEntity.getTokenId()));
        dto.setTokenValue(toPayEntity.getCny().divide(priceValue, 6, RoundingMode.DOWN));
        dto.setSign(getSign(dto.getAmount(), dto.getTokenValue(), dto.getTokenId(), dto.getRemitUserId(), dto.getShopId()));
        response.setConfirmOrderDTO(dto);
        return response;
    }

    private String getSign(BigDecimal amount, BigDecimal value, BigInteger tokenId, BigInteger remitUserId, BigInteger shopId) {
        return "asdfasdfasdfasfdasdfasdf";
    }

    private ToPayResponse check(ToPayEntity toPayEntity) {
        ToPayResponse response = new ToPayResponse();
        response.setStatus(400);
        response.setMessage("sign error");
        if (StringUtils.isBlank(toPayEntity.getSign()) || toPayEntity.getCny() == null || toPayEntity.getOrderNumber() == null || toPayEntity.getTokenId() == null) {
            return response;
        }
        BusinessShop shop = shopService.findOneBy("appKey", toPayEntity.getAppKey());

        Boolean checkResult = toPayEntity.checkSign(toPayEntity.getSign(), shop.getAppSecret());
        if (!checkResult) {
            return response;
        }
        response.setStatus(200);
        response.setMessage("ok");
        return response;
    }

    public Boolean confirmOrderComplete(BigInteger userId, BigInteger id) {
        BusinessTransaction tx = findById(id);
        checkBalance(userId, tx);
        tx.setStatus(2);
        tx.setOrderStatus(2);
        tx.setUpdatedAt(System.currentTimeMillis());
        tx.setStopAt(System.currentTimeMillis());
        update(tx);
        updateCache(tx.getId());
        saveOrder(tx);
        balanceService.updateBalance(tx.getRemitUserId(), BusinessConstant.BASE_TOKEN_ID_USDT, BigDecimal.ZERO.subtract(tx.getTokenValue()));
        sendToShop(tx);
        return true;
    }

    private void sendToShop(BusinessTransaction tx) {
        //change to mq
        BusinessShop shop = shopService.findOneBy("userId", tx.getSellUserId());
        String callbackUrl = shop.getCallbackUrl();
        CallbackDTO dto = new CallbackDTO();
        dto.setStatus(200);
        dto.setMessage(tx.getStatus() == 4 ? "calcel order" : "success");
        dto.setOrderNumber(tx.getOrderNumber());
        dto.setOrderStatus(tx.getOrderStatus());
        ResponseEntity<JSONObject> result = restTemplate.postForEntity(callbackUrl, dto, JSONObject.class);
        if (result.getStatusCode() != HttpStatus.OK) {
            //error
        }
    }

    private void saveOrder(BusinessTransaction tx) {
        appOrderService.saveOrder(tx);
    }

    private void checkBalance(BigInteger userId, BusinessTransaction tx) {
        BigDecimal balance = balanceService.getBalanceByTokenId(tx.getRemitUserId(), BusinessConstant.BASE_TOKEN_ID_USDT);
        Assert.isTrue(balance.compareTo(tx.getTokenValue()) >= 0, MessageConstants.getMsg("INSUFFICIENT_BALANCE"));
    }

    public BusinessTransaction getOverTx() {
        return businessTransactionMapper.getOverTx(System.currentTimeMillis());
    }

}