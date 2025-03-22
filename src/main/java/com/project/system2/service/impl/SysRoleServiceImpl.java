package com.project.system2.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.system2.common.core.utils.StringUtils;
import com.project.system2.domain.entity.SysRole;
import com.project.system2.domain.entity.SysUserRole;
import com.project.system2.mapper.SysRoleMapper;
import com.project.system2.mapper.SysUserRoleMapper;
import com.project.system2.mapper.SysRoleMenuMapper;
import com.project.system2.service.ISysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SysRoleServiceImpl implements ISysRoleService {

    @Autowired
    private SysRoleMapper roleMapper;
    
    @Autowired
    private SysUserRoleMapper userRoleMapper;

    @Autowired
    private SysRoleMenuMapper roleMenuMapper;

    @Override
    public List<SysRole> selectRolesByUserId(Long userId) {
        // 查询用户角色关联
        LambdaQueryWrapper<SysUserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUserRole::getUserId, userId);
        List<SysUserRole> userRoles = userRoleMapper.selectList(wrapper);
        
        // 获取角色ID列表
        List<Long> roleIds = userRoles.stream()
                .map(SysUserRole::getRoleId)
                .collect(Collectors.toList());
        
        if (roleIds.isEmpty()) {
            return new ArrayList<>();
        }
        
        // 查询角色信息
        LambdaQueryWrapper<SysRole> roleWrapper = new LambdaQueryWrapper<>();
        roleWrapper.in(SysRole::getRoleId, roleIds)
                .eq(SysRole::getStatus, "0"); // 只查询正常状态的角色
        return roleMapper.selectList(roleWrapper);
    }

    @Override
    public Set<String> selectRolePermissionByUserId(Long userId) {
        List<SysRole> roles = selectRolesByUserId(userId);
        return roles.stream()
                .map(SysRole::getRoleKey)
                .collect(Collectors.toSet());
    }

    @Override
    public List<SysRole> selectRoleList(SysRole role) {
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();
        // 可以根据条件添加更多查询条件
        if (role != null) {
            wrapper.like(role.getRoleName() != null, SysRole::getRoleName, role.getRoleName())
                    .eq(role != null && role.getStatus() != 0, SysRole::getStatus, role != null ? role.getStatus() : 0);
        }
        return roleMapper.selectList(wrapper);
    }

    @Override
    public SysRole selectRoleById(Long roleId) {
        return roleMapper.selectById(roleId);
    }

    @Override
    @Transactional
    public boolean insertRole(SysRole role) {
        return roleMapper.insert(role) > 0;
    }

    @Override
    @Transactional
    public boolean updateRole(SysRole role) {
        return roleMapper.updateById(role) > 0;
    }

    @Override
    @Transactional
    public boolean deleteRoleById(Long roleId) {
        // 删除角色与用户关联
        LambdaQueryWrapper<SysUserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUserRole::getRoleId, roleId);
        userRoleMapper.delete(wrapper);
        
        // 删除角色
        return roleMapper.deleteById(roleId) > 0;
    }

    @Override
    @Transactional
    public boolean deleteRoleByIds(Long[] roleIds) {
        // 删除角色与用户关联
        LambdaQueryWrapper<SysUserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(SysUserRole::getRoleId, Arrays.asList(roleIds));
        userRoleMapper.delete(wrapper);
        
        // 批量删除角色
        return Arrays.stream(roleIds)
                .map(roleMapper::deleteById)
                .reduce(0, Integer::sum) == roleIds.length;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateRoleMenu(Long roleId, Long[] menuIds) {
        // 删除原有权限
        roleMenuMapper.deleteRoleMenuByRoleId(roleId);
        // 新增权限
        return menuIds.length > 0 && roleMenuMapper.batchRoleMenu(roleId, menuIds) > 0;
    }

    @Override
    @Transactional
    public boolean updateRoleDataScope(SysRole role) {
        return updateRole(role);
    }

    @Override
    @Transactional
    public boolean updateRoleStatus(SysRole role) {
        return updateRole(role);
    }

    @Override
    public IPage<SysRole> selectRolePage(IPage<SysRole> page, SysRole role) {
        // 构建查询条件
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<SysRole>()
            .eq(SysRole::getDelFlag, 0)  // 添加删除标志条件
            .like(StringUtils.isNotBlank(role.getRoleName()), SysRole::getRoleName, role.getRoleName())
            .like(StringUtils.isNotBlank(role.getRoleKey()), SysRole::getRoleKey, role.getRoleKey())
            .eq(role != null && role.getStatus() != null, SysRole::getStatus, role.getStatus())
            .orderByAsc(SysRole::getRoleSort);
        
        // 直接使用 MyBatis-Plus 的 selectPage 方法
        IPage<SysRole> rolePage = roleMapper.selectPage(page, wrapper);
        
        // 为每个角色查询关联的菜单ID
        rolePage.getRecords().forEach(r -> {
            List<Long> menuIds = roleMenuMapper.selectMenuIdsByRoleId(r.getRoleId());
            r.setMenuIds(menuIds);
        });
        
        return rolePage;
    }

    @Override
    public List<SysRole> selectRoleAll() {
        return roleMapper.selectList(null);
    }

    @Override
    public List<Long> selectRoleListByUserId(Long userId) {
        return userRoleMapper.selectList(
            new LambdaQueryWrapper<SysUserRole>()
                .eq(SysUserRole::getUserId, userId)
        ).stream()
        .map(SysUserRole::getRoleId)
        .collect(Collectors.toList());
    }
} 