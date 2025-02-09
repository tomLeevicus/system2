package com.project.system2.controller;

import com.project.system2.common.core.domain.Result;
import com.project.system2.domain.entity.SysMenu;
import com.project.system2.service.ISysMenuService;
import com.project.system2.common.core.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/system/menu")
@Tag(name = "菜单管理", description = "系统菜单管理接口")
public class SysMenuController {
    
    @Autowired
    private ISysMenuService menuService;

    /**
     * 获取菜单列表
     */
    @PreAuthorize("@ss.hasPermi('system:menu:list')")
    @GetMapping("/list")
    @Operation(summary = "获取菜单列表", description = "根据条件查询菜单列表")
    @Parameter(name = "menuName", description = "菜单名称", example = "系统管理")
    public Result<List<SysMenu>> list(SysMenu menu) {
        List<SysMenu> menus = menuService.selectMenuList(menu);
        return Result.success(menus);
    }

    /**
     * 根据菜单编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:menu:query')")
    @GetMapping(value = "/{menuId}")
    @Operation(summary = "获取菜单详情", description = "根据菜单ID获取详细信息")
    @Parameter(name = "menuId", description = "菜单ID", example = "100", required = true)
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
    @Operation(summary = "新增菜单", description = "创建新的系统菜单")
    @Parameter(name = "menu", description = "菜单对象", required = true)
    public Result<Void> add(@Validated @RequestBody SysMenu menu) {
        return menuService.insertMenu(menu) ? Result.success() : Result.error();
    }

    /**
     * 修改菜单
     */
    @PreAuthorize("@ss.hasPermi('system:menu:edit')")
    @PutMapping
    @Operation(summary = "修改菜单", description = "更新现有菜单信息")
    @Parameter(name = "menu", description = "菜单对象", required = true)
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
    @Operation(summary = "删除菜单", description = "根据ID删除菜单项")
    @Parameter(name = "menuId", description = "菜单ID", example = "100", required = true)
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
     * @return 路由信息
     */
    @GetMapping("/getRouters")
    @Operation(summary = "获取用户路由", description = "获取当前用户的菜单路由信息")
    public Result<List<SysMenu>> getRouters() {
        Long userId = SecurityUtils.getUserId();
        List<SysMenu> menus = menuService.selectMenuTreeByUserId(userId);
        return Result.success(menus);
    }
} 