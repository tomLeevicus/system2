package com.project.system2.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.system2.common.core.domain.Result;
import com.project.system2.domain.entity.AssetStorage;
import com.project.system2.domain.model.AssetStorageQuery;
import com.project.system2.mapper.AssetStorageMapper;
import com.project.system2.service.IAssetStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.lang3.StringUtils;
import com.project.system2.common.core.utils.EntityUtils;

@Service
public class AssetStorageServiceImpl implements IAssetStorageService {

    @Autowired
    private AssetStorageMapper assetStorageMapper;

    @Override
    public Result<IPage<AssetStorage>> queryList(AssetStorageQuery query) {
        Page<AssetStorage> page = new Page<>(query.getPageNum(), query.getPageSize());
        
        LambdaQueryWrapper<AssetStorage> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(query.getAssetName()),
                    AssetStorage::getAssetName, query.getAssetName())
               .like(StringUtils.isNotBlank(query.getOperatorName()),
                    AssetStorage::getOperatorName, query.getOperatorName())
               .ge(query.getWarehouseTimeStart() != null,
                    AssetStorage::getWarehouseTime, query.getWarehouseTimeStart())
               .le(query.getWarehouseTimeEnd() != null,
                    AssetStorage::getWarehouseTime, query.getWarehouseTimeEnd())
               .orderByDesc(AssetStorage::getCreateTime);
        
        IPage<AssetStorage> pageResult = assetStorageMapper.selectPage(page, wrapper);
        return Result.success(pageResult);
    }

    @Override
    public Result<AssetStorage> getById(Long id) {
        AssetStorage assetStorage = assetStorageMapper.selectById(id);
        return Result.success(assetStorage);
    }

    @Override
    @Transactional
    public Result<Boolean> add(AssetStorage assetStorage) {
        EntityUtils.setCreateAndUpdateInfo(assetStorage, true);
        int rows = assetStorageMapper.insert(assetStorage);
        return Result.success(rows > 0);
    }

    @Override
    @Transactional
    public Result<Boolean> update(AssetStorage assetStorage) {
        EntityUtils.setCreateAndUpdateInfo(assetStorage, false);
        int rows = assetStorageMapper.updateById(assetStorage);
        return Result.success(rows > 0);
    }

    @Override
    @Transactional
    public Result<Boolean> deleteById(Long id) {
        int rows = assetStorageMapper.deleteById(id);
        return Result.success(rows > 0);
    }
} 