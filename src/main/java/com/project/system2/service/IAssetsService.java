package com.project.system2.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.project.system2.domain.entity.Assets;
import com.project.system2.common.core.domain.Result;
import com.project.system2.domain.query.AssetsQuery;

import java.util.List;
import java.util.Map;

/**
 * 资产管理 服务层
 */
public interface IAssetsService {

    /**
     * 根据ID查询资产
     * @param id 资产ID
     */
    Result<Assets> getAssetById(Long id);

    /**
     * 新增资产
     * @param asset 资产信息
     */
    Result<Boolean> addAsset(Assets asset);

    /**
     * 修改资产
     * @param asset 资产信息
     */
    Result<Boolean> updateAsset(Assets asset);

    /**
     * 删除资产
     * @param id 资产ID
     */
    Result<Boolean> deleteAsset(Long id);

    /**
     * 条件查询资产列表
     * @param query 查询条件
     * @return 分页查询结果
     */
    Result<IPage<Assets>> queryAssetsList(AssetsQuery query);

    /**
     * 按使用状态统计资产数量
     * @return Map<String, Long> 状态 -> 数量
     */
    Map<String, Long> getAssetStatisticsByStatus();

    /**
     * 批量保存资产信息
     * @param assetsList 资产列表
     * @return 是否成功
     */
    boolean saveBatchAssets(List<Assets> assetsList);
} 