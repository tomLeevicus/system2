package com.project.system2.domain.query;

import com.project.system2.common.core.domain.BaseQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "资产领用查询对象")
public class AssetReceiptQuery extends BaseQuery {

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
     * 领用人ID
     */
    @Schema(description = "领用人ID", example = "2001")
    private Long receiverId;

    /**
     * 领用人名称
     */
    @Schema(description = "领用人名称", example = "张三")
    private String receiverName;

    /**
     * 领用日期范围 - 开始
     */
    @Schema(description = "领用日期范围 - 开始", example = "2023-04-01")
    private Date collectionDateStart;

    /**
     * 领用日期范围 - 结束
     */
    @Schema(description = "领用日期范围 - 结束", example = "2023-05-01")
    private Date collectionDateEnd;

    /**
     * 是否长期领用（1-是, 0-否）
     */
    @Schema(description = "是否长期领用（1-是, 0-否）", example = "0")
    private Integer isLongTermUse;

    /**
     * 归还状态（0-未归还, 1-已归还）
     */
    @Schema(description = "归还状态（0-未归还, 1-已归还）", example = "0")
    private Integer returnStatus;

    /**
     * 审核状态（0-未审核, 1-通过, 2-拒绝）
     */
    @Schema(description = "审核状态（0-未审核, 1-通过, 2-拒绝）", example = "1")
    private Integer reviewStatus;
} 