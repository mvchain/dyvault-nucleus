package com.mvc.dyvault.sdk.service;

import com.mvc.dyvault.common.bean.AppUser;
import com.mvc.dyvault.common.bean.vo.Result;
import com.mvc.dyvault.common.bean.vo.TokenVO;
import com.mvc.dyvault.common.util.BaseContextHandler;
import com.mvc.dyvault.common.util.JwtHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.security.auth.login.LoginException;
import java.math.BigInteger;

@Service
public class UserService {

    @Autowired
    ConsoleRemoteService remoteService;
    @Autowired
    StringRedisTemplate redisTemplate;


    public TokenVO login(String cellphone) {
        TokenVO vo = new TokenVO();
        Result<AppUser> userResult = remoteService.getUserByCellphone(cellphone);
        if (null == userResult.getData()) {
            userResult = remoteService.regUser(cellphone);
        }
        AppUser user = userResult.getData();
        String token = JwtHelper.createToken(cellphone, user.getId());
        String refreshToken = JwtHelper.createRefresh(cellphone, user.getId());
        //密码正确后清空错误次数
        vo.setRefreshToken(refreshToken);
        vo.setToken(token);
        vo.setUserId(user.getId());
        vo.setEmail(user.getCellphone());
        vo.setIsBusinesses(user.getIsBusinesses());
        vo.setSalt(user.getSalt());
        return vo;
    }

    public String refresh() throws LoginException {
        BigInteger userId = (BigInteger) BaseContextHandler.get("userId");
        String username = (String) BaseContextHandler.get("username");
        Result<AppUser> userResult = remoteService.getUserByCellphone(username);
        AppUser user = userResult.getData();
        if (null == user) {
            return null;
        }
        if (user.getStatus() == 0) {
            throw new LoginException();
        }
        return JwtHelper.createToken(username, userId);
    }

}
