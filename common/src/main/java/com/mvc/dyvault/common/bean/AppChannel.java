package com.mvc.dyvault.common.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigInteger;

/**
 * app_order
 */
@Table(name = "app_channel")
@Data
@ApiModel("channel model")
public class AppChannel implements Serializable {

    @ApiModelProperty("channel info id")
    @Id
    @Column(insertable = false)
    private BigInteger id;
    @ApiModelProperty("channel name")
    private String channelName;
    @ApiModelProperty("contact information")
    private String contact;
    @ApiModelProperty("contact info")
    private String info;
    private Long createdAt;
    private Long updatedAt;

}