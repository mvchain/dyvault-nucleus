package com.mvc.dyvault.common.sdk.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author qiyichen
 * @create 2019/3/23 16:25
 */
@Data
public class ConfirmOrderDTO {

    private BigInteger shopId;
    private String orderNumber;
    private String tokenName;
    private String tokenId;
    private BigDecimal amount;
    private BigDecimal tokenValue;
    private Long limitTime;
    private BigDecimal price;
    private Integer payType;
    private String payAccount;
    private String buyUsername;
    private String sellUsername;
    private String sign;

}
