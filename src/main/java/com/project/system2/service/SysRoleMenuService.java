package com.project.system2.service;

public interface SysRoleMenuService {
    
    /**
     * 保存角色菜单关联关系
     */
    boolean saveRoleMenu(Long roleId, Long[] menuIds);
    
    /**
     * 删除角色菜单关联关系
     */
    boolean deleteRoleMenuByRoleId(Long roleId);
    
    /**
     * 批量删除角色菜单关联关系
     */
    boolean deleteRoleMenu(Long[] roleIds);
    
    /**
     * 检查菜单是否有角色在使用
     */
    boolean checkMenuExistRole(Long menuId);
} 