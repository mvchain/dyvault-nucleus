package com.mvc.dyvault.common.bean.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author qiyichen
 * @create 2019/1/10 16:58
 */
@Data
@ApiModel("重置密码")
public class AppUserResetDTO {

    @ApiModelProperty("邮箱")
    private String email;
    @ApiModelProperty("校验字段")
    private String value;

}
