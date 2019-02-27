package com.mvc.dyvault.common.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author qiyichen
 * @create 2019/1/12 13:56
 */
@ApiModel("理财购买详情")
@Data
public class FinancialDetailVO extends FinancialSimplePartakeVO {

    @ApiModelProperty("可用余额数量")
    private BigDecimal balance;
    @ApiModelProperty("已购买数量")
    private BigDecimal purchased;
    @ApiModelProperty("产品内容介绍")
    private String content;
    @ApiModelProperty("产品规则介绍")
    private String rule;

}
