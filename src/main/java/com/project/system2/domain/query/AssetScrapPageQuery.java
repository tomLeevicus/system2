package com.project.system2.domain.query;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
@Schema(description = "资产报废分页查询参数")
public class AssetScrapPageQuery {
    @Min(value = 1, message = "页码不能小于1")
    @Schema(description = "页码", example = "1")
    private Integer pageNum = 1;
    
    @Min(value = 1, message = "每页数量不能小于1")
    @Schema(description = "每页数量", example = "10")
    private Integer pageSize = 10;
    
    @Schema(description = "资产名称")
    private String assetName;
    
    @Schema(description = "开始时间（yyyy-MM-dd）")
    private String startTime;
    
    @Schema(description = "结束时间（yyyy-MM-dd）")
    private String endTime;
} 