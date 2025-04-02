package com.project.system2.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 任务信息实体类
 */
@Data
@TableName("act_task_info")
@Schema(description = "任务信息实体")
public class ActTaskInfo extends BaseEntity {

    /**
     * 任务ID
     */
    @TableId
    @Schema(description = "任务ID", example = "task-1001")
    private String id;

    /**
     * 任务名称
     */
    @Schema(description = "任务名称", example = "部门经理审批")
    private String name;

    /**
     * 任务描述
     */
    @Schema(description = "任务描述", example = "请部门经理审批该请假申请")
    private String description;

    /**
     * 优先级
     */
    @Schema(description = "优先级", example = "50")
    private Integer priority;

    /**
     * 任务所有者
     */
    @Schema(description = "任务所有者", example = "admin")
    private String owner;

    /**
     * 任务受理人
     */
    @Schema(description = "任务受理人", example = "zhangsan")
    private String assignee;

    /**
     * 任务受理人名称
     */
    @TableField(exist = false)
    @Schema(description = "任务受理人名称", example = "张三")
    private String assigneeName;

    /**
     * 流程实例ID
     */
    @Schema(description = "流程实例ID", example = "instance-1001")
    private String processInstanceId;

    /**
     * 流程实例名称
     */
    @TableField(exist = false)
    @Schema(description = "流程实例名称", example = "请假申请-张三")
    private String processInstanceName;

    /**
     * 执行ID
     */
    @Schema(description = "执行ID", example = "exec-1001")
    private String executionId;

    /**
     * 任务定义Key
     */
    @Schema(description = "任务定义Key", example = "managerApproval")
    private String taskDefinitionKey;

    /**
     * 任务创建时间
     */
    @Schema(description = "任务创建时间", example = "2023-06-01 10:00:00")
    private Date createTime;

    /**
     * 任务到期日期
     */
    @Schema(description = "任务到期日期", example = "2023-06-05 10:00:00")
    private Date dueDate;

    /**
     * 任务认领时间
     */
    @Schema(description = "任务认领时间", example = "2023-06-02 10:00:00")
    private Date claimTime;

    /**
     * 任务类别
     */
    @Schema(description = "任务类别", example = "approval")
    private String category;

    /**
     * 租户ID
     */
    @Schema(description = "租户ID", example = "tenant1")
    private String tenantId;

    /**
     * 表单Key
     */
    @Schema(description = "表单Key", example = "leaveForm")
    private String formKey;

    /**
     * 流程定义ID
     */
    @Schema(description = "流程定义ID", example = "leave:1:1001")
    private String processDefinitionId;

    /**
     * 流程定义名称
     */
    @TableField(exist = false)
    @Schema(description = "流程定义名称", example = "请假流程")
    private String processDefinitionName;

    /**
     * 范围ID
     */
    @Schema(description = "范围ID", example = "scope-1001")
    private String scopeId;

    /**
     * 范围类型
     */
    @Schema(description = "范围类型", example = "cmmn")
    private String scopeType;

    /**
     * 范围定义ID
     */
    @Schema(description = "范围定义ID", example = "def-1001")
    private String scopeDefinitionId;

    /**
     * 子范围ID
     */
    @Schema(description = "子范围ID", example = "sub-1001")
    private String subScopeId;

    /**
     * 父任务ID
     */
    @Schema(description = "父任务ID", example = "parent-1001")
    private String parentTaskId;

    /**
     * 任务状态
     */
    @Schema(description = "任务状态", example = "pending")
    private String status;

    /**
     * 是否挂起
     */
    @Schema(description = "是否挂起", example = "false")
    private Boolean suspended;

    /**
     * 任务类型
     */
    @Schema(description = "任务类型", example = "userTask")
    private String taskType;

    /**
     * 业务关键字
     */
    @Schema(description = "业务关键字", example = "LEAVE-2023-001")
    private String businessKey;
} 