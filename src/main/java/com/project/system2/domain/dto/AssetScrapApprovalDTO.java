package com.project.system2.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "资产报废审批请求参数")
public class AssetScrapApprovalDTO {
    @Schema(description = "报废记录ID", example = "1", required = true)
    private Long scrapRecordId;
    
    @Schema(description = "是否同意（0-拒绝，1-同意）", example = "1", required = true)
    private Integer isAgreed;
    
    @Schema(description = "审批意见", example = "符合报废条件")
    private String approvalComment;
} 