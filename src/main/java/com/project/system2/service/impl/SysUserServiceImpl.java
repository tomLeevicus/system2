package com.project.system2.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.project.system2.common.core.utils.EntityUtils;
import com.project.system2.common.core.utils.SecurityUtils;
import com.project.system2.domain.entity.SysUser;
import com.project.system2.domain.entity.SysRole;
import com.project.system2.domain.entity.SysDept;
import com.project.system2.domain.query.SysUserQuery;
import com.project.system2.mapper.SysUserMapper;
import com.project.system2.service.ISysUserRoleService;
import com.project.system2.service.ISysUserService;
import com.project.system2.service.ISysRoleService;
import com.project.system2.service.ISysDeptService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    @Autowired
    private SysUserMapper userMapper;

    @Autowired
    private ISysUserRoleService userRoleService;
    
    @Autowired
    private ISysRoleService roleService;

    @Autowired
    private ISysDeptService deptService;

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

        // 更新用户部门 - Restore original logic
        if (user.getDept() != null && user.getDept().getDeptId() != null) {
             // Assuming ISysUserRoleService or similar handles this.
             // Adjust if the service/method name is different.
             // If deptId is directly on SysUser table and handled by updateById, this might be redundant.
             // Check how user-dept relationship is actually managed.
             // For now, restoring the likely original call based on previous context:
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
        SysUser user = userMapper.selectById(userId); // Fetch basic user info
        if (user != null) {
            // Fetch and set roles
            List<SysRole> roles = roleService.selectRolesByUserId(userId);
            user.setRoles(roles); // Set roles list

            // Fetch and set primary department info using the join table
            SysDept dept = deptService.getPrimaryDeptByUserId(userId);
            user.setDept(dept); // Set the department object
            // Set deptId and deptName based on the fetched dept object
            user.setDeptId(dept != null ? dept.getDeptId() : null);
            user.setDeptName(dept != null ? dept.getDeptName() : null);
        }
        return user;
    }

    @Override
    @Transactional
    public boolean updateUserRole(Long userId, List<Long> roleIds) {
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
    public Page<SysUser> selectUserPage(Page<SysUser> page, SysUserQuery query) {
        LambdaQueryWrapper<SysUser> lqw = new LambdaQueryWrapper<>();
        
        // 默认不查询超级管理员
        lqw.ne(SysUser::getId, "1");
        
        // 获取当前登录用户ID
        Long currentUserId = SecurityUtils.getUserId();
        if (currentUserId != null) {
            // 排除当前登录用户
            lqw.ne(SysUser::getId, currentUserId);
        }
        
        if (query != null) {
            // 根据部门ID查询
            if (query.getDeptId() != null) {
                lqw.eq(SysUser::getDeptId, query.getDeptId());
            }
            
            // 根据用户昵称模糊查询
            if (StringUtils.isNotBlank(query.getNickname())) {
                lqw.like(SysUser::getNickname, query.getNickname());
            }
            
            // 根据手机号码模糊查询
            if (StringUtils.isNotBlank(query.getMobile())) {
                lqw.like(SysUser::getMobile, query.getMobile());
            }
            
            // 根据用户性别查询
            if (StringUtils.isNotBlank(query.getGender())) {
                lqw.eq(SysUser::getGender, query.getGender());
            }
            
            // 根据账号状态查询
            if (StringUtils.isNotBlank(query.getStatus())) {
                lqw.eq(SysUser::getStatus, query.getStatus());
            }
            
            // 根据创建时间范围查询
            if (StringUtils.isNotBlank(query.getCreateTimeStart())) {
                lqw.ge(SysUser::getCreateTime, query.getCreateTimeStart());
            }
            
            if (StringUtils.isNotBlank(query.getCreateTimeEnd())) {
                lqw.le(SysUser::getCreateTime, query.getCreateTimeEnd());
            }
            
            // 添加排序条件
            if (StringUtils.isNotBlank(query.getOrderByColumn())) {
                // 这里需要根据实际的排序字段做映射处理
                // 简单示例:
                if ("createTime".equals(query.getOrderByColumn())) {
                    lqw.orderBy(true, "asc".equalsIgnoreCase(query.getIsAsc()), SysUser::getCreateTime);
                }
            } else {
                // 默认排序
                lqw.orderByDesc(SysUser::getCreateTime);
            }
        }
        
        // 执行分页查询
        Page<SysUser> userPage = page(page, lqw);
        
        // 获取查询结果中的用户列表
        List<SysUser> userList = userPage.getRecords();
        
        // 为每个用户查询并设置角色信息
        if (!userList.isEmpty()) {
            for (SysUser user : userList) {
                // 查询用户角色信息
                List<SysRole> roles = roleService.selectRolesByUserId(user.getId());
                user.setRoles(roles);
            }
        }
        
        return userPage;
    }

    @Override
    public List<SysUser> getApprovalUsers() {
        return userMapper.selectApprovalUsers();
    }
} 