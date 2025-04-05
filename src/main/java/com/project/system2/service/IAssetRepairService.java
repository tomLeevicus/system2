package com.project.system2.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.project.system2.common.core.domain.Result;
import com.project.system2.domain.entity.AssetRepair;
import com.project.system2.domain.query.AssetRepairQuery;

public interface IAssetRepairService {
    /**
     * 分页查询维修记录
     */
    Result<IPage<AssetRepair>> queryList(AssetRepairQuery query);

    /**
     * 根据ID获取维修记录
     */
    Result<AssetRepair> getById(Long id);

    /**
     * 新增维修记录
     */
    Result<Boolean> add(AssetRepair assetRepair);

    /**
     * 修改维修记录
     */
    Result<Boolean> update(AssetRepair assetRepair);

    /**
     * 删除维修记录
     */
    Result<Boolean> deleteById(Long id);
}
