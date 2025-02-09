package com.project.system2.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.util.Date;

@Data
@TableName("act_task")
public class ActTask extends BaseEntity {
    
    @TableId
    private String id;
    
    private String name;
    
    private String processInstanceId;
    
    private String processDefinitionId;
    
    private String taskDefinitionKey;
    
    private String description;
    
    private String assignee;
    
    private String owner;
    
    private Date dueDate;
    
    private Integer priority;
    
    private String category;
    
    private String formKey;
    
    private String tenantId;
    
    private String parentTaskId;
    
    private Boolean suspended;
    
    private String businessKey;
    
    private String executionId;
} 