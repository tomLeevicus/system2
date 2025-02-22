package com.project.system2.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.project.system2.common.core.utils.StringUtils;
import com.project.system2.domain.entity.SysMenu;
import com.project.system2.mapper.SysMenuMapper;
import com.project.system2.service.ISysMenuService;

@Service
public class SysMenuServiceImpl implements ISysMenuService {

    @Autowired
    private SysMenuMapper menuMapper;

    @Override
    public Set<String> selectMenuPermsByUserId(Long userId) {
        Set<String> permsSet = menuMapper.selectMenuPermsByUserId(userId);
        return permsSet;
    }

    @Override
    public List<SysMenu> selectMenuTreeByUserId(Long userId) {
        List<SysMenu> menus = null;
        if (userId != null && userId == 1L) {
            // 管理员显示所有菜单信息
            menus = menuMapper.selectMenuTreeAll();
        } else {
            menus = menuMapper.selectMenuTreeByUserId(userId);
        }
        return buildMenuTree(menus);
    }

    @Override
    public List<SysMenu> buildMenuTree(List<SysMenu> menus) {
        List<SysMenu> returnList = new ArrayList<>();
        List<Long> tempList = menus.stream().map(SysMenu::getMenuId).collect(Collectors.toList());
        
        for (SysMenu menu : menus) {
            // 如果是顶级节点，遍历该父节点的所有子节点
            if (!tempList.contains(menu.getParentId())) {
                recursionFn(menus, menu);
                returnList.add(menu);
            }
        }
        
        // 如果构建的树为空，返回原始菜单列表
        if (returnList.isEmpty()) {
            return menus;
        }
        
        return returnList;
    }

    @Override
    public SysMenu selectMenuById(Long menuId) {
        return menuMapper.selectById(menuId);
    }

    @Override
    public boolean hasChildByMenuId(Long menuId) {
        LambdaQueryWrapper<SysMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysMenu::getParentId, menuId);
        return menuMapper.selectCount(wrapper) > 0;
    }

    @Override
    public boolean checkMenuExistRole(Long menuId) {
        return menuMapper.checkMenuExistRole(menuId) > 0;
    }

    @Override
    @Transactional
    public boolean insertMenu(SysMenu menu) {
        return menuMapper.insert(menu) > 0;
    }

    @Override
    @Transactional
    public boolean updateMenu(SysMenu menu) {
        return menuMapper.updateById(menu) > 0;
    }

    @Override
    @Transactional
    public boolean deleteMenuById(Long menuId) {
        return menuMapper.deleteById(menuId) > 0;
    }

    @Override
    public boolean checkMenuNameUnique(SysMenu menu) {
        Long menuId = menu.getMenuId() == null ? -1L : menu.getMenuId();
        LambdaQueryWrapper<SysMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysMenu::getMenuName, menu.getMenuName())
               .eq(SysMenu::getParentId, menu.getParentId())
               .ne(SysMenu::getMenuId, menuId);
        return menuMapper.selectCount(wrapper) == 0;
    }

    @Override
    public List<SysMenu> selectMenuList(SysMenu menu) {
        // 1. 查询所有符合条件的菜单（包含子菜单）
        LambdaQueryWrapper<SysMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotEmpty(menu.getMenuName()), SysMenu::getMenuName, menu.getMenuName())
               .eq(StringUtils.isNotEmpty(menu.getStatus()), SysMenu::getStatus, menu.getStatus())
               .orderByAsc(SysMenu::getParentId)
               .orderByAsc(SysMenu::getOrderNum);
        List<SysMenu> allMenus = menuMapper.selectList(wrapper);
        
        // 2. 构建树形结构
        return  buildMenuTree(allMenus, 0L);
    }

    // 递归构建菜单树
    private List<SysMenu> buildMenuTree(List<SysMenu> menus, Long parentId) {
        List<SysMenu> tree = new ArrayList<>();
        for (SysMenu menu : menus) {
            if (parentId.equals(menu.getParentId())) {
                // 查找子菜单
                List<SysMenu> children = buildMenuTree(menus, menu.getMenuId());
                if (!children.isEmpty()) {
                    menu.setChildren(children);
                }
                tree.add(menu);
            }
        }

        // 如果没有找到任何子菜单，返回空树
        return tree.isEmpty() ? new ArrayList<>() : tree;
    }

    @Override
    public List<Long> selectMenuListByRoleId(Long roleId) {
        return menuMapper.selectMenuListByRoleId(roleId);
    }

    /**
     * 递归列表
     */
    private void recursionFn(List<SysMenu> list, SysMenu t) {
        List<SysMenu> childList = getChildList(list, t);
        t.setChildren(childList);
        for (SysMenu tChild : childList) {
            if (hasChild(list, tChild)) {
                recursionFn(list, tChild);
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private List<SysMenu> getChildList(List<SysMenu> list, SysMenu t) {
        return list.stream()
                .filter(n -> n.getParentId().equals(t.getMenuId()))
                .collect(Collectors.toList());
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<SysMenu> list, SysMenu t) {
        return !getChildList(list, t).isEmpty();
    }
} 