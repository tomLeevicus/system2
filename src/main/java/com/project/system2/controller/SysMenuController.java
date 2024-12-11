package com.project.system2.controller;

import com.project.system2.common.core.domain.Result;
import com.project.system2.domain.entity.SysMenu;
import com.project.system2.service.SysMenuService;
import com.project.system2.common.core.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/system/menu")
public class SysMenuController {
    
    @Autowired
    private SysMenuService menuService;

    /**
     * 获取菜单列表
     */
    @PreAuthorize("@ss.hasPermi('system:menu:list')")
    @GetMapping("/list")
    public Result<List<SysMenu>> list(SysMenu menu) {
        List<SysMenu> menus = menuService.selectMenuList(menu);
        return Result.success(menus);
    }

    /**
     * 根据菜单编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:menu:query')")
    @GetMapping(value = "/{menuId}")
    public Result<SysMenu> getInfo(@PathVariable Long menuId) {
        return Result.success(menuService.selectMenuById(menuId));
    }

    /**
     * 获取菜单下拉树列表
     */
    @GetMapping("/treeselect")
    public Result<List<SysMenu>> treeselect(SysMenu menu) {
        List<SysMenu> menus = menuService.selectMenuList(menu);
        return Result.success(menuService.buildMenuTree(menus));
    }

    /**
     * 加载对应角色菜单列表树
     */
    @GetMapping(value = "/roleMenuTreeselect/{roleId}")
    public Result<Map<String, Object>> roleMenuTreeselect(@PathVariable Long roleId) {
        List<SysMenu> menus = menuService.selectMenuList(new SysMenu());
        Map<String, Object> result = new HashMap<>();
        result.put("checkedKeys", menuService.selectMenuListByRoleId(roleId));
        result.put("menus", menuService.buildMenuTree(menus));
        return Result.success(result);
    }

    /**
     * 新增菜单
     */
    @PreAuthorize("@ss.hasPermi('system:menu:add')")
    @PostMapping
    public Result<Void> add(@Validated @RequestBody SysMenu menu) {
        return menuService.insertMenu(menu) ? Result.success() : Result.error();
    }

    /**
     * 修改菜单
     */
    @PreAuthorize("@ss.hasPermi('system:menu:edit')")
    @PutMapping
    public Result<Void> edit(@Validated @RequestBody SysMenu menu) {
        if (menuService.hasChildByMenuId(menu.getMenuId())) {
            return Result.error("存在子菜单,不允许修改");
        }
        return menuService.updateMenu(menu) ? Result.success() : Result.error();
    }

    /**
     * 删除菜单
     */
    @PreAuthorize("@ss.hasPermi('system:menu:remove')")
    @DeleteMapping("/{menuId}")
    public Result<Void> remove(@PathVariable Long menuId) {
        if (menuService.hasChildByMenuId(menuId)) {
            return Result.error("存在子菜单,不允许删除");
        }
        if (menuService.checkMenuExistRole(menuId)) {
            return Result.error("菜单已分配,不允许删除");
        }
        return menuService.deleteMenuById(menuId) ? Result.success() : Result.error();
    }

    /**
     * 获取路由信息
     */
    @GetMapping("/getRouters")
    public Result<List<SysMenu>> getRouters() {
        Long userId = SecurityUtils.getUserId();
        List<SysMenu> menus = menuService.selectMenuTreeByUserId(userId);
        return Result.success(menus);
    }
} 