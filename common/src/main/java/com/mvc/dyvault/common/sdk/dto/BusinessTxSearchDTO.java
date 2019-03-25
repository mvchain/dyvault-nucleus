package com.mvc.dyvault.common.sdk.dto;

import com.mvc.dyvault.common.bean.dto.PageDTO;
import lombok.Data;

import java.math.BigInteger;

/**
 * @author qiyichen
 * @create 2019/3/25 16:01
 */
@Data
public class BusinessTxSearchDTO extends PageDTO {

    private String orderNumber;
    private BigInteger userId;
    private String cellphone;
    private Integer status;

}
