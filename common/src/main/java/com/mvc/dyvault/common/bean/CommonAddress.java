package com.mvc.dyvault.common.bean;

import com.mvc.dyvault.common.swaggermock.IgnoreUpdate;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author qiyichen
 * @create 2018/11/14 16:29
 */
@Table(name = "common_address")
@Data
@IgnoreUpdate(value = "id")
public class CommonAddress implements Serializable {
    private static final long serialVersionUID = -7228504157787988027L;

    @Id
    @Column(name = "id", updatable = false)
    private BigInteger id;
    private String tokenType;
    private String address;
    private Integer used;
    private BigDecimal balance;
    private BigInteger userId;
    private String addressType;
    private Integer approve;

}
