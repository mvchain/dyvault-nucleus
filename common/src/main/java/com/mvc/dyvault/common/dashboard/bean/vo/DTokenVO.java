package com.mvc.dyvault.common.dashboard.bean.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author qiyichen
 * @create 2018/11/20 16:19
 */
@Data
public class DTokenVO implements Serializable {
    private static final long serialVersionUID = -8746068592195617206L;

    @ApiModelProperty("令牌名称")
    private String tokenName;
    @ApiModelProperty("令牌id")
    private BigInteger tokenId;
    @ApiModelProperty("是否展示")
    private Integer visible;
    @ApiModelProperty("是否可提现")
    private Integer withdraw;
    @ApiModelProperty("是否可充值")
    private Integer recharge;
    @ApiModelProperty("最小提币")
    private BigDecimal withdrawMin;
    @ApiModelProperty("最大提币")
    private BigDecimal withdrawMax;
    @ApiModelProperty("单日提币上限")
    private BigDecimal withdrawDay;
    @ApiModelProperty("动态手续费")
    private Float fee;
    @ApiModelProperty("区块链实际手续费")
    private Float transaferFee;
    @ApiModelProperty("保留金额")
    private Float hold;
    @ApiModelProperty("合约地址")
    private String contractAddress;

}
