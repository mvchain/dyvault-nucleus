package com.mvc.dyvault.console.dao;

import com.mvc.dyvault.common.bean.AdminUserPermission;
import com.mvc.dyvault.console.common.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigInteger;

public interface AdminUserPermissionMapper extends MyMapper<AdminUserPermission> {

    @Select("SELECT GROUP_CONCAT(permission_id) FROM admin_user_permission where user_id = #{userId}")
    String findPermissionStr(@Param("userId") BigInteger userId);

}