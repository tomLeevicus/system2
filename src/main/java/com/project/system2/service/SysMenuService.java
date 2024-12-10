package com.project.system2.service;

import java.util.List;
import java.util.Set;
import com.project.system2.domain.entity.SysMenu;

public interface SysMenuService {
    
    /**
     * 根据用户ID查询权限
     */
    Set<String> selectMenuPermsByUserId(Long userId);
    
    /**
     * 根据用户ID查询菜单树信息
     */
    List<SysMenu> selectMenuTreeByUserId(Long userId);
    
    /**
     * 构建前端所需要的菜单
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
     * 查询菜单是否存在角色
     */
    boolean checkMenuExistRole(Long menuId);
    
    /**
     * 新增保存菜单信息
     */
    boolean insertMenu(SysMenu menu);
    
    /**
     * 修改保存菜单信息
     */
    boolean updateMenu(SysMenu menu);
    
    /**
     * 删除菜单管理信息
     */
    boolean deleteMenuById(Long menuId);
    
    /**
     * 校验菜单名称是否唯一
     */
    boolean checkMenuNameUnique(SysMenu menu);

    /**
     * 查询菜单列表
     */
    List<SysMenu> selectMenuList(SysMenu menu);

    /**
     * 根据角色ID查询菜单列表
     */
    List<Long> selectMenuListByRoleId(Long roleId);
} 