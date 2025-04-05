package com.project.system2.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.project.system2.common.core.domain.Result;
import com.project.system2.common.core.utils.SecurityUtils;
import com.project.system2.domain.entity.AssetRepair;
import com.project.system2.domain.query.AssetRepairQuery;
import com.project.system2.mapper.AssetRepairMapper;
import com.project.system2.service.IAssetRepairService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @Author: BlueberryLee
 * @Date: 2025/1/27 13:23
 * @Description: TODO
 */
@Service
public class AssetRepairServiceImpl extends ServiceImpl<AssetRepairMapper, AssetRepair> implements IAssetRepairService {
    @Autowired
    private AssetRepairMapper assetRepairMapper;

    @Override
    public Result<IPage<AssetRepair>> queryList(AssetRepairQuery query) {
        Page<AssetRepair> page = new Page<>(query.getPageNum(), query.getPageSize());
        
        LambdaQueryWrapper<AssetRepair> wrapper = new LambdaQueryWrapper<>();
        
        // 添加资产ID查询条件
        if (query.getAssetId() != null) {
            wrapper.eq(AssetRepair::getAssetId, query.getAssetId());
        }
        
        // 添加资产名称查询条件
        if (StringUtils.isNotBlank(query.getAssetName())) {
            wrapper.like(AssetRepair::getAssetName, query.getAssetName());
        }
        
        // 添加维修原因查询条件
        if (StringUtils.isNotBlank(query.getReasonForRepair())) {
            wrapper.like(AssetRepair::getReasonForRepair, query.getReasonForRepair());
        }
        
        // 添加维修日期范围查询条件
        if (query.getRepairDateStart() != null && query.getRepairDateEnd() != null) {
            wrapper.between(AssetRepair::getRepairDate, 
                           query.getRepairDateStart(), 
                           query.getRepairDateEnd());
        }
        
        // 添加维修状态查询条件
        if (query.getStatus() != null) {
            wrapper.eq(AssetRepair::getStatus, query.getStatus());
        }
        
        // 添加排序条件
        if (StringUtils.isNotBlank(query.getOrderByColumn())) {
            // 这里需要根据实际的排序字段做映射处理
            // 简单示例:
            if ("repairDate".equals(query.getOrderByColumn())) {
                wrapper.orderBy(true, "asc".equalsIgnoreCase(query.getIsAsc()), 
                              AssetRepair::getRepairDate);
            }
        } else {
            // 默认排序
            wrapper.orderByDesc(AssetRepair::getRepairDate);
        }
        
        IPage<AssetRepair> pageResult = page(page, wrapper);
        return Result.success(pageResult);
    }

    @Override
    public Result<AssetRepair> getById(Long id) {
        AssetRepair assetRepair = assetRepairMapper.selectById(id);
        return Result.success(assetRepair);
    }

    @Override
    @Transactional
    public Result<Boolean> add(AssetRepair assetRepair) {
        assetRepair.setCreateTime(new Date());
        assetRepair.setCreateBy(SecurityUtils.getUserId());
        assetRepair.setDelFlag(0);
        assetRepair.setStatus(0);
        int rows = assetRepairMapper.insert(assetRepair);
        return Result.success(rows > 0);
    }

    @Override
    @Transactional
    public Result<Boolean> update(AssetRepair assetRepair) {
        assetRepair.setUpdateTime(new Date());
        assetRepair.setUpdateBy(SecurityUtils.getUserId());
        int rows = assetRepairMapper.updateById(assetRepair);
        return Result.success(rows > 0);
    }

    @Override
    @Transactional
    public Result<Boolean> deleteById(Long id) {

        return assetRepairMapper.deleteById(id) == 1? Result.success():Result.error();
    }
}
