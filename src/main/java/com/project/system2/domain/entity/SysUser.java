package com.project.system2.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_user")
public class SysUser extends BaseEntity {

    @TableId(type = IdType.AUTO)
    private Long userId;

    private Long deptId;

    private String username;

    private String nickname;

    private String email;

    private String mobile;

    private String gender;

    private String avatar;

    private String password;

    private String status;

    @TableField(fill = FieldFill.INSERT)
    private String delFlag;

    private LocalDateTime lastLoginTime;
} 