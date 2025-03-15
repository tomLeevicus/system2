package com.project.system2.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.util.Date;

@Data
@TableName("sys_process_reviewer")
@Schema(description = "流程审批人实体")
public class ProcessReviewer {
    
    @TableId(type = IdType.AUTO)
    @Schema(description = "主键ID", example = "1")
    private Long id;
    
    /**
     * 流程定义ID
     */
    @Schema(description = "流程定义ID", example = "PD_ASSET_PURCHASE:1:001")
    private String processDefinitionId;
    
    /**
     * 审核员ID
     */
    @Schema(description = "审核员用户ID", example = "1001")
    private Long reviewerId;
    
    @Schema(description = "创建时间", example = "2023-01-01 12:00:00")
    private Date createTime;
} 