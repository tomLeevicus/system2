package com.project.system2.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.project.system2.common.core.utils.EntityUtils;
import com.project.system2.domain.entity.SysRole;
import com.project.system2.mapper.SysRoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.project.system2.common.core.utils.StringUtils;
import com.project.system2.domain.entity.SysMenu;
import com.project.system2.domain.query.SysMenuQuery;
import com.project.system2.mapper.SysMenuMapper;
import com.project.system2.service.ISysMenuService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu>  implements ISysMenuService {

    private static final Logger log = LoggerFactory.getLogger(SysMenuServiceImpl.class);

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
            log.debug("普通用户 {} 查询到的原始菜单数据: {}", userId, menus);
        }


        return buildMenuTree(menus);
    }

    @Override
    public List<SysMenu> buildMenuTree(List<SysMenu> menus) {
        log.debug("开始构建菜单树，原始数据量: {}", menus.size());
        
        List<SysMenu> returnList = new ArrayList<>();
        List<Long> tempList = menus.stream()
            .map(SysMenu::getMenuId)
            .collect(Collectors.toList());
        
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
        EntityUtils.setCreateAndUpdateInfo(menu,true);
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
    public List<SysMenu> selectMenuList(SysMenuQuery query) {
        LambdaQueryWrapper<SysMenu> lqw = new LambdaQueryWrapper<>();
        
        if (query != null) {
            // 根据菜单名称模糊查询
            if (StringUtils.isNotBlank(query.getMenuName())) {
                lqw.like(SysMenu::getMenuName, query.getMenuName());
            }
            
            // 根据菜单状态查询
            if (StringUtils.isNotBlank(query.getVisible())) {
                lqw.eq(SysMenu::getVisible, query.getVisible());
            }
            
            // 根据菜单类型查询
            if (StringUtils.isNotBlank(query.getMenuType())) {
                lqw.eq(SysMenu::getMenuType, query.getMenuType());
            }
            
            // 添加排序条件
            if (StringUtils.isNotBlank(query.getOrderByColumn())) {
                // 这里需要根据实际的排序字段做映射处理
                // 简单示例:
                if ("createTime".equals(query.getOrderByColumn())) {
                    lqw.orderBy(true, "asc".equalsIgnoreCase(query.getIsAsc()), SysMenu::getCreateTime);
                }
            } else {
                // 默认排序
                lqw.orderByAsc(SysMenu::getParentId)
                   .orderByAsc(SysMenu::getOrderNum);
            }
        }
        
        // 查询数据
        List<SysMenu> menus = list(lqw);
        
        // 构建树结构
        return buildMenuTree(menus);
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

    @Override
    public List<Long> selectMenuListByRoleId(Long roleId) {
        return menuMapper.selectMenuListByRoleId(roleId);
    }
} 