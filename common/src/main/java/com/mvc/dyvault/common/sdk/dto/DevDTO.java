package com.mvc.dyvault.common.sdk.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author qiyichen
 * @create 2019/3/25 15:58
 */
@Data
public class DevDTO {

    @ApiModelProperty("callback url")
    private String callbackUrl;
}
