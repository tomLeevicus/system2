package com.project.system2.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.project.system2.common.core.domain.Result;
import com.project.system2.domain.entity.AssetReceipt;
import com.project.system2.domain.dto.PersonalAssetDTO;
import com.project.system2.domain.query.AssetReceiptQuery;
import com.project.system2.domain.query.AssetReceiptRecordQuery;

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

    /**
     * 获取当前用户的资产信息及领用信息
     */
    Result<IPage<PersonalAssetDTO>> getPersonalAssets(Long userId, int pageNum, int pageSize);

    Result<Boolean> approveReceipt(AssetReceiptRecordQuery assetReceiptRecordQuery);
}