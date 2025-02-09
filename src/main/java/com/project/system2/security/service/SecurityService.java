package com.project.system2.security.service;

import com.project.system2.common.core.domain.model.LoginUser;
import com.project.system2.common.core.utils.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Set;

@Service("ss")
public class SecurityService {
    
    /**
     * 验证用户是否具备某权限
     * 
     * @param permission 权限字符串
     * @return 用户是否具备某权限
     */
    public boolean hasPermi(String permission) {
        // 如果未传入权限标识，则返回false
        if (permission == null || permission.isEmpty()) {
            return false;
        }
        
        // 获取当前登录用户信息
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (loginUser == null) {
            return false;
        }

        // 如果是超级管理员，则直接返回true
        if (loginUser.getUser().isAdmin()) {
            return true;
        }
        
        // 获取用户权限列表
        Set<String> permissions = SecurityUtils.getPermissions();
        if (CollectionUtils.isEmpty(permissions)) {
            return false;
        }
        
        return permissions.contains(permission);
    }
} 