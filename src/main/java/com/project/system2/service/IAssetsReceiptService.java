package com.project.system2.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.project.system2.common.core.domain.Result;
import com.project.system2.domain.entity.AssetReceipt;
import com.project.system2.domain.model.AssetReceiptQuery;

public interface IAssetsReceiptService {
    /**
     * 分页查询资产领用记录
     */
    Result<IPage<AssetReceipt>> queryList(AssetReceiptQuery query);

    /**
     * 获取资产领用记录详情
     */
    Result<AssetReceipt> getById(Long id);

    /**
     * 新增资产领用记录
     */
    Result<Boolean> add(AssetReceipt assetsReceipt);

    /**
     * 修改资产领用记录
     */
    Result<Boolean> update(AssetReceipt assetsReceipt);

    /**
     * 删除资产领用记录
     */
    Result<Boolean> deleteById(Long id);
} 