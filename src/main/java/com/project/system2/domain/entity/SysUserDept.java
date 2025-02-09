package com.project.system2.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("sys_user_dept")
public class SysUserDept {
    @TableId
    private Long userId;
    private Long deptId;
    private Integer postSort;
} 