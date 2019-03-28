package com.mvc.dyvault.console.dao;

import com.mvc.dyvault.common.bean.BusinessTransaction;
import com.mvc.dyvault.common.sdk.vo.BusinessTxCountVO;
import com.mvc.dyvault.console.common.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigInteger;
import java.util.List;

public interface BusinessTransactionMapper extends MyMapper<BusinessTransaction> {

    @Select("SELECT ${startedAt} createdAt, count(1) txCount, sum(amount) cny, sum(token_value) tokenValue, 'USDT' tokenName " +
            "FROM business_transaction WHERE `status` = 2 AND remit_user_id = #{id} AND created_at >= #{startedAt} AND created_at <= #{stopAt}")
    List<BusinessTxCountVO> getBusinessCount(@Param("id") BigInteger id, @Param("startedAt") Long startedAt, @Param("stopAt") Long stopAt);


    @Select("SELECT * FROM business_transaction WHERE status = 1 AND (created_at + limit_time) < #{currentTimeMillis} LIMIT 1")
    BusinessTransaction getOverTx(@Param("currentTimeMillis") Long currentTimeMillis);

}