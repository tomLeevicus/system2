package com.project.system2.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.*;
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
    private String nickname;

    @Email(message = "邮箱格式不正确")
    @Size(min = 0, max = 50, message = "邮箱长度不能超过50个字符")
    @Schema(description = "邮箱", example = "zhangsan@example.com")
    private String email;

    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    @Schema(description = "手机号码", example = "13800138000")
    private String mobile;

    private String gender;

    private String avatar;

    @TableField(select = false)
    private String password;

    private String status;

    private String remark;

    private Date lastLoginTime;

    @TableField(exist = false)
    private List<SysRole> roles;

    @TableField(exist = false)
    private Set<String> permissions;

    @TableField(exist = false)
    private Long[] roleIds;

    @TableField(exist = false)
    private SysDept dept;

    @TableField(exist = false)
    private List<String> roleKeys;

    public boolean isAdmin() {
        if (roleKeys == null || roleKeys.isEmpty()) {
            return false;
        }
        return roleKeys.contains("admin");
    }

}