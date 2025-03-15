package com.project.system2.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@TableName("sys_role_menu")
@Schema(description = "角色和菜单关联实体")
public class SysRoleMenu {
    
    @Schema(description = "角色ID", example = "1")
    private Long roleId;
    
    @Schema(description = "菜单ID", example = "100")
    private Long menuId;
} 