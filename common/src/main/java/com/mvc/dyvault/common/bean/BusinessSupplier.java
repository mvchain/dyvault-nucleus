package com.mvc.dyvault.common.bean;

import lombok.Data;

import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigInteger;

/**
 * admin_user
 */
@Data
public class BusinessSupplier implements Serializable {

    @Id
    private BigInteger id;
    private BigInteger userId;
    private Float priceDifferences;
    private Integer bankSwitch;
    private Integer hasBank;
    private Integer aliPaySwitch;
    private Integer hasAliPay;
    private Integer weChatSwitch;
    private Integer hasWeChat;

}