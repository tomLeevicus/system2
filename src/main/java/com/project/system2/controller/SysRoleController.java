package com.project.system2.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.system2.common.core.domain.Result;
import com.project.system2.common.core.domain.PageQuery;
import com.project.system2.domain.entity.SysRole;
import com.project.system2.service.ISysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/system/role")
@Tag(name = "角色管理", description = "系统角色管理接口")
public class SysRoleController {
    
    @Autowired
    private ISysRoleService roleService;

    /**
     * 获取角色列表
     */
    @PreAuthorize("@ss.hasPermi('system:role:list')")
    @GetMapping("/list")
    @Operation(summary = "分页查询角色列表", description = "根据条件分页查询系统角色")
    @Parameter(name = "pageNum", description = "页码", example = "1")
    @Parameter(name = "pageSize", description = "每页数量", example = "10")
    public Result<IPage<SysRole>> list(SysRole role, PageQuery pageQuery) {
        IPage<SysRole> page = new Page<>(pageQuery.getPageNum(), pageQuery.getPageSize());
        return Result.success(roleService.selectRolePage(page, role));
    }

    /**
     * 根据角色编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:role:query')")
    @GetMapping(value = "/roleId/{roleId}")
    @Operation(summary = "获取角色详情", description = "根据角色ID获取详细信息")
    @Parameter(name = "roleId", description = "角色ID", example = "1", required = true)
    public Result<SysRole> getInfo(@PathVariable Long roleId) {
        return Result.success(roleService.selectRoleById(roleId));
    }

    /**
     * 新增角色
     */
    @PreAuthorize("@ss.hasPermi('system:role:add')")
    @PostMapping("/add")
    @Operation(summary = "新增角色", description = "创建新的系统角色")
    @Parameter(name = "role", description = "角色对象", required = true)
    public Result<Void> add(@Validated @RequestBody SysRole role) {
        return roleService.insertRole(role) ? Result.success() : Result.error();
    }

    /**
     * 修改角色
     */
    @PreAuthorize("@ss.hasPermi('system:role:edit')")
    @PutMapping("/edit")
    @Operation(summary = "修改角色", description = "更新现有角色信息")
    @Parameter(name = "role", description = "角色对象", required = true)
    public Result<Void> edit(@Validated @RequestBody SysRole role) {
        return roleService.updateRole(role) ? Result.success() : Result.error();
    }

    /**
     * 删除角色
     */
    @PreAuthorize("@ss.hasPermi('system:role:remove')")
    @DeleteMapping("/remove/{roleIds}")
    @Operation(summary = "删除角色", description = "根据ID批量删除角色")
    @Parameter(name = "roleIds", description = "角色ID数组", example = "[1,2,3]", required = true)
    public Result<Void> remove(@PathVariable Long[] roleIds) {
        return roleService.deleteRoleByIds(roleIds) ? Result.success() : Result.error();
    }

    /**
     * 修改保存数据权限
     */
    @PreAuthorize("@ss.hasPermi('system:role:edit')")
    @PutMapping("/dataScope")
    @Operation(summary = "更新数据权限", description = "更新角色的数据权限范围")
    @Parameter(name = "role", description = "包含新数据权限的角色对象", required = true)
    public Result<Void> dataScope(@RequestBody SysRole role) {
        return roleService.updateRoleDataScope(role) ? Result.success() : Result.error();
    }

    /**
     * 修改角色状态
     */
    @PreAuthorize("@ss.hasPermi('system:role:edit')")
    @PutMapping("/changeStatus")
    @Operation(summary = "修改角色状态", description = "启用或禁用角色")
    @Parameter(name = "role", description = "包含新状态的角色对象", required = true)
    public Result<Void> changeStatus(@RequestBody SysRole role) {
        return roleService.updateRoleStatus(role) ? Result.success() : Result.error();
    }

    /**
     * 分配菜单
     */
    @PreAuthorize("@ss.hasPermi('system:role:edit')")
    @PutMapping("/authMenu")
    @Operation(summary = "分配菜单权限", description = "为角色分配菜单访问权限")
    @Parameter(name = "roleId", description = "角色ID", example = "1", required = true)
    @Parameter(name = "menuIds", description = "菜单ID数组", example = "[100,101]", required = true)
    public Result<Void> authMenu(Long roleId, Long[] menuIds) {
        return roleService.updateRoleMenu(roleId, menuIds) ? Result.success() : Result.error();
    }
} 