package com.mvc.dyvault.common.bean;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigInteger;

/**
 * admin_user
 */
@Table(name = "business_shop_payment")
@Data
public class BusinessShopPayment implements Serializable {

    @Id
    private BigInteger id;
    private BigInteger userId;
    private Integer paymentType;
    private String accountName;
    private String paymentAccount;
    private BigInteger shopId;
    private String bank;
    private String branch;
    private Integer status;
    private String paymentImage;

}