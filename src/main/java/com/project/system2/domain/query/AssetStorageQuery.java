package com.project.system2.domain.query;

import com.project.system2.common.core.domain.BaseQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "资产入库查询对象")
public class AssetStorageQuery extends BaseQuery {

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
     * 操作者名称
     */
    @Schema(description = "操作人姓名", example = "张三")
    private String operatorName;

    /**
     * 入库时间范围 - 开始
     */
    @Schema(description = "入库时间范围 - 开始", example = "2023-03-01")
    private Date warehouseTimeStart;

    /**
     * 入库时间范围 - 结束
     */
    @Schema(description = "入库时间范围 - 结束", example = "2023-03-31")
    private Date warehouseTimeEnd;
} 