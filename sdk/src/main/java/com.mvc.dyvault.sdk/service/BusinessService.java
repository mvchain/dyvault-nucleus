package com.mvc.dyvault.sdk.service;

import com.github.pagehelper.PageInfo;
import com.mvc.dyvault.common.bean.vo.Result;
import com.mvc.dyvault.common.sdk.dto.BusinessTxSearchDTO;
import com.mvc.dyvault.common.sdk.dto.ConfirmOrderDTO;
import com.mvc.dyvault.common.sdk.dto.DevDTO;
import com.mvc.dyvault.common.sdk.vo.*;
import com.mvc.dyvault.sdk.bean.ToPayEntity;
import com.mvc.dyvault.sdk.bean.ToPayResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@Service
public class BusinessService {

    @Autowired
    ConsoleRemoteService remoteService;
    @Autowired
    StringRedisTemplate redisTemplate;

    public OrderDetailVO checkOrderExist(BigInteger userId) {
        Result<OrderDetailVO> result = remoteService.checkOrderExist(userId);
        return result.getData();
    }

    public BigInteger confirmOrder(BigInteger userId, ConfirmOrderDTO confirmOrderDTO) {
        Result<BigInteger> result = remoteService.confirmOrder(userId, confirmOrderDTO);
        return result.getData();
    }

    public Boolean updateStatus(BigInteger userId, BigInteger id, Integer status, String payAccount) {
        Result<Boolean> result = remoteService.updateStatus(userId, id, status, payAccount);
        return result.getData();
    }

    public OrderDetailVO getDetail(BigInteger userId, BigInteger id) {
        Result<OrderDetailVO> result = remoteService.getDetail(userId, id);
        return result.getData();
    }

    public PageInfo<ShopVO> getShop() {
        Result<PageInfo<ShopVO>> result = remoteService.getShop();
        return result.getData();
    }

    public DevVO getDevSetting(BigInteger id) {
        Result<DevVO> result = remoteService.getDevSetting(id);
        return result.getData();
    }

    public Boolean setDevSetting(BigInteger id, DevDTO devDTO) {
        Result<Boolean> result = remoteService.setDevSetting(id, devDTO);
        return result.getData();
    }

    public PageInfo<BusinessOrderVO> getList(BigInteger id, BusinessTxSearchDTO businessTxSearchDTO) {
        Result<PageInfo<BusinessOrderVO>> result = remoteService.getBusinessList(id, businessTxSearchDTO);
        return result.getData();
    }

    public List<BusinessTxCountVO> getTxCount(BigInteger id, Long startedAt, Long stopAt) {
        Result<List<BusinessTxCountVO>> result = remoteService.getBusinessCount(id, startedAt, stopAt);
        return result.getData();
    }

    public ToPayResponse createOrder(ToPayEntity toPayEntity) {
        ToPayResponse response = remoteService.createOrder(toPayEntity);
        return response;
    }
}
