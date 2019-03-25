package com.mvc.dyvault.common.sdk.vo;

import lombok.Data;

import java.math.BigInteger;

/**
 * @author qiyichen
 * @create 2019/3/25 14:30
 */
@Data
public class ShopVO {

    private String shopName;
    private Long createdAt;
    private BigInteger id;
    private BigInteger shopId;

}
