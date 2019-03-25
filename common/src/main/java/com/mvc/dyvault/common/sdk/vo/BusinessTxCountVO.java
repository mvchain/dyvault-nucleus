package com.mvc.dyvault.common.sdk.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author qiyichen
 * @create 2019/3/25 16:09
 */
@Data
public class BusinessTxCountVO {

    private Long createdAt;
    private Integer txCount;
    private BigDecimal cny;
    private BigDecimal tokenValue;
    private String tokenName;

}
