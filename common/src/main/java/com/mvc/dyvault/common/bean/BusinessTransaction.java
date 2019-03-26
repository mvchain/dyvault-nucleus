package com.mvc.dyvault.common.bean;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author qiyichen
 * @create 2019/3/22 15:11
 */
@Data
public class BusinessTransaction {

    @Id
    private BigInteger id;
    private String orderNumber;
    private String tokenName;
    private String tokenId;
    private Integer orderType;
    private Integer orderStatus;
    private Integer status;
    private BigDecimal amount;
    private BigDecimal tokenValue;
    private Long createdAt;
    private Long stopAt;
    private Long limitTime;
    private BigInteger buyUserId;
    private BigInteger sellUserId;
    private BigDecimal price;
    private Integer payType;
    private String payAccount;
    private BigInteger remitUserId;
    private BigInteger remitShopId;
    private String buyUsername;
    private String sellUsername;
    @Column(name = "user_id", updatable = false)
    private BigInteger userId;
    private Long updatedAt;
    private Long payAt;
    private Integer autoSend;
    private String selfOrderNumber;

    @Transient
    private String cellphone;

}
