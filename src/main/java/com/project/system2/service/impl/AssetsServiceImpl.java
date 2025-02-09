package com.project.system2.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.system2.common.core.domain.Result;
import com.project.system2.domain.entity.Assets;
import com.project.system2.domain.model.AssetsQuery;
import com.project.system2.mapper.AssetsMapper;
import com.project.system2.service.IAssetsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AssetsServiceImpl implements IAssetsService {

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
        wrapper.like(StringUtils.isNotBlank(query.getAssetName()), 
                    Assets::getAssetName, query.getAssetName());
        // 添加资产分类查询条件                
        wrapper.eq(query.getClassificationId() != null,
                  Assets::getAssetClassificationId, query.getClassificationId());
        
        // 按创建时间降序排序              
        wrapper.orderByDesc(Assets::getCreateTime);
        
        IPage<Assets> pageResult = assetsMapper.selectPage(page, wrapper);
        return Result.success(pageResult);
    }
} 