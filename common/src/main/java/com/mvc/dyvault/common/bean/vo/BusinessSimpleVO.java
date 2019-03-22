package com.mvc.dyvault.common.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author qiyichen
 * @create 2019/3/22 10:53
 */
@Data
@ApiModel("Business Simple VO")
public class BusinessSimpleVO {

    @ApiModelProperty("business order id")
    private BigInteger id;
    @ApiModelProperty("order number")
    private String orderNumber;
    @ApiModelProperty("token name")
    private String tokenName;
    @ApiModelProperty("token id")
    private String tokenId;
    @ApiModelProperty("orderType 1 in 2 out")
    private Integer orderType;
    @ApiModelProperty("orderStatus 0wait 1payed 2complete 4cancel 9fail")
    private Integer orderStatus;
    @ApiModelProperty("payed")
    private BigDecimal amount;
    @ApiModelProperty("token value")
    private BigDecimal tokenValue;
    @ApiModelProperty("created time")
    private Long createdAt;
    @ApiModelProperty("stop(complete or cancel) time")
    private Long stopAt;
    private BigInteger userId;
}
