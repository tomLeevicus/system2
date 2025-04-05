package com.project.system2.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.project.system2.common.core.domain.Result;
import com.project.system2.common.core.utils.EntityUtils;
import com.project.system2.domain.entity.AssetClassification;
import com.project.system2.domain.query.AssetClassificationQuery;
import com.project.system2.mapper.AssetClassificationMapper;
import com.project.system2.service.IAssetClassificationService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AssetClassificationServiceImpl extends ServiceImpl<AssetClassificationMapper, AssetClassification> 
    implements IAssetClassificationService {

    @Autowired
    private AssetClassificationMapper assetClassificationMapper;


    @Override
    public Result<IPage<AssetClassification>> queryList(AssetClassificationQuery query) {
        Page<AssetClassification> page = new Page<>(query.getPageNum(), query.getPageSize());
        
        LambdaQueryWrapper<AssetClassification> wrapper = new LambdaQueryWrapper<>();
        
        // 添加分类名称查询条件
        if (StringUtils.isNotBlank(query.getCategoryName())) {
            wrapper.like(AssetClassification::getCategoryName, query.getCategoryName());
        }
        
        // 添加排序号查询条件
        if (query.getSortNum() != null) {
            wrapper.eq(AssetClassification::getSortNum, query.getSortNum());
        }
        
        // 添加排序条件
        if (StringUtils.isNotBlank(query.getOrderByColumn())) {
            // 这里需要根据实际的排序字段做映射处理
            // 简单示例:
            if ("sortNum".equals(query.getOrderByColumn())) {
                wrapper.orderBy(true, "asc".equalsIgnoreCase(query.getIsAsc()), 
                              AssetClassification::getSortNum);
            }
        } else {
            // 默认排序
            wrapper.orderByDesc(AssetClassification::getSortNum);
        }
        
        // 执行查询
        IPage<AssetClassification> resultPage = page(page, wrapper);
        
        // 统计每个分类下的资产数量
        resultPage.getRecords().forEach(classification -> {
            Long count = baseMapper.countAssetsByClassificationId(classification.getId());
            classification.setAssetCount(count != null ? count.intValue() : 0);
        });
        
        return Result.success(resultPage);
    }

    @Override
    public Result<AssetClassification> getById(Long id) {
        return Result.success(baseMapper.selectById(id));
    }

    @Override
    @Transactional
    public Result<Boolean> add(AssetClassification classification) {
        EntityUtils.setCreateAndUpdateInfo(classification, true);
        return Result.success(save(classification));
    }

    @Override
    @Transactional
    public Result<Boolean> update(AssetClassification classification) {
        EntityUtils.setCreateAndUpdateInfo(classification, false);
        return Result.success(updateById(classification));
    }

    @Override
    @Transactional
    public Result<Boolean> deleteById(Long id) {
        // 检查是否存在关联资产
        int count = baseMapper.checkAssetsExist(id);
        if (count > 0) {
            return Result.error("该分类下存在关联资产，无法删除");
        }
        
        return Result.success(removeById(id));
    }

    @Override
    public Result<List<AssetClassification>> getList() {
        return Result.success(list(new QueryWrapper<AssetClassification>()
            .eq("del_flag", 0)
            .select("category_name", "id")));
    }
}