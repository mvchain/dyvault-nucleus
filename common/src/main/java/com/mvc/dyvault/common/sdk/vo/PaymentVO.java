package com.mvc.dyvault.common.sdk.vo;

import lombok.Data;

import java.math.BigInteger;

/**
 * @author qiyichen
 * @create 2019/3/23 15:54
 */
@Data
public class PaymentVO {

    private BigInteger shopId;
    private String paymentType;
    private String accountName;
    private String paymentAccount;
    private String bank;
    private String branch;

}
