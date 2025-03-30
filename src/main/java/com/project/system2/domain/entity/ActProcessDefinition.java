package com.project.system2.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
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
    
    /**
     * 流程定义ID
     */
    @Schema(description = "流程定义ID", example = "1001")
    private String id;

    /**
     * 流程名称
     */
    @Schema(description = "流程名称", example = "资产采购审批流程")
    private String name;
    
    /**
     * 流程KEY
     */
    @Schema(description = "流程KEY", example = "ASSET_PURCHASE")
    @TableField("process_key")
    private String processKey;
    
    /**
     * 流程分类
     */
    @Schema(description = "流程分类", example = "资产管理")
    private String category;
    
    /**
     * 版本号
     */
    @Schema(description = "流程版本", example = "1")
    private Integer version;
    
    /**
     * 部署ID
     */
    @Schema(description = "部署ID", example = "DEPLOY_001")
    private String deploymentId;
    
    /**
     * 资源名称
     */
    @Schema(description = "资源名称", example = "asset_purchase.bpmn20.xml")
    private String resourceName;
    
    /**
     * 流程图资源名称
     */
    @Schema(description = "流程图资源名称", example = "asset_purchase.png")
    private String diagramResourceName;
    
    /**
     * 描述
     */
    @Schema(description = "描述", example = "用于资产采购的审批流程")
    private String description;
    
    /**
     * 是否挂起
     */
    @Schema(description = "是否挂起", example = "false")
    private Boolean suspended;
} 