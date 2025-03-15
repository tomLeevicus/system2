package com.project.system2.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_user")
@Schema(description = "系统用户实体")
public class SysUser extends BaseEntity {
    
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "用户ID", example = "1001")
    private Long id;

    @NotBlank(message = "用户账号不能为空")
    @Size(min = 0, max = 30, message = "用户账号长度不能超过30个字符")
    @Schema(description = "用户名", example = "zhangsan")
    private String username;

    @NotBlank(message = "用户昵称不能为空")
    @Size(min = 0, max = 30, message = "用户昵称长度不能超过30个字符")
    @Schema(description = "用户昵称", example = "管理员")
    private String nickname;

    @Email(message = "邮箱格式不正确")
    @Size(min = 0, max = 50, message = "邮箱长度不能超过50个字符")
    @Schema(description = "邮箱", example = "zhangsan@example.com")
    private String email;

    @Size(min = 0, max = 11, message = "手机号码长度不能超过11个字符")
    @Schema(description = "手机号码", example = "13800138000")
    private String mobile;

    @Schema(description = "用户性别（0男 1女 2未知）", example = "0")
    private String gender;

    @Schema(description = "头像地址", example = "/profile/avatar/2023/01/01/avatar.png")
    private String avatar;

    @TableField(select = false)
    @Schema(description = "密码", example = "[加密后的密码]")
    private String password;

    @Schema(description = "帐号状态（0停用 1正常）", example = "1")
    private String status;

    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "最后登录IP", example = "127.0.0.1")
    private String loginIp;

    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "最后登录时间", example = "2023-06-01 10:00:00")
    private Date lastLoginTime;

    @TableField(exist = false)
    @Schema(description = "角色ID列表")
    private Long[] roleIds;

    @TableField(exist = false)
    @Schema(description = "角色列表")
    private List<SysRole> roles;

    @TableField(exist = false)
    @Schema(description = "权限列表")
    private Set<String> permissions;

    @TableField(exist = false)
    @Schema(description = "部门ID", example = "103")
    private Long deptId;

    @TableField(exist = false)
    @Schema(description = "部门名称", example = "研发部")
    private String deptName;

    @TableField(exist = false)
    @Schema(description = "角色关键字列表")
    private List<String> roleKeys;

    @TableField(exist = false)
    @Schema(description = "部门对象")
    private SysDept dept;

    @Schema(description = "是否为管理员")
    public boolean isAdmin() {
        if (roleKeys == null || roleKeys.isEmpty()) {
            return false;
        }
        return roleKeys.contains("admin");
    }

    @Schema(description = "判断是否为管理员")
    public static boolean isAdmin(Long userId) {
        return userId != null && 1L == userId;
    }
}