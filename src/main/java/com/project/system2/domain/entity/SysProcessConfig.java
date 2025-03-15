package com.project.system2.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_process_config")
@Schema(description = "流程配置实体")
public class SysProcessConfig {
    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "主键ID", example = "1")
    private Long id;
    
    /**
     * 流程标识
     */
    @Schema(description = "流程标识", example = "asset_purchase")
    private String processKey;
    
    /**
     * 流程名称
     */
    @Schema(description = "流程名称", example = "资产采购流程")
    private String name;
    
    /**
     * 流程分类
     */
    @Schema(description = "流程分类", example = "资产管理")
    private String category;
    
    /**
     * 审批角色标识
     */
    @Schema(description = "审批角色标识", example = "asset_approver")
    private String roleKey;
    
    /**
     * 状态（0正常 1停用）
     */
    @Schema(description = "状态（0正常 1停用）", example = "0")
    private String status;
    
    /**
     * 创建时间
     */
    @Schema(description = "创建时间", example = "2023-01-01 12:00:00")
    private Date createTime;
    
    /**
     * 更新时间
     */
    @Schema(description = "更新时间", example = "2023-01-15 14:30:00")
    private Date updateTime;
} 