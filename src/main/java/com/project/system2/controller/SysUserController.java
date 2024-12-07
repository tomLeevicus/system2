package com.project.system2.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.system2.common.core.domain.Result;
import com.project.system2.common.core.domain.PageQuery;
import com.project.system2.domain.entity.SysUser;
import com.project.system2.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system/user")
public class SysUserController {
    
    @Autowired
    private SysUserService userService;

    /**
     * 获取用户列表
     */
    @PreAuthorize("@ss.hasPermi('system:user:list')")
    @GetMapping("/list")
    public Result<Page<SysUser>> list(SysUser user, PageQuery pageQuery) {
        Page<SysUser> page = new Page<>(pageQuery.getPageNum(), pageQuery.getPageSize());
        return Result.success(userService.selectUserPage(page, user));
    }

    /**
     * 根据用户编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:user:query')")
    @GetMapping(value = "/{userId}")
    public Result<SysUser> getInfo(@PathVariable Long userId) {
        return Result.success(userService.getUserById(userId));
    }

    /**
     * 新增用户
     */
    @PreAuthorize("@ss.hasPermi('system:user:add')")
    @PostMapping
    public Result<Void> add(@Validated @RequestBody SysUser user) {
        return userService.addUser(user) ? Result.success() : Result.error();
    }

    /**
     * 修改用户
     */
    @PreAuthorize("@ss.hasPermi('system:user:edit')")
    @PutMapping
    public Result<Void> edit(@Validated @RequestBody SysUser user) {
        return userService.updateUser(user) ? Result.success() : Result.error();
    }

    /**
     * 删除用户
     */
    @PreAuthorize("@ss.hasPermi('system:user:remove')")
    @DeleteMapping("/{userIds}")
    public Result<Void> remove(@PathVariable Long[] userIds) {
        return userService.deleteUserByIds(userIds) ? Result.success() : Result.error();
    }

    /**
     * 重置密码
     */
    @PreAuthorize("@ss.hasPermi('system:user:resetPwd')")
    @PutMapping("/resetPwd")
    public Result<Void> resetPwd(@RequestBody SysUser user) {
        return userService.resetPwd(user) ? Result.success() : Result.error();
    }

    /**
     * 分配角色
     */
    @PreAuthorize("@ss.hasPermi('system:user:edit')")
    @PutMapping("/authRole")
    public Result<Void> insertAuthRole(Long userId, Long[] roleIds) {
        return userService.updateUserRole(userId, roleIds) ? Result.success() : Result.error();
    }
} 