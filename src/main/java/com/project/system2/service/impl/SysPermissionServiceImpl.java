package com.project.system2.service.impl;

import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.util.Arrays;

import com.project.system2.common.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.project.system2.domain.entity.SysUser;
import com.project.system2.service.ISysPermissionService;
import com.project.system2.domain.entity.SysRole;
import com.project.system2.mapper.SysRoleMapper;
import com.project.system2.mapper.SysMenuMapper;

@Service
public class SysPermissionServiceImpl implements ISysPermissionService {

    @Autowired
    private SysRoleMapper roleMapper;
    
    @Autowired
    private SysMenuMapper menuMapper;

    /**
     * 获取角色数据权限
     */
    @Override
    public Set<String> getRolePermission(SysUser user) {
        Set<String> roles = new HashSet<>();
        // 管理员拥有所有权限
        if (user.isAdmin()) {
            roles.add("admin");
        } else {
            roles.addAll(user.getRoleKeys());
        }
        return roles;
    }

    /**
     * 获取菜单数据权限
     */
    @Override
    public Set<String> getMenuPermission(SysUser user) {
        Set<String> perms = new HashSet<>();
        // 管理员拥有所有权限
        if (user.isAdmin()) {
            perms.add("*:*:*");
        } else {
            // 查询用户具有的权限
            Set<String> menuPerms = menuMapper.selectMenuPermsByUserId(user.getId());
            if (!CollectionUtils.isEmpty(menuPerms)) {
                perms.addAll(menuPerms);
            } else {
                // 如果没有任何权限，添加一个基础权限
                perms.add("common:view");
            }
        }
        return perms;
    }

    @Override
    public Set<String> selectRolePermissionByUserId(Long userId) {
        Set<String> roles = new HashSet<>();
        // 通过用户ID查询角色
        List<SysRole> roleList = roleMapper.selectRolesByUserId(userId);
        for (SysRole role : roleList) {
            if (StringUtils.isNotNull(role)) {
                roles.addAll(Arrays.asList(role.getRoleKey().trim().split(",")));
            }
        }
        return roles;
    }

    @Override
    public Set<String> selectMenuPermsByRoleId(Long roleId) {
        Set<String> perms = new HashSet<>();
        // 通过角色ID查询权限
        List<String> permList = menuMapper.selectMenuPermsByRoleId(roleId);
        for (String perm : permList) {
            if (StringUtils.isNotEmpty(perm)) {
                perms.add(perm);
            }
        }
        return perms;
    }

    @Override
    public Set<String> selectMenuPermsByUserId(Long userId) {
        // 通过用户ID查询权限
        Set<String> perms = menuMapper.selectMenuPermsByUserId(userId);
        return perms;
    }
} 