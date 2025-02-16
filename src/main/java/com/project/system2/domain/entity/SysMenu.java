package com.project.system2.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_menu")
public class SysMenu extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 菜单ID */
    @TableId(value = "menu_id", type = IdType.AUTO)
    private Long menuId;

    /** 父菜单ID */
    private Long parentId;

    /** 菜单名称 */
    @NotBlank(message = "菜单名称不能为空")
    @Size(min = 0, max = 50, message = "菜单名称长度不能超过50个字符")
    private String menuName;

    /** 路由地址 */
    private String path;

    /** 组件路径 */
    private String component;

    /** 路由参数 */
    private String query;

    /** 菜单状态（0显示 1隐藏） */
    private String visible;

    /** 菜单状态（0正常 1停用） */
    private String status;

    /** 权限标识 */
    private String perms;

    /** 是否为外链（0否 1是） */
    private Integer isFrame;

    /** 是否缓存（0不缓存 1缓存） */
    private Integer isCache;

    /** 菜单类型（M目录 C菜单 F按钮） */
    private String menuType;  

    /** 菜单图标 */
    private String icon;

    /** 显示顺序 */
    private Integer orderNum;

    /** 创建时间 */
    private Date createTime;

    /** 子菜单 */
    @TableField(exist = false)
    @Schema(description = "子菜单列表")
    private List<SysMenu> children;

    public boolean isMenu() {
        return menuType != null && ("M".equals(menuType) || "C".equals(menuType));
    }

    public boolean isButton() {
        return menuType != null && "F".equals(menuType);
    }

    public List<SysMenu> getChildren() {
        return children;
    }

    public void setChildren(List<SysMenu> children) {
        this.children = children;
    }
} 