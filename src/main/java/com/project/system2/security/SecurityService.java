package com.project.system2.security;

import com.project.system2.domain.entity.SysRole;
import com.project.system2.service.TokenService;
import com.project.system2.common.core.domain.model.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service("ss")
public class SecurityService {
    
    @Autowired
    private TokenService tokenService;
    
    /**
     * 判断是否具有某个权限
     */
    public boolean hasPermi(String permission) {
        LoginUser loginUser = tokenService.getLoginUser();
        if (loginUser == null) {
            return false;
        }
        
        // 如果是管理员，直接返回true
        if (isAdmin(loginUser)) {
            return true;
        }
        
        // 非管理员校验权限
        if (CollectionUtils.isEmpty(loginUser.getPermissions())) {
            return false;
        }
        return loginUser.getPermissions().contains(permission);
    }
    
    /**
     * 判断是否具有某个角色
     */
    public boolean hasRole(String role) {
        LoginUser loginUser = tokenService.getLoginUser();
        if (loginUser == null) {
            return false;
        }

        // 如果是管理员，直接返回true
        if (isAdmin(loginUser)) {
            return true;
        }
        
        // 非管理员校验角色
        if (CollectionUtils.isEmpty(loginUser.getUser().getRoles())) {
            return false;
        }
        return loginUser.getUser().getRoles().stream()
            .map(SysRole::getRoleKey)
            .anyMatch(roleKey -> roleKey.equals(role));
    }
    
    /**
     * 判断是否为管理员
     */
    private boolean isAdmin(LoginUser loginUser) {
        return loginUser != null && loginUser.getUser() != null 
            && (loginUser.getUser().isAdmin() 
                || loginUser.getUser().getRoles().stream()
                    .anyMatch(role -> SysRole.ADMIN_ROLE_KEY.equals(role.getRoleKey())));
    }
} 