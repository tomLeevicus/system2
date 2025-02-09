package com.project.system2.service;

import java.util.List;
import java.util.Set;
import com.project.system2.domain.entity.SysMenu;

public interface ISysMenuService {
    
    /**
     * 根据用户ID查询权限
     */
    Set<String> selectMenuPermsByUserId(Long userId);
    
    /**
     * 根据用户ID查询菜单树信息
     *
     * @param userId 用户ID
     * @return 菜单列表
     */
    List<SysMenu> selectMenuTreeByUserId(Long userId);
    
    /**
     * 构建前端所需要的菜单树
     *
     * @param menus 菜单列表
     * @return 菜单列表
     */
    List<SysMenu> buildMenuTree(List<SysMenu> menus);
    
    /**
     * 根据菜单ID查询信息
     *
     * @param menuId 菜单ID
     * @return 菜单信息
     */
    SysMenu selectMenuById(Long menuId);
    
    /**
     * 是否存在菜单子节点
     *
     * @param menuId 菜单ID
     * @return 结果 true 存在 false 不存在
     */
    boolean hasChildByMenuId(Long menuId);
    
    /**
     * 查询菜单是否存在角色
     *
     * @param menuId 菜单ID
     * @return 结果 true 存在 false 不存在
     */
    boolean checkMenuExistRole(Long menuId);
    
    /**
     * 新增菜单
     *
     * @param menu 菜单信息
     * @return 结果
     */
    boolean insertMenu(SysMenu menu);
    
    /**
     * 修改菜单
     *
     * @param menu 菜单信息
     * @return 结果
     */
    boolean updateMenu(SysMenu menu);
    
    /**
     * 删除菜单
     *
     * @param menuId 菜单ID
     * @return 结果
     */
    boolean deleteMenuById(Long menuId);
    
    /**
     * 校验菜单名称是否唯一
     */
    boolean checkMenuNameUnique(SysMenu menu);

    /**
     * 查询菜单列表
     *
     * @param menu 菜单信息
     * @return 菜单列表
     */
    List<SysMenu> selectMenuList(SysMenu menu);

    /**
     * 根据角色ID查询菜单树信息
     *
     * @param roleId 角色ID
     * @return 选中菜单列表
     */
    List<Long> selectMenuListByRoleId(Long roleId);
} 