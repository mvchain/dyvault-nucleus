package com.mvc.dyvault.common.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author qiyichen
 * @create 2019/3/5 18:51
 */
@Data
@ApiModel("email token vo")
@NoArgsConstructor
@AllArgsConstructor
public class EmailTokenVO {

    @ApiModelProperty("salt")
    private String salt;

    @ApiModelProperty("token")
    private String token;
}
