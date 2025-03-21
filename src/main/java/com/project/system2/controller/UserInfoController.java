package com.project.system2.controller;

import com.project.system2.common.core.domain.Result;
import com.project.system2.common.core.utils.SecurityUtils;
import com.project.system2.domain.entity.SysUser;
import com.project.system2.service.ISysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/system/userInfo")
@Tag(name = "用户管理", description = "系统用户管理接口")
public class UserInfoController {

    @Autowired
    private ISysUserService userService;


    /**
     * 获取当前登录用户的信息
     */
    @GetMapping(value = "/getInfo")
    @Operation(summary = "获取用户详情", description = "根据用户ID获取详细信息")
    @Parameter(name = "userId", description = "用户ID", example = "1", required = true)
    public Result<SysUser> getInfo() {
        Long userId = SecurityUtils.getUserId();
        if (userId != null) {
            return Result.success(userService.getUserById(userId));
        }
        return Result.error();
    }
}
