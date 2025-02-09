package com.project.system2.domain.model;

import lombok.Data;

@Data
public class AssetClassificationQuery {
    /**
     * 当前页码
     */
    private Integer pageNum = 1;

    /**
     * 每页数量
     */
    private Integer pageSize = 10;

    /**
     * 分类名称
     */
    private String categoryName;

    /**
     * 排序号
     */
    private Integer sortNum;
} 