package com.mvc.dyvault.common.bean.vo;

import lombok.Data;

/**
 * @author qiyichen
 * @create 2019/3/25 14:10
 */
@Data
public class SupplierVO {

    private Float priceDifferences;
    private Integer bankSwitch;
    private Integer hasBank;
    private Integer aliPaySwitch;
    private Integer hasAliPay;
    private Integer weChatSwitch;
    private Integer hasWeChat;
}
