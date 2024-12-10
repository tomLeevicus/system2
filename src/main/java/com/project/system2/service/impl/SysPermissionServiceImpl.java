package com.project.system2.service.impl;

import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.system2.domain.entity.SysUser;
import com.project.system2.service.SysPermissionService;
import com.project.system2.service.SysRoleService;
import com.project.system2.service.SysMenuService;

@Service
public class SysPermissionServiceImpl implements SysPermissionService {

    @Autowired
    private SysRoleService roleService;
    
    @Autowired
    private SysMenuService menuService;

    @Override
    public Set<String> getMenuPermission(SysUser user) {
        Set<String> perms = new HashSet<>();
        // 管理员拥有所有权限
        if (user.isAdmin()) {
            perms.add("*:*:*");
        } else {
            perms.addAll(menuService.selectMenuPermsByUserId(user.getUserId()));
        }
        return perms;
    }

    @Override
    public Set<String> getRolePermission(SysUser user) {
        Set<String> roles = new HashSet<>();
        // 管理员拥有所有角色权限
        if (user.isAdmin()) {
            roles.add("admin");
        } else {
            roles.addAll(roleService.selectRolePermissionByUserId(user.getUserId()));
        }
        return roles;
    }
} 