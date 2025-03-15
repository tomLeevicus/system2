package com.project.system2.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.util.Date;

@Data
@TableName("act_task")
@Schema(description = "工作流任务实体")
public class ActTask extends BaseEntity {
    
    @TableId
    @Schema(description = "任务ID", example = "TASK_2023001")
    private String id;
    
    @Schema(description = "任务名称", example = "资产审批")
    private String name;
    
    @Schema(description = "流程实例ID", example = "PI_2023001")
    private String processInstanceId;
    
    @Schema(description = "流程定义ID", example = "PD_ASSET_PURCHASE:1:001")
    private String processDefinitionId;
    
    @Schema(description = "任务定义KEY", example = "approve_task")
    private String taskDefinitionKey;
    
    @Schema(description = "任务描述", example = "审批资产采购申请")
    private String description;
    
    @Schema(description = "任务受理人", example = "zhangsan")
    private String assignee;
    
    @Schema(description = "任务所有者", example = "admin")
    private String owner;
    
    @Schema(description = "截止日期", example = "2023-06-30 23:59:59")
    private Date dueDate;
    
    @Schema(description = "优先级", example = "50")
    private Integer priority;
    
    @Schema(description = "任务分类", example = "approval")
    private String category;
    
    @Schema(description = "表单KEY", example = "asset_form")
    private String formKey;
    
    @Schema(description = "租户ID", example = "T001")
    private String tenantId;
    
    @Schema(description = "父任务ID", example = "TASK_2023000")
    private String parentTaskId;
    
    @Schema(description = "是否挂起", example = "false")
    private Boolean suspended;
    
    @Schema(description = "业务KEY", example = "ASSET_BIZ_001")
    private String businessKey;
    
    @Schema(description = "执行ID", example = "EXE_001")
    private String executionId;
} 