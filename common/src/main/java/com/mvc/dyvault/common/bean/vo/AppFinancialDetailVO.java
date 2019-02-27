package com.mvc.dyvault.common.bean.vo;

import com.mvc.dyvault.common.bean.AppFinancial;
import com.mvc.dyvault.common.bean.AppFinancialContent;
import com.mvc.dyvault.common.bean.AppFinancialDetail;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author qiyichen
 * @create 2019/1/15 17:14
 */
@Data
@ApiModel("理财产品后台详情")
public class AppFinancialDetailVO extends AppFinancialVO {

    @ApiModelProperty("产品规则")
    private AppFinancialContent content;
    @ApiModelProperty("产品提成详情")
    private List<AppFinancialDetail> details;

}
