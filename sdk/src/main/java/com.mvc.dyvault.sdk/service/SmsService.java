package com.mvc.dyvault.sdk.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class SmsService {

    @Autowired
    StringRedisTemplate redisTemplate;


    public Boolean sendSms(String cellphone) {
        return true;
    }
}
