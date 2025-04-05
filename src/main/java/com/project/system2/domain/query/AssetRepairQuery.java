package com.project.system2.domain.query;

import com.project.system2.common.core.domain.BaseQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "资产维修查询对象")
public class AssetRepairQuery extends BaseQuery {

    /**
     * 资产ID
     */
    @Schema(description = "关联资产ID", example = "1001")
    private Long assetId;

    /**
     * 资产名称
     */
    @Schema(description = "资产名称", example = "联想笔记本电脑")
    private String assetName;

    /**
     * 维修原因
     */
    @Schema(description = "维修原因", example = "屏幕损坏需要更换")
    private String reasonForRepair;

    /**
     * 维修日期范围 - 开始
     */
    @Schema(description = "维修日期范围 - 开始", example = "2023-04-01")
    private LocalDate repairDateStart;

    /**
     * 维修日期范围 - 结束
     */
    @Schema(description = "维修日期范围 - 结束", example = "2023-05-01")
    private LocalDate repairDateEnd;

    /**
     * 维修状态（0-未修理，1-维修完成）
     */
    @Schema(description = "维修状态（0-未修理，1-维修完成）", example = "1")
    private Integer status;
} 