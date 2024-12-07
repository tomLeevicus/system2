package com.project.system2.domain.entity;

import lombok.Data;
import java.util.Date;

@Data
public class ActProcessInstance {
    
    private String id;
    
    private String processDefinitionId;
    
    private String processDefinitionKey;
    
    private String processDefinitionName;
    
    private Integer processDefinitionVersion;
    
    private String deploymentId;
    
    private String businessKey;
    
    private Boolean suspended;
    
    private Date startTime;
    
    private String startUserId;
    
    private String tenantId;
    
    private String name;
    
    private String description;
} 