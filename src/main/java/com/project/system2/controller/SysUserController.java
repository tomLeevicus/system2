package com.project.system2.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.system2.common.core.domain.Result;
import com.project.system2.common.core.domain.PageQuery;
import com.project.system2.domain.entity.SysUser;
import com.project.system2.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/system/user")
@Tag(name = "用户管理", description = "系统用户管理接口")
public class SysUserController {
    
    @Autowired
    private ISysUserService userService;

    /**
     * 获取用户列表
     */
    @PreAuthorize("@ss.hasPermi('system:user:list')")
    @GetMapping("/list")
    @Operation(summary = "分页查询用户列表", description = "根据条件分页查询系统用户")
    @Parameter(name = "pageNum", description = "页码", example = "1")
    @Parameter(name = "pageSize", description = "每页数量", example = "10")
    public Result<Page<SysUser>> list(SysUser user, PageQuery pageQuery) {
        Page<SysUser> page = new Page<>(pageQuery.getPageNum(), pageQuery.getPageSize());
        return Result.success(userService.selectUserPage(page, user));
    }

    /**
     * 根据用户编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:user:query')")
    @GetMapping(value = "/getInfo/{userId}")
    @Operation(summary = "获取用户详情", description = "根据用户ID获取详细信息")
    @Parameter(name = "userId", description = "用户ID", example = "1", required = true)
    public Result<SysUser> getInfo(@PathVariable Long userId) {
        return Result.success(userService.getUserById(userId));
    }

    /**
     * 新增用户
     */
    @PreAuthorize("@ss.hasPermi('system:user:add')")
    @PostMapping("/add")
    @Operation(summary = "新增用户", description = "创建新的系统用户")
    @Parameter(name = "user", description = "用户对象", required = true)
    public Result<Void> add(@Validated @RequestBody SysUser user) {
        return userService.addUser(user) ? Result.success() : Result.error();
    }

    /**
     * 修改用户
     */
    @PreAuthorize("@ss.hasPermi('system:user:edit')")
    @PutMapping("/edit")
    @Operation(summary = "修改用户", description = "更新现有用户信息")
    @Parameter(name = "user", description = "用户对象", required = true)
    public Result<Void> edit(@Validated @RequestBody SysUser user) {
        return userService.updateUser(user) ? Result.success() : Result.error();
    }

    /**
     * 删除用户
     */
    @PreAuthorize("@ss.hasPermi('system:user:remove')")
    @DeleteMapping("/remove/{userIds}")
    @Operation(summary = "删除用户", description = "根据ID批量删除用户")
    @Parameter(name = "userIds", description = "用户ID数组", example = "[1001,1002]", required = true)
    public Result<Void> remove(@PathVariable Long[] userIds) {
        return userService.deleteUserByIds(userIds) ? Result.success() : Result.error();
    }

    /**
     * 重置密码
     */
    @PreAuthorize("@ss.hasPermi('system:user:resetPwd')")
    @PutMapping("/resetPwd")
    @Operation(summary = "重置密码", description = "重置指定用户的登录密码")
    @Parameter(name = "user", description = "包含新密码的用户对象", required = true)
    public Result<Void> resetPwd(@RequestBody SysUser user) {
        return userService.resetPwd(user) ? Result.success() : Result.error();
    }

    /**
     * 分配角色
     */
    @PreAuthorize("@ss.hasPermi('system:user:edit')")
    @PutMapping("/authRole")
    @Operation(summary = "分配角色", description = "为用户分配系统角色")
    @Parameter(name = "userId", description = "用户ID", example = "1001", required = true)
    @Parameter(name = "roleIds", description = "角色ID数组", example = "[1,2]", required = true)
    public Result<Void> insertAuthRole(Long userId, Long[] roleIds) {
        return userService.updateUserRole(userId, roleIds) ? Result.success() : Result.error();
    }
} 