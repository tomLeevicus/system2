package com.project.system2.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.project.system2.common.core.domain.Result;
import com.project.system2.domain.entity.AssetStorage;
import com.project.system2.domain.query.AssetStorageQuery;

public interface IAssetStorageService {
    /**
     * 分页查询资产入库记录
     */
    Result<IPage<AssetStorage>> queryList(AssetStorageQuery query);

    /**
     * 获取资产入库记录详情
     */
    Result<AssetStorage> getById(Long id);

    /**
     * 新增资产入库记录
     */
    Result<Boolean> add(AssetStorage assetStorage);

    /**
     * 修改资产入库记录
     */
    Result<Boolean> update(AssetStorage assetStorage);

    /**
     * 删除资产入库记录
     */
    Result<Boolean> deleteById(Long id);
} 