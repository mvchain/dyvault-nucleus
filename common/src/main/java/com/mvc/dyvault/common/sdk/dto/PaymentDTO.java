package com.mvc.dyvault.common.sdk.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author qiyichen
 * @create 2019/3/25 13:59
 */
@Data
public class PaymentDTO {

    @ApiModelProperty("1 credit card; 2 Alipay 3 WeChat")
    private Integer paymentType;
    @ApiModelProperty("name")
    private String accountName;
    @ApiModelProperty("card number or payment account")
    private String paymentAccount;
    @ApiModelProperty("bank")
    private String bank;
    @ApiModelProperty("bank branch")
    private String branch;
    @ApiModelProperty("QR Code image url")
    private String paymentImage;

}
