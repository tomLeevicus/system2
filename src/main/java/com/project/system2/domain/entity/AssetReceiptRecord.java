package com.project.system2.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
@TableName(value = "asset_receipt_record")
public class AssetReceiptRecord {

    private Long id;
    private Long receiptId;
    private Long assetId;
    private Long recipientId;
    private Long approverId;
    private Date approvalTime;
    private Date applyTime;
    private int status;
    private String remark;
    @TableField("del_flag")
    @Schema(description = "逻辑删除标志（0-存在，1-删除）", example = "0")
    private int delFlag;
}
