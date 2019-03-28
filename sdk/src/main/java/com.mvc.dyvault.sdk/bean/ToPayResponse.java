package com.mvc.dyvault.sdk.bean;

import com.mvc.dyvault.common.sdk.dto.ConfirmOrderDTO;
import lombok.Data;

/**
 * @author qiyichen
 * @create 2019/3/28 13:41
 */
@Data
public class ToPayResponse {

    private Integer status;
    private ConfirmOrderDTO confirmOrderDTO;
    private String message;

}
