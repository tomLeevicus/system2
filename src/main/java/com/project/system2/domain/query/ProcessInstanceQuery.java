package com.project.system2.domain.query;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;

@Data
public class ProcessInstanceQuery {
    // 流程定义相关
    private String processDefinitionId;
    private String processDefinitionKey;
    
    // 实例基本信息
    private String name;
    private String businessKey;
    
    // 状态查询
    private Boolean suspended;       // 是否挂起（true/false）
    private String status;          // 流程实例状态（对应字典值：running/suspended等）
    
    // 发起人信息
    private Long startUserId;       // 发起人ID
    private String startUserName;   // 发起人姓名
    
    // 时间范围查询
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTimeBegin;    // 开始时间查询起始
    
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTimeEnd;      // 开始时间查询截止
    
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTimeBegin;      // 结束时间查询起始
    
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTimeEnd;        // 结束时间查询截止
    
    // 分页参数
    private Integer pageNum = 1;
    private Integer pageSize = 10;
    
    // 排序字段（示例值：start_time desc, end_time asc）
    private String orderBy; 
} 