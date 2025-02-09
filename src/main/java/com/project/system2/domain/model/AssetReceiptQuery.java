package com.project.system2.domain.model;

import lombok.Data;
import java.util.Date;

@Data
public class AssetReceiptQuery {
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
     * 领用人名称
     */
    private String receiverName;

    /**
     * 领用日期范围-开始
     */
    private Date collectionDateStart;

    /**
     * 领用日期范围-结束
     */
    private Date collectionDateEnd;

    /**
     * 是否长期领用
     */
    private Integer isLongTermUse;

    /**
     * 归还状态
     */
    private Integer returnStatus;

    /**
     * 审核状态
     */
    private Integer reviewStatus;
} 