package com.project.system2.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@TableName("act_form_definition")
@Schema(description = "表单定义实体")
public class FormDefinition extends BaseEntity {
    
    @TableId
    @Schema(description = "表单ID", example = "FORM_001")
    private String id;
    
    @Schema(description = "表单名称", example = "资产采购申请表")
    private String name;
    
    @Schema(description = "表单KEY", example = "asset_purchase_form")
    private String key;
    
    @Schema(description = "分类", example = "资产类表单")
    private String category;
    
    @Schema(description = "表单版本", example = "1.0")
    private Integer version;
    
    @Schema(description = "资源名称", example = "asset-purchase-form.json")
    private String resourceName;
    
    @Schema(description = "部署ID", example = "DEP_001")
    private String deploymentId;
    
    @Schema(description = "租户ID", example = "T001")
    private String tenantId;
    
    @Schema(description = "表单JSON结构", example = "{...}")
    private String formDefinition; // JSON格式的表单定义
} 