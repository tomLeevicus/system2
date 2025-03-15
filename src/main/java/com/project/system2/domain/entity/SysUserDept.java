package com.project.system2.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@TableName("sys_user_dept")
@Schema(description = "用户与部门关联实体")
public class SysUserDept {
    @TableId
    @Schema(description = "用户ID", example = "1001")
    private Long userId;
    
    @Schema(description = "部门ID", example = "101")
    private Long deptId;
    
    @Schema(description = "职位排序(数字越小级别越高)", example = "1")
    private Integer postSort;
} 