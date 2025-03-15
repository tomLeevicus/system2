package com.project.system2.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
@TableName(value = "asset_receipt_record")
@Schema(description = "资产领用审批记录")
public class AssetReceiptRecord {

    @Schema(description = "记录ID", example = "1")
    private Long id;
    
    @Schema(description = "领用单ID", example = "600")
    private Long receiptId;
    
    @Schema(description = "资产ID", example = "1001")
    private Long assetId;
    
    @Schema(description = "申请人ID", example = "2001")
    private Long recipientId;
    
    @Schema(description = "审批人ID", example = "3001")
    private Long approverId;
    
    @Schema(description = "审批时间", example = "2023-04-05 10:30:00")
    private Date approvalTime;
    
    @Schema(description = "申请时间", example = "2023-04-01 09:15:00")
    private Date applyTime;
    
    @Schema(description = "审批状态（0-待审批, 1-已通过, 2-已拒绝）", example = "1")
    private int status;
    
    @Schema(description = "备注信息", example = "部门工作需要")
    private String remark;
    
    @TableField("del_flag")
    @Schema(description = "逻辑删除标志（0-存在，1-删除）", example = "0")
    private int delFlag;
}
