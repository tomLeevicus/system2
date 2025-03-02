package com.project.system2.domain.query;

import lombok.Data;

@Data
public class AssetReceiptRecordQuery {
//    领用记录id
    private Long receiptId;
//  是否同意
    private Boolean isAgree;
//意见
    private String remark;
}
