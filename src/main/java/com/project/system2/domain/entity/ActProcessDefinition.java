package com.project.system2.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import java.util.Date;
import com.project.system2.domain.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@TableName("act_process_definition")
@Schema(description = "流程定义实体")
public class ActProcessDefinition extends BaseEntity {
    
    @Schema(description = "流程定义ID", example = "PD_2023001")
    @TableId
    private String id;

    @Schema(description = "流程名称", example = "资产采购审批流程")
    private String name;
    
    @Schema(description = "流程KEY", example = "ASSET_PURCHASE")
    @TableField("process_key")
    private String processKey;
    
    private String category;
    
    @Schema(description = "流程版本", example = "1")
    private Integer version;
    
    @Schema(description = "部署ID", example = "DEPLOY_001")
    private String deploymentId;
    
    private String resourceName;
    
    private String diagramResourceName;
    
    private String description;
    
    @Schema(description = "是否挂起", example = "false")
    private Boolean suspended;
} 