package com.project.system2.domain.model;

import lombok.Data;
import java.util.Date;

@Data
public class AssetsQuery {
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
     * 资产分类ID
     */
    private Long classificationId;

    // 资产编号
    private String assetNumber;
    
    // 资产型号
    private String assetModel;
    
    // 购买时间范围
    private Date assetPurchaseTimeStart;
    private Date assetPurchaseTimeEnd;
    
    // 启用时间范围
    private Date assetUseTimeStart;
    private Date assetUseTimeEnd;
} 