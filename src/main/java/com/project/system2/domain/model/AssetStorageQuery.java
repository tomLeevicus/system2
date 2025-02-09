package com.project.system2.domain.model;

import lombok.Data;
import java.util.Date;

@Data
public class AssetStorageQuery {
    /**
     * 当前页码
     */
    private Integer pageNum = 1;

    /**
     * 每页数量
     */
    private Integer pageSize = 10;

    /**
     * 资产名称
     */
    private String assetName;

    /**
     * 操作者名称
     */
    private String operatorName;

    /**
     * 入库时间范围-开始
     */
    private Date warehouseTimeStart;

    /**
     * 入库时间范围-结束
     */
    private Date warehouseTimeEnd;
} 