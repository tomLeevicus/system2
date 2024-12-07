package com.project.system2.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_menu")
public class SysMenu extends BaseEntity {

    @TableId(type = IdType.AUTO)
    private Long menuId;

    private Long parentId;

    private String menuName;

    private String path;

    private String component;

    private String perms;

    private String icon;

    private String menuType;

    private Integer orderNum;

    private String status;

    private String visible;

    @TableField(fill = FieldFill.INSERT)
    private String delFlag;

    @TableField(exist = false)
    private List<SysMenu> children;
} 