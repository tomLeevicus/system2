package com.project.system2.service.impl;

import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.util.Arrays;

import com.project.system2.common.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.system2.domain.entity.SysUser;
import com.project.system2.service.SysPermissionService;
import com.project.system2.service.SysRoleService;
import com.project.system2.service.SysMenuService;
import com.project.system2.domain.entity.SysRole;
import com.project.system2.domain.entity.SysMenu;
import com.project.system2.mapper.SysRoleMapper;
import com.project.system2.mapper.SysMenuMapper;

@Service
public class SysPermissionServiceImpl implements SysPermissionService {

    @Autowired
    private SysRoleMapper roleMapper;
    
    @Autowired
    private SysMenuMapper menuMapper;


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
        Set<String> perms = new HashSet<>();
        // 通过用户ID查询权限
        List<String> permList = menuMapper.selectMenuPermsByUserId(userId);
        for (String perm : permList) {
            if (StringUtils.isNotEmpty(perm)) {
                perms.add(perm);
            }
        }
        return perms;
    }
} 