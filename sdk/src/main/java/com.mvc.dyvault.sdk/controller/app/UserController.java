package com.mvc.dyvault.sdk.controller.app;

import com.mvc.dyvault.common.bean.vo.Result;
import com.mvc.dyvault.common.bean.vo.TokenVO;
import com.mvc.dyvault.common.permission.NotLogin;
import com.mvc.dyvault.common.sdk.dto.SdkLoginDTO;
import com.mvc.dyvault.common.util.MessageConstants;
import com.mvc.dyvault.sdk.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.LoginException;

/**
 * @author qiyichen
 * @create 2018/11/19 19:48
 */
@Api(tags = "user controller")
@RestController
@RequestMapping("user")
public class UserController extends BaseController {

    @ApiOperation("user login/register")
    @PostMapping("login")
    @NotLogin
    public Result<TokenVO> login(@RequestBody SdkLoginDTO sdkLoginDTO) {
        Boolean checkResult = smsSender.checkSmsValidCode(sdkLoginDTO.getCellphone(), sdkLoginDTO.getValidCode());
        Assert.isTrue(checkResult, MessageConstants.getMsg("SMS_ERROR"));
        TokenVO result = userService.login(sdkLoginDTO.getCellphone());
        return new Result<>(result);
    }

    @PostMapping("refresh")
    @ApiOperation("刷新令牌")
    @NotLogin
    Result<String> refresh() throws LoginException {
        String result = userService.refresh();
        return new Result(result);
    }

    @ApiOperation("send sms")
    @GetMapping("sms")
    @NotLogin
    public Result<Boolean> sendSms(@RequestParam String cellphone) {
        smsSender.send(cellphone);
        return new Result<>(true);
    }

}
