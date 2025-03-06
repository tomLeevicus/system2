package com.project.system2.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.project.system2.common.core.domain.Result;
import com.project.system2.domain.entity.AssetScrap;
import com.project.system2.domain.dto.AssetScrapApprovalDTO;
import com.project.system2.domain.entity.AssetScrapRecord;
import com.project.system2.domain.query.AssetScrapPageQuery;

public interface IAssetScrapService {
    // 其他方法...

    /**
     * 处理资产报废
     */
    Result<Boolean> scrapAsset(AssetScrap assetScrap);

    Result<Boolean> approveScrap(AssetScrapApprovalDTO dto);

    Result<IPage<AssetScrap>> pageScrapRecords(AssetScrapPageQuery query);
} 