package com.mvc.dyvault.common.bean.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @author qiyichen
 * @create 2018/11/6 19:30
 */
@Data
@ApiModel("用户登录参数")
public class UserDTO {

    @ApiModelProperty("用户登录账户")
    @NotNull
    private String username;

    @ApiModelProperty("用户密码")
    @NotNull(message = "{PASSWORD_EMPTY}")
    private String password;

    @ApiModelProperty("验证码.登录时返回的错误次数过多时需要输入")
    private String validCode;

    @ApiModelProperty("图片验证token,密码错误次数过多时必须传入")
    private String imageToken;
}
