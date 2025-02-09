package com.project.system2.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.system2.common.core.domain.Result;
import com.project.system2.common.core.utils.SecurityUtils;
import com.project.system2.domain.entity.AssetRepair;
import com.project.system2.domain.model.AssetRepairQuery;
import com.project.system2.mapper.AssetRepairMapper;
import com.project.system2.service.IAssetRepairService;
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
public class AssetRepairServiceImpl implements IAssetRepairService {
    @Autowired
    private AssetRepairMapper assetRepairMapper;

    @Override
    public Result<IPage<AssetRepair>> queryList(AssetRepairQuery query) {
        Page<AssetRepair> page = new Page<>(query.getPageNum(), query.getPageSize());
        
        IPage<AssetRepair> pageResult = assetRepairMapper.selectRepairPage(page, query);
        
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
        // 软删除
        AssetRepair assetRepair = new AssetRepair();
        assetRepair.setId(id);
        assetRepair.setDelFlag(1);
        assetRepair.setUpdateTime(new Date());
        assetRepair.setUpdateBy(SecurityUtils.getUserId());
        int rows = assetRepairMapper.updateById(assetRepair);
        return Result.success(rows > 0);
    }
}
