package com.mvc.dyvault.simulation.bean;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author qiyichen
 * @create 2019/3/29 15:03
 */
@Data
public class Order {

    private String orderNumber;
    private BigDecimal cny;
    private Long createdAt;
    private Integer status;

}
