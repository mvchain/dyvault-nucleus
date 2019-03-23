package com.mvc.dyvault.common.bean;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigInteger;

/**
 * admin_user
 */
@Table(name = "business_shop")
@Data
public class BusinessShop implements Serializable {

    @Id
    private BigInteger id;
    private BigInteger userId;
    private String shopName;
    private Long createdAt;
    private Long updateAt;

}