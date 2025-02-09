package com.project.system2.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.project.system2.domain.entity.SysRoleMenu;
import com.project.system2.mapper.SysRoleMenuMapper;
import com.project.system2.service.ISysRoleMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SysRoleMenuServiceImpl implements ISysRoleMenuService {

    @Autowired
    private SysRoleMenuMapper roleMenuMapper;

    @Override
    @Transactional
    public boolean saveRoleMenu(Long roleId, Long[] menuIds) {
        // 先删除原有关联
        deleteRoleMenuByRoleId(roleId);
        
        // 添加新的关联
        if (menuIds != null && menuIds.length > 0) {
            List<SysRoleMenu> list = Arrays.stream(menuIds)
                    .map(menuId -> {
                        SysRoleMenu rm = new SysRoleMenu();
                        rm.setRoleId(roleId);
                        rm.setMenuId(menuId);
                        return rm;
                    }).collect(Collectors.toList());
            
            for (SysRoleMenu roleMenu : list) {
                roleMenuMapper.insert(roleMenu);
            }
        }
        
        return true;
    }

    @Override
    @Transactional
    public boolean deleteRoleMenuByRoleId(Long roleId) {
        LambdaQueryWrapper<SysRoleMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysRoleMenu::getRoleId, roleId);
        return roleMenuMapper.delete(wrapper) >= 0;
    }

    @Override
    @Transactional
    public boolean deleteRoleMenu(Long[] roleIds) {
        LambdaQueryWrapper<SysRoleMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(SysRoleMenu::getRoleId, Arrays.asList(roleIds));
        return roleMenuMapper.delete(wrapper) >= 0;
    }

    @Override
    public boolean checkMenuExistRole(Long menuId) {
        LambdaQueryWrapper<SysRoleMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysRoleMenu::getMenuId, menuId);
        return roleMenuMapper.selectCount(wrapper) > 0;
    }
} 