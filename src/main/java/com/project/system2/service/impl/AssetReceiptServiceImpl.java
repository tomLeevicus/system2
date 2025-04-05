package com.project.system2.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.project.system2.domain.entity.AssetReceipt;
import com.project.system2.domain.query.AssetReceiptQuery;
import com.project.system2.domain.query.AssetReceiptRecordQuery;
import com.project.system2.mapper.AssetReceiptMapper;
import com.project.system2.service.IAssetReceiptService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class AssetReceiptServiceImpl extends ServiceImpl<AssetReceiptMapper, AssetReceipt> implements IAssetReceiptService {

    @Override
    public IPage<AssetReceipt> queryList(AssetReceiptQuery query) {
        Page<AssetReceipt> page = new Page<>(query.getPageNum(), query.getPageSize());
        
        LambdaQueryWrapper<AssetReceipt> wrapper = new LambdaQueryWrapper<>();
        
        // 添加资产ID查询条件
        if (query.getAssetId() != null) {
            wrapper.eq(AssetReceipt::getAssetId, query.getAssetId());
        }
        
        // 添加资产名称查询条件
        if (StringUtils.isNotBlank(query.getAssetName())) {
            wrapper.like(AssetReceipt::getAssetName, query.getAssetName());
        }
        
        // 添加领用人ID查询条件
        if (query.getReceiverId() != null) {
            wrapper.eq(AssetReceipt::getReceiverId, query.getReceiverId());
        }
        
        // 添加领用人名称查询条件
        if (StringUtils.isNotBlank(query.getReceiverName())) {
            wrapper.like(AssetReceipt::getReceiverName, query.getReceiverName());
        }
        
        // 添加领用日期范围查询条件
        if (query.getCollectionDateStart() != null && query.getCollectionDateEnd() != null) {
            wrapper.between(AssetReceipt::getCollectionDate, 
                           query.getCollectionDateStart(), 
                           query.getCollectionDateEnd());
        }
        
        // 添加是否长期领用查询条件
        if (query.getIsLongTermUse() != null) {
            wrapper.eq(AssetReceipt::getIsLongTermUse, query.getIsLongTermUse());
        }
        
        // 添加归还状态查询条件
        if (query.getReturnStatus() != null) {
            wrapper.eq(AssetReceipt::getReturnStatus, query.getReturnStatus());
        }
        
        // 添加审核状态查询条件
        if (query.getReviewStatus() != null) {
            wrapper.eq(AssetReceipt::getReviewStatus, query.getReviewStatus());
        }
        
        // 添加排序条件
        if (StringUtils.isNotBlank(query.getOrderByColumn())) {
            // 这里需要根据实际的排序字段做映射处理
            // 简单示例:
            if ("collectionDate".equals(query.getOrderByColumn())) {
                wrapper.orderBy(true, "asc".equalsIgnoreCase(query.getIsAsc()), 
                              AssetReceipt::getCollectionDate);
            }
        } else {
            // 默认排序
            wrapper.orderByDesc(AssetReceipt::getCollectionDate);
        }
        
        return page(page, wrapper);
    }

    @Override
    public AssetReceipt getById(Long id) {
        return baseMapper.selectById(id);
    }

    @Override
    public boolean add(AssetReceipt assetReceipt) {
        return baseMapper.insert(assetReceipt) > 0;
    }

    @Override
    public boolean update(AssetReceipt assetReceipt) {
        return baseMapper.updateById(assetReceipt) > 0;
    }

    @Override
    public boolean deleteById(Long id) {
        return baseMapper.deleteById(id) > 0;
    }

    @Override
    public boolean approveReceipt(AssetReceiptRecordQuery query) {
        // 实现审批逻辑
        return true;
    }
} 