package com.project.system2.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.project.system2.common.core.domain.Result;
import com.project.system2.domain.entity.Assets;
import com.project.system2.domain.model.AssetsQuery;
import com.project.system2.mapper.AssetsMapper;
import com.project.system2.service.IAssetsService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AssetsServiceImpl extends ServiceImpl<AssetsMapper, Assets> implements IAssetsService {

    @Override
    public Result<List<Assets>> listAssets() {
        List<Assets> list = this.list();
        return Result.success(list);
    }

    @Override
    public Result<Assets> getAssetById(Long id) {
        Assets asset = this.getById(id);
        return Result.success(asset);
    }

    @Override
    public Result<Boolean> addAsset(Assets asset) {
        boolean success = this.save(asset);
        return Result.success(success);
    }

    @Override
    public Result<Boolean> updateAsset(Assets asset) {
        boolean success = this.updateById(asset);
        return Result.success(success);
    }

    @Override
    public Result<Boolean> deleteAsset(Long id) {
        boolean success = this.removeById(id);
        return Result.success(success);
    }

    @Override
    public Result<IPage<Assets>> queryAssetsList(AssetsQuery query) {
        Page<Assets> page = new Page<>(query.getPageNum(), query.getPageSize());
        IPage<Assets> pageResult = baseMapper.selectAssetsList(page, query);
        return Result.success(pageResult);
    }
} 