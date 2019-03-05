package com.mvc.dyvault.common.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import javax.persistence.Column;
import lombok.Data;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;
import lombok.Generated;

/**
 * app_user_balance
 */
@Table(name = "app_user_balance")
@Data
public class AppUserBalance implements Serializable {
    /**
     * 用户id
     */
    @Id
    @Column(name = "user_id", updatable = false)
    private BigInteger userId;

    /**
     * 令牌id
     */
    @Column(name = "token_id")
    private BigInteger tokenId;

    /** "1,2,3"
     * 余额
     */
    @Column(name = "balance")
    private BigDecimal balance;

    /**
     * 是否在资产列表中展示
     */
    private Integer visible;

    /**
     * app_user_balance
     */
    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     * @return user_id 用户id
     */
    public BigInteger getUserId() {
        return userId;
    }

    /**
     * 用户id
     * @param userId 用户id
     */
    public void setUserId(BigInteger userId) {
        this.userId = userId;
    }

    /**
     * 令牌id
     * @return token_id 令牌id
     */
    public BigInteger getTokenId() {
        return tokenId;
    }

    /**
     * 令牌id
     * @param tokenId 令牌id
     */
    public void setTokenId(BigInteger tokenId) {
        this.tokenId = tokenId;
    }

    /**
     * 余额
     * @return balance 余额
     */
    public BigDecimal getBalance() {
        return balance;
    }

    /**
     * 余额
     * @param balance 余额
     */
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}