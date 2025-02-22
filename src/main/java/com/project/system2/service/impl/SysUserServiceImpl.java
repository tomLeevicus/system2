package com.project.system2.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.project.system2.domain.entity.SysUser;
import com.project.system2.mapper.SysUserMapper;
import com.project.system2.service.ISysUserRoleService;
import com.project.system2.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    @Autowired
    private SysUserMapper userMapper;

    @Autowired
    private ISysUserRoleService userRoleService;

    @Override
    public SysUser getUserByUsername(String username) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getUsername, username);
        return userMapper.selectOne(wrapper);
    }

    @Override
    @Transactional
    public boolean addUser(SysUser user) {
        return userMapper.insert(user) > 0;
    }

    @Override
    @Transactional
    public boolean updateUser(SysUser user) {
        // 更新用户信息
        boolean isUpdated = userMapper.updateById(user) > 0;

        // 更新用户角色
        if (user.getRoleIds() != null) {
            userRoleService.saveUserRole(user.getId(), user.getRoleIds());
        }

        // 更新用户部门
        if (user.getDept() != null) {
            userRoleService.updateUserDept(user.getId(), user.getDept().getDeptId());
        }

        return isUpdated;
    }

    @Override
    @Transactional
    public boolean deleteUser(Long userId) {
        return userMapper.deleteById(userId) > 0;
    }

    @Override
    public SysUser getUserById(Long userId) {
        return userMapper.selectById(userId);
    }

    @Override
    @Transactional
    public boolean updateUserRole(Long userId, Long[] roleIds) {
        return userRoleService.saveUserRole(userId, roleIds);
    }

    @Override
    @Transactional
    public boolean deleteUserByIds(Long[] userIds) {
        // 删除用户与角色关联
        userRoleService.deleteUserRole(userIds);
        // 批量删除用户
        return Arrays.stream(userIds)
                .map(this::deleteUser)
                .reduce(true, (a, b) -> a && b);
    }

    @Override
    @Transactional
    public boolean resetPwd(SysUser user) {
        return updateUser(user);
    }

    @Override
    public Page<SysUser> selectUserPage(Page<SysUser> page, SysUser user) {
        return userMapper.selectUserWithDeptRolePage(page, user);
    }

    @Override
    public List<SysUser> getApprovalUsers() {
        return userMapper.selectApprovalUsers();
    }
} 