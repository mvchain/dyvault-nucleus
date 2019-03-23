package com.mvc.dyvault.sdk.service;

import com.mvc.dyvault.common.bean.vo.Result;
import com.mvc.dyvault.common.sdk.vo.PaymentVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@Service
public class ShopService {

    @Autowired
    ConsoleRemoteService remoteService;
    @Autowired
    StringRedisTemplate redisTemplate;


    public List<PaymentVO> getPaymentDetail(BigInteger shopId) {
        Result<List<PaymentVO>> result = remoteService.getPaymentDetail(shopId);
        return result.getData();
    }
}
