package com.project.system2.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("act_process_reviewer")
public class ProcessReviewer {
    
    /**
     * 流程定义ID
     */
    private String processDefinitionId;
    
    /**
     * 审核员ID
     */
    private String reviewerId;
} 