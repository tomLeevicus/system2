package com.project.system2.service;

import com.project.system2.domain.entity.SysMenu;
import java.util.List;
import java.util.Set;

public interface SysMenuService {
    
    /**
     * 根据用户ID查询菜单树信息
     */
    List<SysMenu> selectMenuTreeByUserId(Long userId);
    
    /**
     * 根据用户ID查询权限标识
     */
    Set<String> selectMenuPermsByUserId(Long userId);
    
    /**
     * 根据角色ID查询菜单树信息
     */
    List<Long> selectMenuListByRoleId(Long roleId);
    
    /**
     * 构建前端所需要的菜单树
     */
    List<SysMenu> buildMenuTree(List<SysMenu> menus);
    
    /**
     * 根据菜单ID查询信息
     */
    SysMenu selectMenuById(Long menuId);
    
    /**
     * 是否存在菜单子节点
     */
    boolean hasChildByMenuId(Long menuId);
    
    /**
     * 新增菜单
     */
    boolean insertMenu(SysMenu menu);
    
    /**
     * 修改菜单
     */
    boolean updateMenu(SysMenu menu);
    
    /**
     * 删除菜单
     */
    boolean deleteMenuById(Long menuId);
    
    /**
     * 查询菜单列表
     */
    List<SysMenu> selectMenuList(SysMenu menu);
    
    /**
     * 检查菜单是否存在角色
     */
    boolean checkMenuExistRole(Long menuId);
} 