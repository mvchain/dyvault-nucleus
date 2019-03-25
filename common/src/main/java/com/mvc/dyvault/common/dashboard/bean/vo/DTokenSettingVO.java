package com.mvc.dyvault.common.dashboard.bean.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author qiyichen
 * @create 2018/11/20 18:01
 */
@Data
public class DTokenSettingVO implements Serializable {

    private static final long serialVersionUID = 434868991756370710L;
    @ApiModelProperty("是否可提现")
    private Integer withdraw;

    @ApiModelProperty("是否可充值")
    private Integer recharge;

    @ApiModelProperty("是否课件 ")
    private Integer visible;

    @ApiModelProperty("提现手续费")
    private Float fee;

    @ApiModelProperty("单笔提币下限")
    private BigDecimal withdrawMin;

    @ApiModelProperty("单笔提币上限")
    private BigDecimal withdrawMax;

    @ApiModelProperty("每日提币上限")
    private BigDecimal withdrawDay;

    @ApiModelProperty("令牌id")
    private BigInteger id;

    @ApiModelProperty("令牌名称")
    private String tokenName;

    @ApiModelProperty("是否内部虚拟币种")
    private Integer inner;

    @ApiModelProperty("区块链实际手续费")
    private Float transaferFee;

    @ApiModelProperty("保留金额")
    private Float hold;

    @ApiModelProperty("official sell")
    private Integer officialSell;

}
