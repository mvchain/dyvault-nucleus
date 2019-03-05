package com.mvc.dyvault.common.bean;

import com.mvc.dyvault.common.swaggermock.IgnoreUpdate;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigInteger;

/**
 * @author qiyichen
 * @create 2018/12/3 14:06
 */
@Table(name = "block_sign")
@Data
@IgnoreUpdate(value = "id")
public class BlockSign {
    @Id
    @Column(name = "id", updatable = false)
    private BigInteger id;
    private Integer oprType;
    private String orderId;
    private String sign;
    private String result;
    private Integer status;
    private String hash;
    private Long startedAt;
    private String tokenType;
    private String contractAddress;
    private String fromAddress;
    private String toAddress;

}
