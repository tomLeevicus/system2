package com.project.system2.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.project.system2.common.core.domain.Result;
import com.project.system2.domain.entity.Assets;
import com.project.system2.domain.query.AssetsQuery;
import com.project.system2.mapper.AssetsMapper;
import com.project.system2.service.IAssetsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.HashMap;
import java.util.Arrays;

@Service
public class AssetsServiceImpl extends ServiceImpl<AssetsMapper, Assets> implements IAssetsService {

    @Autowired
    private AssetsMapper assetsMapper;

    @Override
    public Result<Assets> getAssetById(Long id) {
        Assets asset = assetsMapper.selectById(id);
        return Result.success(asset);
    }

    @Override
    public Result<Boolean> addAsset(Assets asset) {
        int rows = assetsMapper.insert(asset);
        return Result.success(rows > 0);
    }

    @Override
    public Result<Boolean> updateAsset(Assets asset) {
        int rows = assetsMapper.updateById(asset);
        return Result.success(rows > 0);
    }

    @Override
    public Result<Boolean> deleteAsset(Long id) {
        int rows = assetsMapper.deleteById(id);
        return Result.success(rows > 0);
    }

    @Override
    public Result<IPage<Assets>> queryAssetsList(AssetsQuery query) {
        Page<Assets> page = new Page<>(query.getPageNum(), query.getPageSize());
        
        LambdaQueryWrapper<Assets> wrapper = new LambdaQueryWrapper<>();
        
        // 添加资产名称查询条件
        if (StringUtils.isNotBlank(query.getAssetName())) {
            wrapper.like(Assets::getAssetName, query.getAssetName());
        }
        
        // 添加资产分类ID查询条件
        if (query.getAssetClassificationId() != null) {
            wrapper.eq(Assets::getAssetClassificationId, query.getAssetClassificationId());
        }
        
        // 添加资产型号查询条件
        if (StringUtils.isNotBlank(query.getAssetModel())) {
            wrapper.like(Assets::getAssetModel, query.getAssetModel());
        }
        
        // 添加资产使用状态查询条件
        if (query.getAssetUseStatus() != null) {
            wrapper.eq(Assets::getAssetUseStatus, query.getAssetUseStatus());
        }
        
        // 添加资产状态查询条件
        if (query.getAssetStatus() != null) {
            wrapper.eq(Assets::getAssetStatus, query.getAssetStatus());
        }
        
        // 添加使用部门ID查询条件
        if (query.getAssetUseDepartmentId() != null) {
            wrapper.eq(Assets::getAssetUseDepartmentId, query.getAssetUseDepartmentId());
        }
        
        // 添加负责人ID查询条件
        if (query.getAssetUserId() != null) {
            wrapper.eq(Assets::getAssetUserId, query.getAssetUserId());
        }
        
        // 添加存放位置查询条件
        if (StringUtils.isNotBlank(query.getAssetStorageLocation())) {
            wrapper.like(Assets::getAssetStorageLocation, query.getAssetStorageLocation());
        }
        
        // 添加排序条件
        if (StringUtils.isNotBlank(query.getOrderByColumn())) {
            // 这里需要根据实际的排序字段做映射处理
            // 简单示例:
            if ("createTime".equals(query.getOrderByColumn())) {
                wrapper.orderBy(true, "asc".equalsIgnoreCase(query.getIsAsc()), Assets::getCreateTime);
            }
        } else {
            // 默认排序
            wrapper.orderByDesc(Assets::getCreateTime);
        }
        
        IPage<Assets> pageResult = page(page, wrapper);
        return Result.success(pageResult);
    }

    @Override
    public Map<String, Long> getAssetStatisticsByStatus() {
        // Define all possible statuses
        List<String> allStatuses = Arrays.asList("闲置", "在用", "维修", "报废", "未知"); 
        // Initialize the result map with all statuses and count 0
        Map<String, Long> resultMap = new HashMap<>();
        for (String status : allStatuses) {
            resultMap.put(status, 0L);
        }

        // Query assets and get actual counts
        List<Assets> allAssets = assetsMapper.selectList(
            new LambdaQueryWrapper<Assets>().select(Assets::getAssetUseStatus)
        );

        // Group by status and count existing assets
        Map<String, Long> actualCounts = allAssets.stream()
                .collect(Collectors.groupingBy(
                    asset -> mapStatusToString(asset.getAssetUseStatus()), 
                    Collectors.counting()
                ));

        // Merge actual counts into the result map
        resultMap.putAll(actualCounts);

        return resultMap;
    }

    // Helper method to convert status code to String (replace with actual logic)
    private String mapStatusToString(Integer status) {
        if (status == null) return "未知";
        switch (status) {
            case 0: return "闲置"; // Example mapping
            case 1: return "在用"; // Example mapping
            case 2: return "维修"; // Example mapping
            case 3: return "报废"; // Example mapping
            // Add other status codes as needed
            default: return "状态[" + status + "]";
        }
    }
} 