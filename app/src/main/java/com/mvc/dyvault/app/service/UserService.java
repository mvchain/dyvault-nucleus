package com.mvc.dyvault.app.service;

import com.mvc.dyvault.app.feign.ConsoleRemoteService;
import com.mvc.dyvault.common.bean.AppUser;
import com.mvc.dyvault.common.bean.dto.*;
import com.mvc.dyvault.common.bean.vo.*;
import com.mvc.dyvault.common.util.*;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.security.auth.login.LoginException;
import java.math.BigInteger;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class UserService {

    @Autowired
    ConsoleRemoteService userRemoteService;
    @Autowired
    StringRedisTemplate redisTemplate;
    @Autowired
    MailService mailService;

    public UserSimpleVO getUserById(BigInteger userId) {
        UserSimpleVO vo = new UserSimpleVO();
        Result<AppUser> userResult = userRemoteService.getUserById(userId);
        AppUser user = userResult.getData();
        Assert.notNull(user, MessageConstants.getMsg("USER_PASS_WRONG"));
        vo.setNickname(user.getNickname());
        vo.setUsername(mailService.getMail(user.getEmail()));
        return vo;
    }

    public TokenVO login(UserDTO userDTO) {
        TokenVO vo = new TokenVO();
        String redisKey = "LOGIN_WRONG_TIME_" + userDTO.getUsername();
        checkWrongTimes(redisKey, userDTO.getImageToken(), userDTO.getUsername());
        Result<AppUser> userResult = userRemoteService.getUserByUsername(userDTO.getUsername());
        AppUser user = userResult.getData();
        wrongLogin(null != user, redisKey);
        Assert.notNull(user, MessageConstants.getMsg("USER_PASS_WRONG"));
        Assert.isTrue(user.getStatus() == 1 || user.getStatus() == 4, MessageConstants.getMsg("ACCOUNT_LOCK"));
        Boolean passwordCheck = user.getPassword().equals(userDTO.getPassword());
        wrongLogin(passwordCheck, redisKey);
        if (user.getStatus() == 4) {
            List<String> list = MnemonicUtil.getWordsList(user.getPvKey());
            Collections.shuffle(list);
            throw new PvkeyException(StringUtils.join(list, ","));
        }
        Assert.isTrue(passwordCheck, MessageConstants.getMsg("USER_PASS_WRONG"));
        String token = JwtHelper.createToken(userDTO.getUsername(), user.getId());
        String refreshToken = JwtHelper.createRefresh(userDTO.getUsername(), user.getId());
        //密码正确后清空错误次数
        redisTemplate.delete(redisKey);
        vo.setRefreshToken(refreshToken);
        vo.setToken(token);
        vo.setUserId(user.getId());
        vo.setEmail(user.getEmail());
        return vo;
    }

    private Boolean checkWrongTimes(String redisKey, String imageToken, String email) {
        Long wrongTimes = redisTemplate.opsForHash().increment(redisKey, "TIME", 0);
        if (null == wrongTimes || wrongTimes.compareTo(5L) < 0) {
            return true;
        }
        Claims claim = JwtHelper.parseJWT(imageToken);
        if (null == claim) {
            throw new PassWrongMoreException(MessageConstants.getMsg("USER_PASS_WRONG_MORE"), 402);
        }
        String username = claim.get("username", String.class);
        String type = claim.get("type", String.class);
        if (!type.equalsIgnoreCase("valiCode") || !username.equalsIgnoreCase(email)) {
            throw new PassWrongMoreException(MessageConstants.getMsg("USER_PASS_WRONG_MORE"), 402);
        }
        return true;
    }

    private void wrongLogin(Boolean passwordCheck, String redisKey) {
        if (!passwordCheck) {
            //从第一次错误时间开始计时,10分钟内每次错误则错误次数加1
            Long wrongTimes = redisTemplate.opsForHash().increment(redisKey, "TIME", 1);
            if (wrongTimes.equals(1L)) {
                redisTemplate.expire(redisKey, 10, TimeUnit.MINUTES);
            }
            //超过5次错误则抛出错误,下一次需要输入验证码
            if (wrongTimes.compareTo(5L) >= 0) {
                redisTemplate.expire(redisKey, 1, TimeUnit.HOURS);
                throw new PassWrongMoreException(MessageConstants.getMsg("USER_PASS_WRONG_MORE"), 402);
            }
        }
    }

    public String refresh() throws LoginException {
        BigInteger userId = (BigInteger) BaseContextHandler.get("userId");
        String username = (String) BaseContextHandler.get("username");
        Result<AppUser> userResult = userRemoteService.getUserByUsername(username);
        AppUser user = userResult.getData();
        if (null == user) {
            return null;
        }
        if (user.getStatus() == 0) {
            throw new LoginException();
        }
        return JwtHelper.createToken(username, userId);
    }

    public AppUser getUserByUsername(String cellphone) {
        Result<AppUser> userResult = userRemoteService.getUserByUsername(cellphone);
        return userResult.getData();
    }

    public AppUserRetVO register(AppUserDTO appUserDTO) {
        Result<AppUserRetVO> userResult = userRemoteService.register(appUserDTO);
        mnemonicsActive(appUserDTO.getEmail());
        return userResult.getData();
    }

    public void mnemonicsActive(String email) {
        userRemoteService.mnemonicsActive(email);
    }

    public void forget(BigInteger userId, String password, Integer type) {
        userRemoteService.forget(userId, password, type);
    }

    public AppUser reset(AppUserResetDTO appUserResetDTO) {
        Boolean result = mailService.checkSmsValiCode(appUserResetDTO.getEmail(), appUserResetDTO.getValue());
        Assert.isTrue(result, MessageConstants.getMsg("SMS_ERROR"));
        AppUser user = userRemoteService.getUserByUsername(appUserResetDTO.getEmail()).getData();
        Assert.notNull(user, MessageConstants.getMsg("SMS_ERROR"));
        return user;
    }

    public void updatePwd(BigInteger userId, AppUserPwdDTO appUserPwdDTO) {
        AppUser user = userRemoteService.getUserById(userId).getData();
        Assert.isTrue(user.getPassword().equals(appUserPwdDTO.getPassword()), MessageConstants.getMsg("USER_PASS_WRONG"));
        user = new AppUser();
        user.setId(userId);
        user.setPassword(appUserPwdDTO.getNewPassword());
        userRemoteService.updateUser(user);
    }

    public void updateTransPwd(BigInteger userId, AppUserPwdDTO appUserPwdDTO) {
        AppUser user = userRemoteService.getUserById(userId).getData();
        Assert.isTrue(user.getTransactionPassword().equals(appUserPwdDTO.getPassword()), MessageConstants.getMsg("USER_PASS_WRONG"));
        user = new AppUser();
        user.setId(userId);
        user.setTransactionPassword(appUserPwdDTO.getNewPassword());
        userRemoteService.updateUser(user);
    }

    public void updateEmail(BigInteger userId, String email) {
        AppUser user = new AppUser();
        user.setId(userId);
        user.setEmail(email);
        userRemoteService.updateUser(user);
    }

    public String getEmail(BigInteger userId) {
        Result<AppUser> userResult = userRemoteService.getUserById(userId);
        AppUser user = userResult.getData();
        return user.getEmail();
    }
}
