package com.project.system2.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.project.system2.common.core.domain.Result;
import com.project.system2.domain.entity.AssetClassification;
import com.project.system2.domain.model.AssetClassificationQuery;

import java.util.List;

public interface IAssetClassificationService {
    /**
     * 分页查询资产分类
     */
    Result<IPage<AssetClassification>> queryList(AssetClassificationQuery query);

    /**
     * 根据ID获取资产分类
     */
    Result<AssetClassification> getById(Long id);

    /**
     * 新增资产分类
     */
    Result<Boolean> add(AssetClassification classification);

    /**
     * 修改资产分类
     */
    Result<Boolean> update(AssetClassification classification);

    /**
     * 删除资产分类
     */
    Result<Boolean> deleteById(Long id);

    /**
     * 获取列表无分页
     * @return
     */
    Result<List<AssetClassification>> getList();

}