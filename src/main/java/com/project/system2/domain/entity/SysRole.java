package com.project.system2.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Set;
import java.util.List;

@Data
@TableName("sys_role")
@Schema(description = "系统角色实体")
public class SysRole extends BaseEntity {
    
    /** 超级管理员角色权限标识 */
    public static final String ADMIN_ROLE_KEY = "admin";
    
    @TableId(type = IdType.AUTO)
    @Schema(description = "角色ID", example = "1")
    private Long roleId;

    @NotBlank(message = "角色名称不能为空")
    @Size(min = 0, max = 30, message = "角色名称长度不能超过30个字符")
    @Schema(description = "角色名称", example = "管理员")
    private String roleName;

    @NotBlank(message = "权限字符不能为空")
    @Size(min = 0, max = 100, message = "权限字符长度不能超过100个字符")
    @Schema(description = "角色权限字符串", example = "admin")
    private String roleKey;

    @NotNull(message = "显示顺序不能为空")
    private Integer roleSort;

    @Schema(description = "角色状态（0-正常，1-停用）", example = "0")
    private String status;

    private String remark;

    /** 菜单组 */
    @TableField(exist = false)
    @Schema(description = "菜单ID集合")
    private List<Long> menuIds;

    /** 部门组（数据权限） */
    @TableField(exist = false)
    private Long[] deptIds;

    /** 角色菜单权限 */
    private Set<String> permissions;

    public boolean isAdmin() {
        return isAdmin(this.roleId) || ADMIN_ROLE_KEY.equals(this.roleKey);
    }

    public static boolean isAdmin(Long roleId) {
        return roleId != null && 1L == roleId;
    }

    /**
     * 判断是否为超级管理员角色
     */
    public static boolean isAdminRole(String roleKey) {
        return ADMIN_ROLE_KEY.equals(roleKey);
    }

    public List<Long> getMenuIds() {
        return menuIds;
    }

    public void setMenuIds(List<Long> menuIds) {
        this.menuIds = menuIds;
    }
} 