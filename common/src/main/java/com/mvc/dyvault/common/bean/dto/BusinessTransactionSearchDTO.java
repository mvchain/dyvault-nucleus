package com.mvc.dyvault.common.bean.dto;

import lombok.Data;

/**
 * @author qiyichen
 * @create 2019/3/25 14:18
 */
@Data
public class BusinessTransactionSearchDTO extends PageDTO {

    private String orderNumber;
    private String payAccount;

}
