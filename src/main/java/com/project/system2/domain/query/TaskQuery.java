package com.project.system2.domain.query;

import com.project.system2.common.core.domain.BaseQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 流程任务查询对象
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "流程任务查询对象")
public class TaskQuery extends BaseQuery {

    /**
     * 流程名称
     */
    @Schema(description = "流程名称")
    private String processName;
    
    /**
     * 流程状态
     */
    @Schema(description = "流程状态", allowableValues = {"running", "suspended", "completed"})
    private String status;
    
    /**
     * 流程实例ID
     */
    @Schema(description = "流程实例ID")
    private String processInstanceId;
    
    /**
     * 任务ID
     */
    @Schema(description = "任务ID")
    private String taskId;
    
    /**
     * 任务名称
     */
    @Schema(description = "任务名称")
    private String taskName;
    
    /**
     * 任务处理人ID
     */
    @Schema(description = "任务处理人ID")
    private Long assigneeId;
    
    /**
     * 流程发起人ID
     */
    @Schema(description = "流程发起人ID")
    private Long startUserId;
    
    /**
     * 流程发起人姓名
     */
    @Schema(description = "流程发起人姓名")
    private String startUserName;
    
    /**
     * 开始时间(起)
     */
    @Schema(description = "开始时间(起)")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTimeBegin;
    
    /**
     * 开始时间(止)
     */
    @Schema(description = "开始时间(止)")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTimeEnd;
    
    /**
     * 创建时间(起)
     */
    @Schema(description = "创建时间(起)")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTimeBegin;
    
    /**
     * 创建时间(止)
     */
    @Schema(description = "创建时间(止)")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTimeEnd;
    
    /**
     * 业务关键字
     */
    @Schema(description = "业务关键字")
    private String businessKey;
} 