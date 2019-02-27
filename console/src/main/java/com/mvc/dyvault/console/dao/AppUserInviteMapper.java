package com.mvc.dyvault.console.dao;

import com.mvc.dyvault.common.bean.AppUserInvite;
import com.mvc.dyvault.common.bean.dto.RecommendDTO;
import com.mvc.dyvault.console.common.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigInteger;

public interface AppUserInviteMapper extends MyMapper<AppUserInvite> {
    @Select("SELECT GROUP_CONCAT(invite_user_id) FROM (SELECT invite_user_id FROM app_user_invite WHERE user_id = #{recommendDTO.userId} ${str} ORDER BY invite_user_id desc limit #{recommendDTO.pageSize}) t")
    String getRecommend(@Param("recommendDTO") RecommendDTO userId, @Param("str") String str);
}