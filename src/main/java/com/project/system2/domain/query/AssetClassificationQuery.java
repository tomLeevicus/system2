package com.project.system2.domain.query;

import com.project.system2.common.core.domain.BaseQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "资产分类查询对象")
public class AssetClassificationQuery extends BaseQuery {

    /**
     * 分类名称
     */
    @Schema(description = "分类名称", example = "电子设备")
    private String categoryName;

    /**
     * 排序号
     */
    @Schema(description = "排序号", example = "1")
    private Integer sortNum;
} 