package com.project.system2.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_menu")
public class SysMenu extends BaseEntity {

    @TableId(type = IdType.AUTO)
    private Long menuId;

    @NotBlank(message = "菜单名称不能为空")
    @Size(min = 0, max = 50, message = "菜单名称长度不能超过50个字符")
    private String menuName;

    private Long parentId;

    private Integer orderNum;

    private String path;

    private String component;

    private String menuType;

    private String visible;

    private String status;

    private String perms;

    private String icon;

    @TableField(exist = false)
    private List<SysMenu> children = new ArrayList<>();

    public boolean isMenu() {
        return menuType != null && ("M".equals(menuType) || "C".equals(menuType));
    }

    public boolean isButton() {
        return menuType != null && "F".equals(menuType);
    }
} 