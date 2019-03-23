package com.mvc.dyvault.sdk.service;

import com.mvc.dyvault.common.bean.vo.Result;
import com.mvc.dyvault.common.sdk.dto.ConfirmOrderDTO;
import com.mvc.dyvault.common.sdk.vo.OrderDetailVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

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
}
