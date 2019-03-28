package com.mvc.dyvault.common.sdk.dto;

import lombok.Data;

/**
 * @author qiyichen
 * @create 2019/3/28 16:48
 */
@Data
public class CallbackDTO {

    private Integer status;
    private String message;
    private String orderNumber;
    private Integer orderStatus;

}
