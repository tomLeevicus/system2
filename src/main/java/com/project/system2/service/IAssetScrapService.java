package com.project.system2.service;

import com.project.system2.common.core.domain.Result;
import com.project.system2.domain.entity.AssetScrap;

public interface IAssetScrapService {
    // 其他方法...

    /**
     * 处理资产报废
     */
    Result<Boolean> scrapAsset(AssetScrap assetScrap);
} 