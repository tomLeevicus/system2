package com.project.system2.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import java.util.Date;

@Data
@TableName("sys_process_reviewer")
public class ProcessReviewer {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 流程定义ID
     */
    private String processDefinitionId;
    
    /**
     * 审核员ID
     */
    private Long reviewerId;
    
    private Date createTime;
} 