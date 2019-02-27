package com.mvc.dyvault.console.service;

import com.mvc.dyvault.common.bean.AdminUserPermission;
import com.mvc.dyvault.common.dashboard.bean.dto.PermissionDTO;
import com.mvc.dyvault.console.common.AbstractService;
import com.mvc.dyvault.console.common.BaseService;
import com.mvc.dyvault.console.dao.AdminUserPermissionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Service
public class AdminUserPermissionService extends AbstractService<AdminUserPermission> implements BaseService<AdminUserPermission> {

    @Autowired
    AdminUserPermissionMapper adminUserPermissionMapper;

    public void delete(BigInteger id) {
        AdminUserPermission permission = new AdminUserPermission();
        permission.setUserId(id);
        adminUserPermissionMapper.delete(permission);
    }

    public void updatePermission(BigInteger userId, List<PermissionDTO> permissionList) {
        delete(userId);
        if (CollectionUtils.isEmpty(permissionList)) {
            return;
        }
        for (PermissionDTO dto : permissionList) {
            if (dto.getStatus() == 1) {
                AdminUserPermission permission = new AdminUserPermission();
                permission.setUserId(userId);
                permission.setPermissionId(dto.getPermissionId());
                save(permission);
            }
        }
        String str = adminUserPermissionMapper.findPermissionStr(userId);
        if (null == str) {
            str = "";
        }
        redisTemplate.opsForValue().set("ADMIN_PERMISSON_" + userId, str);
    }
}