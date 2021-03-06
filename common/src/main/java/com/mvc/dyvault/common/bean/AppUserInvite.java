package com.mvc.dyvault.common.bean;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.math.BigInteger;

/**
 * @author qiyichen
 * @create 2019/1/9 15:50
 */
@Data
public class AppUserInvite {

    @Id
    @Column(name = "user_id", updatable = false)
    private BigInteger userId;
    private BigInteger inviteUserId;
    private Long createdAt;
    private Long updatedAt;

}
