package com.mvc.dyvault.common.bean.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigInteger;

@Data
public class UserTypeDTO {

    @ApiModelProperty("userId")
    private BigInteger userId;
    @ApiModelProperty("user type(0.normal 1.token seller 2.business)")
    private Integer userType;
    @ApiModelProperty("name, can not be null when type is business")
    private String name;
    @ApiModelProperty("password")
    private String password;

}
