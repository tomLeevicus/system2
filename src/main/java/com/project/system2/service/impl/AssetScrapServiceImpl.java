package com.project.system2.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.project.system2.common.core.domain.Result;
import com.project.system2.domain.entity.AssetScrap;
import com.project.system2.domain.entity.Assets;
import com.project.system2.mapper.AssetScrapMapper;
import com.project.system2.mapper.AssetsMapper;
import com.project.system2.service.IAssetScrapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AssetScrapServiceImpl implements IAssetScrapService {

    @Autowired
    private AssetScrapMapper assetScrapMapper;

    @Autowired
    private AssetsMapper assetsMapper;

    @Override
    public Result<Boolean> scrapAsset(AssetScrap assetScrap) {
        // 处理资产报废的逻辑
        // 例如，更新资产状态，记录报废信息等
        int rows = assetScrapMapper.insert(assetScrap);// 假设有一个Mapper来处理报废记录
        if (rows >0){
            //更新对应资产信息asset_use_status 为3
            assetsMapper.updateAssetUseStatus(assetScrap.getAssetId());
        }
        return Result.success(rows > 0);
    }
} 