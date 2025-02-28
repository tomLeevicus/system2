package com.project.system2.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
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
}
