package com.project.system2.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.project.system2.domain.entity.SysUserRole;
import com.project.system2.mapper.SysUserRoleMapper;
import com.project.system2.service.SysUserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SysUserRoleServiceImpl implements SysUserRoleService {

    @Autowired
    private SysUserRoleMapper userRoleMapper;

    @Override
    @Transactional
    public boolean saveUserRole(Long userId, Long[] roleIds) {
        // 先删除原有关联
        deleteUserRoleByUserId(userId);
        
        // 添加新的关联
        if (roleIds != null && roleIds.length > 0) {
            List<SysUserRole> list = Arrays.stream(roleIds)
                    .map(roleId -> {
                        SysUserRole ur = new SysUserRole();
                        ur.setUserId(userId);
                        ur.setRoleId(roleId);
                        return ur;
                    }).collect(Collectors.toList());
            
            for (SysUserRole userRole : list) {
                userRoleMapper.insert(userRole);
            }
        }
        
        return true;
    }

    @Override
    @Transactional
    public boolean deleteUserRoleByUserId(Long userId) {
        LambdaQueryWrapper<SysUserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUserRole::getUserId, userId);
        return userRoleMapper.delete(wrapper) >= 0;
    }

    @Override
    @Transactional
    public boolean deleteUserRole(Long[] userIds) {
        LambdaQueryWrapper<SysUserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(SysUserRole::getUserId, Arrays.asList(userIds));
        return userRoleMapper.delete(wrapper) >= 0;
    }

    @Override
    @Transactional
    public boolean deleteAuthUsers(Long roleId, Long[] userIds) {
        LambdaQueryWrapper<SysUserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUserRole::getRoleId, roleId)
                .in(SysUserRole::getUserId, Arrays.asList(userIds));
        return userRoleMapper.delete(wrapper) >= 0;
    }

    @Override
    @Transactional
    public boolean insertAuthUsers(Long roleId, Long[] userIds) {
        List<SysUserRole> list = Arrays.stream(userIds)
                .map(userId -> {
                    SysUserRole ur = new SysUserRole();
                    ur.setUserId(userId);
                    ur.setRoleId(roleId);
                    return ur;
                }).collect(Collectors.toList());
        
        for (SysUserRole userRole : list) {
            userRoleMapper.insert(userRole);
        }
        
        return true;
    }

    @Override
    public boolean checkRoleExistUser(Long roleId) {
        LambdaQueryWrapper<SysUserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUserRole::getRoleId, roleId);
        return userRoleMapper.selectCount(wrapper) > 0;
    }
} 