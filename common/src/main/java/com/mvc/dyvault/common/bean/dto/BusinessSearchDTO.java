package com.mvc.dyvault.common.bean.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigInteger;

/**
 * @author qiyichen
 * @create 2019/3/22 10:22
 */
@Data
@ApiModel("Business search dto")
public class BusinessSearchDTO {

    @ApiModelProperty("分页大小,默认10条")
    private Integer pageSize = 10;
    @ApiModelProperty("last id")
    private BigInteger id;
    @ApiModelProperty("business order status, 1 started 2 complete 4 cancel 0 all")
    private Integer status;

}
