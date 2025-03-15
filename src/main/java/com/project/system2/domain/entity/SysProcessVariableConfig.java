package com.project.system2.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_process_variable_config")
@Schema(description = "流程变量配置实体")
public class SysProcessVariableConfig extends BaseEntity {
    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "配置ID", example = "CONFIG_001")
    private Long id;
    
    /**
     * 流程标识
     */
    @Schema(description = "流程定义ID", example = "PD_2023001")
    private String processKey;
    
    /**
     * 参数名称
     */
    @Schema(description = "变量名称", example = "approverList")
    private String name;
    
    /**
     * 参数标签
     */
    private String label;
    
    /**
     * 参数类型
     */
    @Schema(description = "变量类型", example = "0")
    private String variableType;
    
    /**
     * 是否必填
     */
    @Schema(description = "是否必填（0-否，1-是）", example = "1")
    private Boolean required;
    
    /**
     * 默认值
     */
    @Schema(description = "默认值", example = "['zhangsan','lisi']")
    private String defaultValue;
    
    /**
     * 验证规则（JSON格式）
     */
    private String validator;
    
    /**
     * 排序号
     */
    @Schema(description = "排序号", example = "1")
    private Integer sortOrder;
    
    /**
     * 状态（0正常 1停用）
     */
    @Schema(description = "配置状态（0-启用，1-停用）", example = "0")
    private Integer status;


    /*@Schema(description = "关联值（角色KEY/用户ID/部门ID）", example = "asset_approver")
    private String relatedValue;*/
} 