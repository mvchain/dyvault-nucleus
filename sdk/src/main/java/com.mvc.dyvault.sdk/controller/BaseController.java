package com.mvc.dyvault.sdk.controller;

import com.mvc.dyvault.common.util.BaseContextHandler;
import com.mvc.dyvault.sdk.service.BusinessService;
import com.mvc.dyvault.sdk.service.ShopService;
import com.mvc.dyvault.sdk.service.SmsService;
import com.mvc.dyvault.sdk.service.UserService;
import com.mvc.dyvault.sdk.util.SmsSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.math.BigInteger;

/**
 * @author qiyichen
 * @create 2018/11/6 17:11
 */
public class BaseController {

    @Autowired
    protected StringRedisTemplate redisTemplate;
    @Autowired
    protected BusinessService businessService;
    @Autowired
    protected UserService userService;
    @Autowired
    protected ShopService shopService;
    @Autowired
    protected SmsSender smsSender;


    protected BigInteger getUserId() {
        BigInteger userId = (BigInteger) BaseContextHandler.get("userId");
        return userId;
    }

}
