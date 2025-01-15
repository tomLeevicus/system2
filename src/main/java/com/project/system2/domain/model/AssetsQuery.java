package com.project.system2.domain.model;

import lombok.Data;
import java.util.Date;

@Data
public class AssetsQuery {
    // 资产编号
    private String assetNumber;
    
    // 资产名称
    private String assetName;
    
    // 资产型号
    private String assetModel;
    
    // 购买时间范围
    private Date assetPurchaseTimeStart;
    private Date assetPurchaseTimeEnd;
    
    // 启用时间范围
    private Date assetUseTimeStart;
    private Date assetUseTimeEnd;
    
    // 分页参数
    private Integer pageNum = 1;
    private Integer pageSize = 10;
} 