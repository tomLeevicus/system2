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
@Schema(description = "菜单权限实体")
public class SysMenu extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 菜单ID */
    @TableId(value = "menu_id", type = IdType.AUTO)
    @Schema(description = "菜单ID", example = "1")
    private Long menuId;

    /** 父菜单ID */
    @Schema(description = "父菜单ID", example = "0")
    private Long parentId;

    /** 菜单名称 */
//    @NotBlank(message = "菜单名称不能为空")
    @Size(min = 0, max = 50, message = "菜单名称长度不能超过50个字符")
    @Schema(description = "菜单名称", example = "系统管理")
    private String menuName;

    /** 路由地址 */
    @Schema(description = "路由地址", example = "/system")
    private String path;

    /** 组件路径 */
    @Schema(description = "组件路径", example = "system/index")
    private String component;

    /** 路由参数 */
    @Schema(description = "路由参数", example = "id=1")
    private String query;

    /** 菜单状态（0显示 1隐藏） */
    @Schema(description = "菜单状态（0显示 1隐藏）", example = "0")
    private String visible;

    /** 是否启用（1启用 0停用） */
    @Schema(description = "是否启用（1启用 0停用）", example = "1")
    private String status;

    /** 权限标识 */
    @Schema(description = "权限标识", example = "system:user:list")
    private String perms;

    /** 是否为外链（0否 1是） */
    @Schema(description = "是否为外链（0否 1是）", example = "0")
    private Integer isFrame;

    /** 是否缓存（0不缓存 1缓存） */
    @Schema(description = "是否缓存（0不缓存 1缓存）", example = "0")
    private Integer isCache;

    /** 菜单类型（M目录 C菜单 F按钮） */
    @Schema(description = "菜单类型（M目录 C菜单 F按钮）", example = "C")
    private String menuType;  

    /** 菜单图标 */
    @Schema(description = "菜单图标", example = "system")
    private String icon;

    /** 显示顺序 */
    @Schema(description = "显示顺序", example = "1")
    private Integer orderNum;

    /** 创建时间 */
    @Schema(description = "创建时间", example = "2023-01-01 12:00:00")
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