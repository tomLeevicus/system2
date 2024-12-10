package com.project.system2.service;

import java.util.Set;
import com.project.system2.domain.entity.SysUser;

/**
 * 权限服务接口
 */
public interface SysPermissionService {
    
    /**
     * 获取菜单权限列表
     * 
     * @param user 用户信息
     * @return 菜单权限列表
     */
    Set<String> getMenuPermission(SysUser user);
    
    /**
     * 获取角色权限列表
     * 
     * @param user 用户信息
     * @return 角色权限列表
     */
    Set<String> getRolePermission(SysUser user);
} 