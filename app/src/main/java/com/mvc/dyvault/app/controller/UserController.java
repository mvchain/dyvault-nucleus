package com.mvc.dyvault.app.controller;

import com.mvc.dyvault.app.service.MailService;
import com.mvc.dyvault.app.util.GeetestLib;
import com.mvc.dyvault.common.bean.AppUser;
import com.mvc.dyvault.common.bean.dto.*;
import com.mvc.dyvault.common.bean.vo.*;
import com.mvc.dyvault.common.constant.RedisConstant;
import com.mvc.dyvault.common.permission.NotLogin;
import com.mvc.dyvault.common.swaggermock.SwaggerMock;
import com.mvc.dyvault.common.util.InviteUtil;
import com.mvc.dyvault.common.util.JwtHelper;
import com.mvc.dyvault.common.util.MessageConstants;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.LoginException;
import javax.validation.Valid;
import java.math.BigInteger;
import java.util.HashMap;

/**
 * 用户相关
 *
 * @author qiyichen
 * @create 2018/11/6 19:02
 */
@Api(tags = "用户相关")
@RequestMapping("user")
@RestController
@Log
public class UserController extends BaseController {

    @Autowired
    MailService mailService;
    @Autowired
    GeetestLib gtSdk;

    @ApiOperation("获取验证码信息")
    @GetMapping("valid")
    @NotLogin
    public Result<ValidVO> getValiMsg(@RequestParam String email) {
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("user_id", email);
        int gtServerStatus = gtSdk.preProcess(param);
        //进行验证预处理
        String resStr = gtSdk.getResponseStr();
        ValidVO vo = new ValidVO();
        vo.setResult(resStr);
        vo.setUid(email);
        vo.setStatus(gtServerStatus);
        return new Result(vo);
    }

    @ApiOperation("验证验证码")
    @PostMapping("valid")
    @NotLogin
    public Result<String> checkValiImage(@RequestBody ValidDTO validDTO) {
        String challenge = validDTO.getGeetest_challenge();
        String validate = validDTO.getGeetest_validate();
        String seccode = validDTO.getGeetest_seccode();
        //从session中获取userid
        String userid = validDTO.getUid();
        //自定义参数,可选择添加
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("user_id", userid);
        int gtResult = 0;
        if (validDTO.getStatus() == 1) {
            gtResult = gtSdk.enhencedValidateRequest(challenge, validate, seccode, param);
        } else {
            gtResult = gtSdk.failbackValidateRequest(challenge, validate, seccode);
        }
        String token = JwtHelper.create(validDTO.getUid(), BigInteger.ZERO, "valiCode");
        return new Result<>(gtResult == 1 ? token : "");
    }


    @ApiOperation("用户登录,缓存登录令牌.登录规则后续确定,如返回406则需要校验助记词(错误信息中返回助记词字符串)")
    @PostMapping("login")
    @SwaggerMock("${user.login}")
    @NotLogin
    public Result<TokenVO> login(@RequestBody @Valid UserDTO userDTO) {
        String key = RedisConstant.MAIL_VALI_PRE + userDTO.getUsername();
        Boolean result = mailService.checkSmsValiCode(userDTO.getUsername(), userDTO.getValidCode());
        Assert.isTrue(result, MessageConstants.getMsg("SMS_ERROR"));
        TokenVO vo = userService.login(userDTO);
        redisTemplate.delete(key);
        return new Result(vo);
    }

    @PostMapping("refresh")
    @ApiOperation("刷新令牌")
    Result<String> refresh() throws LoginException {
        String result = userService.refresh();
        return new Result(result);
    }

    @ApiOperation("用户信息获取")
    @GetMapping("info")
    @SwaggerMock("${user.info}")
    public Result<UserSimpleVO> getInfo() {
        UserSimpleVO user = userService.getUserById(getUserId());
        return new Result<>(user);
    }

    @ApiOperation("获取邮箱验证码, 10分钟内有效, 以最后一次为准")
    @GetMapping("email/logout")
    @NotLogin
    public Result<Boolean> getSms(@RequestParam String email) {
        mailService.send(email);
        return new Result<>(true);
    }

    @ApiOperation("校验注册信息,返回一个一次性的token,需要在后续注册时传入")
    @NotLogin
    @PostMapping("")
    public Result<String> regCheck(@RequestBody AppuserRegCheckDTO appuserRegCheckDTO) {
        String key = RedisConstant.MAIL_VALI_PRE + appuserRegCheckDTO.getEmail();
        Boolean result = mailService.checkSmsValiCode(appuserRegCheckDTO.getEmail(), appuserRegCheckDTO.getValiCode());
        Assert.isTrue(result, MessageConstants.getMsg("SMS_ERROR"));
        AppUser user = userService.getUserByUsername(appuserRegCheckDTO.getEmail());
        Assert.isTrue(null == user, MessageConstants.getMsg("USER_EXIST"));
        Long id = 0L;
        if (StringUtils.isNotBlank(appuserRegCheckDTO.getInviteCode())) {
            id = InviteUtil.codeToId(appuserRegCheckDTO.getInviteCode());
            Assert.isTrue(null != id && !id.equals(0L), MessageConstants.getMsg("INVITE_ERROR"));
            UserSimpleVO vo = userService.getUserById(BigInteger.valueOf(id));
            Assert.isTrue(null != vo, MessageConstants.getMsg("INVITE_ERROR"));
        }
        String tempToken = JwtHelper.createReg(appuserRegCheckDTO.getEmail(), BigInteger.valueOf(id));
        redisTemplate.delete(key);
        return new Result<>(tempToken);
    }

