package com.mvc.dyvault.common.bean;

import com.mvc.dyvault.common.swaggermock.IgnoreUpdate;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author qiyichen
 * @create 2018/12/10 14:19
 */
@Data
@IgnoreUpdate(value = "id")
public class TokenVolume {

    @Id
    @Column(name = "id", insertable = false)
    private BigInteger id;
    private BigInteger tokenId;
    private BigDecimal value;
    private Long createdAt;
    private Integer used;
}
