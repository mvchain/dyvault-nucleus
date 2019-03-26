package com.mvc.dyvault.common.sdk.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author qiyichen
 * @create 2019/3/25 16:00
 */
@Data
public class BusinessOrderVO {

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
    private Integer remitUserId;
    private Integer remitShopId;
    private String buyUsername;
    private String sellUsername;
    private Long updatedAt;
    private Long payAt;
    private String selfOrderNumber;
    private String cellphone;

    public String getStatusStr() {
        switch (status) {
            case 1:
                return "充值中";
            case 2:
                return "已完成";
            case 4:
                return "已取消";
            case 9:
                return "失败";
        }
        return "失败";
    }

}