    @ApiOperation("用户注册,需要将之前的信息和token一起带入进行校验")
    @NotLogin
    @PostMapping("register")
    public Result<AppUserRetVO> register(@RequestBody AppUserDTO appUserDTO) {
        Claims claim = JwtHelper.parseJWT(appUserDTO.getToken());
        Assert.notNull(claim, MessageConstants.getMsg("TOKEN_EXPIRE"));
        String username = claim.get("username", String.class);
        String type = claim.get("type", String.class);
        Assert.isTrue("reg".equals(type), MessageConstants.getMsg("TOKEN_EXPIRE"));
        BigInteger userId = null;
        try {
            userId = claim.get("userId", BigInteger.class);
        } catch (Exception e) {
            Integer userIdInt = claim.get("userId", Integer.class);
            userId = BigInteger.valueOf(userIdInt);
        }
        if (StringUtils.isNotBlank(appUserDTO.getInviteCode())) {
            Long id = InviteUtil.codeToId(appUserDTO.getInviteCode());
            Boolean checkResult = userId.longValue() == id && appUserDTO.getEmail().equals(username);
            Assert.isTrue(id != 0L, MessageConstants.getMsg("INVITE_ERROR"));
            Assert.isTrue(checkResult, MessageConstants.getMsg("REGISTER_WRONG"));
        }
        AppUserRetVO vo = userService.register(appUserDTO);
        return new Result<>(vo);
    }

    @ApiOperation("重置密码,返回一次性token")
    @PostMapping("reset")
    @NotLogin
    public Result<String> reset(@RequestBody AppUserResetDTO appUserResetDTO) {
        AppUser user = userService.reset(appUserResetDTO);
        Assert.notNull(user, MessageConstants.getMsg("SMS_ERROR"));
        String tempToken = JwtHelper.createForget(user.getEmail(), user.getId());
        return new Result<>(tempToken);
    }

    @ApiOperation("忘记密码(修改密码)")
    @NotLogin
    @PutMapping("forget")
    public Result<Boolean> forget(@RequestBody PasswordDTO passwordDTO) {
        Claims claim = JwtHelper.parseJWT(passwordDTO.getToken());
        Assert.notNull(claim, MessageConstants.getMsg("TOKEN_EXPIRE"));
        String type = claim.get("type", String.class);
        Assert.isTrue("forget".equals(type), MessageConstants.getMsg("TOKEN_EXPIRE"));
        BigInteger userId = null;
        try {
            userId = claim.get("userId", BigInteger.class);
        } catch (Exception e) {
            Integer userIdInt = claim.get("userId", Integer.class);
            userId = BigInteger.valueOf(userIdInt);
        }
        userService.forget(userId, passwordDTO.getPassword(), passwordDTO.getType());
        return new Result<>(true);
    }

    @ApiOperation("登录密码修改")
    @PutMapping("password")
    public Result<Boolean> updatePwd(@RequestBody AppUserPwdDTO appUserPwdDTO) {
        userService.updatePwd(getUserId(), appUserPwdDTO);
        log.info(appUserPwdDTO.getPassword());
        return new Result<>(true);
    }

    @ApiOperation("支付密码修改")
    @PutMapping("transactionPassword")
    public Result<Boolean> updateTransPwd(@RequestBody AppUserPwdDTO appUserPwdDTO) {
        userService.updateTransPwd(getUserId(), appUserPwdDTO);
        log.info(appUserPwdDTO.getPassword());
        return new Result<>(true);
    }

    @ApiOperation("修改绑定邮箱")
    @PutMapping("email")
    public Result<Boolean> updateEmail(@RequestBody AppUserMailDTO appUserMailDTO) {
        Boolean result = mailService.checkSmsValiCode(appUserMailDTO.getEmail(), appUserMailDTO.getValiCode());
        Assert.isTrue(result, MessageConstants.getMsg("SMS_ERROR"));
        Claims claim = JwtHelper.parseJWT(appUserMailDTO.getToken());
        String type = claim.get("type", String.class);
        Assert.isTrue("email".equals(type), MessageConstants.getMsg("TOKEN_EXPIRE"));
        BigInteger userId = null;
        try {
            userId = claim.get("userId", BigInteger.class);
        } catch (Exception e) {
            Integer userIdInt = claim.get("userId", Integer.class);
            userId = BigInteger.valueOf(userIdInt);
        }
        Assert.isTrue(userId.equals(getUserId()), MessageConstants.getMsg("TOKEN_EXPIRE"));
        userService.updateEmail(getUserId(), appUserMailDTO.getEmail());
        return new Result<>(true);
    }

    @ApiOperation("校验邮箱状态(修改密码时第一步校验)")
    @PostMapping("email")
    public Result<String> checkEmail(@RequestBody AppUserEmailDTO appUserEmailDTO) {
        String email = userService.getEmail(getUserId());
        Boolean result = mailService.checkSmsValiCode(email, appUserEmailDTO.getValiCode());
        Assert.isTrue(result, MessageConstants.getMsg("SMS_ERROR"));
        String token = JwtHelper.create(email, getUserId(), "email");
        return new Result<>(token);
    }

    @ApiOperation("发送验证码(不输入邮箱地址,直接取当前用户注册邮箱)")
    @GetMapping(value = "email", headers = "Authorization")
    public Result<Boolean> getEmail() {
        String email = userService.getEmail(getUserId());
        mailService.send(email);
        return new Result<>(true);
    }

}
