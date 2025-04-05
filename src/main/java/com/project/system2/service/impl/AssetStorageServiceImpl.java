package com.project.system2.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.project.system2.common.core.domain.Result;
import com.project.system2.domain.entity.AssetStorage;
import com.project.system2.domain.query.AssetStorageQuery;
import com.project.system2.mapper.AssetStorageMapper;
import com.project.system2.service.IAssetStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.lang3.StringUtils;
import com.project.system2.common.core.utils.EntityUtils;

@Service
public class AssetStorageServiceImpl extends ServiceImpl<AssetStorageMapper,AssetStorage> implements IAssetStorageService {

    @Autowired
    private AssetStorageMapper assetStorageMapper;

    @Override
    public Result<IPage<AssetStorage>> queryList(AssetStorageQuery query) {
        Page<AssetStorage> page = new Page<>(query.getPageNum(), query.getPageSize());
        
        LambdaQueryWrapper<AssetStorage> wrapper = new LambdaQueryWrapper<>();
        
        // 添加资产ID查询条件
        if (query.getAssetId() != null) {
            wrapper.eq(AssetStorage::getAssetId, query.getAssetId());
        }
        
        // 添加资产名称查询条件
        if (StringUtils.isNotBlank(query.getAssetName())) {
            wrapper.like(AssetStorage::getAssetName, query.getAssetName());
        }
        
        // 添加操作者名称查询条件
        if (StringUtils.isNotBlank(query.getOperatorName())) {
            wrapper.like(AssetStorage::getOperatorName, query.getOperatorName());
        }
        
        // 添加入库时间范围查询条件
        if (query.getWarehouseTimeStart() != null && query.getWarehouseTimeEnd() != null) {
            wrapper.between(AssetStorage::getWarehouseTime, 
                           query.getWarehouseTimeStart(), 
                           query.getWarehouseTimeEnd());
        }
        
        // 添加排序条件
        if (StringUtils.isNotBlank(query.getOrderByColumn())) {
            // 这里需要根据实际的排序字段做映射处理
            // 简单示例:
            if ("warehouseTime".equals(query.getOrderByColumn())) {
                wrapper.orderBy(true, "asc".equalsIgnoreCase(query.getIsAsc()), 
                              AssetStorage::getWarehouseTime);
            }
        } else {
            // 默认排序
            wrapper.orderByDesc(AssetStorage::getWarehouseTime);
        }
        
        return Result.success(page(page, wrapper));
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