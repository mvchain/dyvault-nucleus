package com.mvc.dyvault.app.controller;

import com.mvc.dyvault.app.service.*;
import com.mvc.dyvault.common.bean.vo.Result;
import com.mvc.dyvault.common.util.BaseContextHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.client.RestTemplate;

import java.math.BigInteger;

/**
 * @author qiyichen
 * @create 2018/11/6 17:11
 */
public class BaseController {

    @Autowired
    protected AssetService assetService;
    @Autowired
    protected MessageService messageService;
    @Autowired
    protected TokenService tokenService;
    @Autowired
    protected UserService userService;

    @Autowired
    protected StringRedisTemplate redisTemplate;
    protected RestTemplate restTemplate = new RestTemplate();

    protected Result mockResult = new Result(500, "服务器错误", null);

    protected BigInteger getUserId() {
        BigInteger userId = (BigInteger) BaseContextHandler.get("userId");
        return userId;
    }

}
