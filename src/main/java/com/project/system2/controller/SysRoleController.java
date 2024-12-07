package com.project.system2.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.system2.common.core.domain.Result;
import com.project.system2.common.core.domain.PageQuery;
import com.project.system2.domain.entity.SysRole;
import com.project.system2.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system/role")
public class SysRoleController {
    
    @Autowired
    private SysRoleService roleService;

    /**
     * 获取角色列表
     */
    @PreAuthorize("@ss.hasPermi('system:role:list')")
    @GetMapping("/list")
    public Result<Page<SysRole>> list(SysRole role, PageQuery pageQuery) {
        Page<SysRole> page = new Page<>(pageQuery.getPageNum(), pageQuery.getPageSize());
        return Result.success(roleService.selectRolePage(page, role));
    }

    /**
     * 根据角色编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:role:query')")
    @GetMapping(value = "/{roleId}")
    public Result<SysRole> getInfo(@PathVariable Long roleId) {
        return Result.success(roleService.selectRoleById(roleId));
    }

    /**
     * 新增角色
     */
    @PreAuthorize("@ss.hasPermi('system:role:add')")
    @PostMapping
    public Result<Void> add(@Validated @RequestBody SysRole role) {
        return roleService.insertRole(role) ? Result.success() : Result.error();
    }

    /**
     * 修改角色
     */
    @PreAuthorize("@ss.hasPermi('system:role:edit')")
    @PutMapping
    public Result<Void> edit(@Validated @RequestBody SysRole role) {
        return roleService.updateRole(role) ? Result.success() : Result.error();
    }

    /**
     * 删除角色
     */
    @PreAuthorize("@ss.hasPermi('system:role:remove')")
    @DeleteMapping("/{roleIds}")
    public Result<Void> remove(@PathVariable Long[] roleIds) {
        return roleService.deleteRoleByIds(roleIds) ? Result.success() : Result.error();
    }

    /**
     * 修改保存数据权限
     */
    @PreAuthorize("@ss.hasPermi('system:role:edit')")
    @PutMapping("/dataScope")
    public Result<Void> dataScope(@RequestBody SysRole role) {
        return roleService.updateRoleDataScope(role) ? Result.success() : Result.error();
    }

    /**
     * 修改角色状态
     */
    @PreAuthorize("@ss.hasPermi('system:role:edit')")
    @PutMapping("/changeStatus")
    public Result<Void> changeStatus(@RequestBody SysRole role) {
        return roleService.updateRoleStatus(role) ? Result.success() : Result.error();
    }

    /**
     * 分配菜单
     */
    @PreAuthorize("@ss.hasPermi('system:role:edit')")
    @PutMapping("/authMenu")
    public Result<Void> authMenu(Long roleId, Long[] menuIds) {
        return roleService.updateRoleMenu(roleId, menuIds) ? Result.success() : Result.error();
    }
} 