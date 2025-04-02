package com.project.system2.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import java.util.Date;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@TableName("act_process_instance")
@Schema(description = "流程实例实体")
public class ActProcessInstance extends BaseEntity {
    
    @TableId
    @Schema(description = "流程实例ID", example = "PI_2023001")
    private String id;
    
    @Schema(description = "流程定义ID", example = "PD_2023001")
    private String processDefinitionId;
    
    @Schema(description = "流程定义Key", example = "assetCall")
    private String processDefinitionKey;
    
    @Schema(description = "流程定义版本", example = "1")
    private Integer processDefinitionVersion;
    
    @Schema(description = "业务关键字", example = "BusinessKey123")
    private String businessKey;
    
    @Schema(description = "流程状态(running/suspended/completed)", example = "running")
    private String status;
    
    @Schema(description = "是否挂起", example = "false")
    private Boolean suspended;
    
    @Schema(description = "开始时间")
    private Date startTime;
    
    @Schema(description = "结束时间")
    private Date endTime;
    
    @Schema(description = "发起人ID", example = "1001")
    private Long startUserId;
    
    @Schema(description = "发起人姓名", example = "John Doe")
    private String startUserName;
    
    @Schema(description = "流程实例名称", example = "资产调用流程实例")
    private String name;
    
    @Schema(description = "当前任务ID")
    private String taskId;
    
    @Schema(description = "任务名称")
    private String taskName;
    
    @Schema(description = "处理人")
    private String assignee;
    
    @Schema(description = "任务结束时间")
    private Date taskEndTime;
    
    @TableField(exist = false)
    private String taskStatus; // 任务状态（active/suspended）

//    任务创建时间
    private Date taskCreatecTime;

} 