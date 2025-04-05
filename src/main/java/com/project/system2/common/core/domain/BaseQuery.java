package com.project.system2.common.core.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 基础查询对象
 * 包含分页参数
 */
@Data
@Schema(description = "基础查询对象")
public class BaseQuery {

    /**
     * 当前页码
     */
    @Schema(description = "当前页码", example = "1")
    private Integer pageNum = 1;

    /**
     * 每页数量
     */
    @Schema(description = "每页数量", example = "10")
    private Integer pageSize = 10;
    
    /**
     * 排序字段
     */
    @Schema(description = "排序字段")
    private String orderByColumn;

    /**
     * 排序方向 "asc" 或 "desc"
     */
    @Schema(description = "排序方向", example = "asc", allowableValues = {"asc", "desc"})
    private String isAsc;
} 