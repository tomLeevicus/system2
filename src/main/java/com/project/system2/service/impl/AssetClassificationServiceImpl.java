package com.project.system2.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.system2.common.core.domain.Result;
import com.project.system2.common.core.utils.EntityUtils;
import com.project.system2.domain.entity.AssetClassification;
import com.project.system2.domain.model.AssetClassificationQuery;
import com.project.system2.mapper.AssetClassificationMapper;
import com.project.system2.service.IAssetClassificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

@Service
public class AssetClassificationServiceImpl implements IAssetClassificationService {

    @Autowired
    private AssetClassificationMapper assetClassificationMapper;

    @Override
    public Result<IPage<AssetClassification>> queryList(AssetClassificationQuery query) {
        Page<AssetClassification> page = new Page<>(query.getPageNum(), query.getPageSize());
        
        // 转换查询条件
        AssetClassification classification = new AssetClassification();
        classification.setCategoryName(query.getCategoryName());
        
        // 使用新的分页查询方法
        IPage<AssetClassification> pageResult = assetClassificationMapper.selectClassificationPage(page, classification);
        return Result.success(pageResult);
    }

    @Override
    public Result<AssetClassification> getById(Long id) {
        AssetClassification classification = assetClassificationMapper.selectById(id);
        return Result.success(classification);
    }

    @Override
    @Transactional
    public Result<Boolean> add(AssetClassification classification) {
        EntityUtils.setCreateAndUpdateInfo(classification,true);
        int rows = assetClassificationMapper.insert(classification);
        return Result.success(rows > 0);
    }

    @Override
    @Transactional
    public Result<Boolean> update(AssetClassification classification) {
        EntityUtils.setCreateAndUpdateInfo(classification,false);
        int rows = assetClassificationMapper.updateById(classification);
        return Result.success(rows > 0);
    }

    @Override
    @Transactional
    public Result<Boolean> deleteById(Long id) {
        // 检查是否存在关联资产
        int count = assetClassificationMapper.checkAssetsExist(id);
        if (count > 0) {
            return Result.error("该分类下存在关联资产，无法删除");
        }
        
        // 执行软删除
        int rows = assetClassificationMapper.deleteById(id);
        return Result.success(rows > 0);
    }

    @Override
    public Result<List<AssetClassification>> getList() {
        return Result.success(assetClassificationMapper.selectList(new QueryWrapper<AssetClassification>().eq("del_flag", 0).select("category_name", "id")));
    }
}