package com.project.system2.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("sys_role_menu")
public class SysRoleMenu {
    
    private Long roleId;
    
    private Long menuId;
} 