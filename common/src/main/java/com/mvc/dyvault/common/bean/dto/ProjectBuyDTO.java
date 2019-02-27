package com.mvc.dyvault.common.bean.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author qiyichen
 * @create 2018/11/8 16:26
 */
@Data
@ApiModel("众筹项目购买参数")
public class ProjectBuyDTO {

    @ApiModelProperty("购买数量 ")
    private BigDecimal value;
    @ApiModelProperty("交易密码")
    private String password;

}
