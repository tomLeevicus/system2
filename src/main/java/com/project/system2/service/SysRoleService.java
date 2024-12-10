package com.project.system2.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.system2.domain.entity.SysRole;
import java.util.List;
import java.util.Set;

public interface SysRoleService {
    
    /**
     * 根据用户ID查询角色列表
     */
    List<SysRole> selectRolesByUserId(Long userId);
    
    /**
     * 根据用户ID查询角色权限
     */
    Set<String> selectRolePermissionByUserId(Long userId);
    
    /**
     * 查询所有角色
     */
    List<SysRole> selectRoleAll();
    
    /**
     * 根据用户ID获取角色选择框列表
     */
    List<Long> selectRoleListByUserId(Long userId);
    
    /**
     * 查询角色列表
     */
    List<SysRole> selectRoleList(SysRole role);
    
    /**
     * 根据角色ID查询角色
     */
    SysRole selectRoleById(Long roleId);
    
    /**
     * 新增角色
     */
    boolean insertRole(SysRole role);
    
    /**
     * 修改角色
     */
    boolean updateRole(SysRole role);
    
    /**
     * 删除角色
     */
    boolean deleteRoleById(Long roleId);
    
    /**
     * 批量删除角色
     */
    boolean deleteRoleByIds(Long[] roleIds);
    
    /**
     * 修改角色菜单
     */
    boolean updateRoleMenu(Long roleId, Long[] menuIds);
    
    /**
     * 修改角色数据权限
     */
    boolean updateRoleDataScope(SysRole role);
    
    /**
     * 修改角色状态
     */
    boolean updateRoleStatus(SysRole role);
    
    /**
     * 分页查询角色列表
     */
    Page<SysRole> selectRolePage(Page<SysRole> page, SysRole role);
} 