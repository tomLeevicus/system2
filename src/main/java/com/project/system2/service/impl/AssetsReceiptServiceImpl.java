package com.project.system2.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.system2.common.core.domain.Result;
import com.project.system2.domain.entity.AssetReceipt;
import com.project.system2.domain.model.AssetReceiptQuery;
import com.project.system2.mapper.AssetReceiptMapper;
import com.project.system2.service.IAssetsReceiptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.lang3.StringUtils;
import com.project.system2.common.core.utils.EntityUtils;

@Service
public class AssetsReceiptServiceImpl implements IAssetsReceiptService {

    @Autowired
    private AssetReceiptMapper assetReceiptMapper;

    @Override
    public Result<IPage<AssetReceipt>> queryList(AssetReceiptQuery query) {
        Page<AssetReceipt> page = new Page<>(query.getPageNum(), query.getPageSize());
        
        LambdaQueryWrapper<AssetReceipt> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(query.getAssetName()),
                    AssetReceipt::getAssetName, query.getAssetName())
               .like(StringUtils.isNotBlank(query.getReceiverName()),
                    AssetReceipt::getReceiverName, query.getReceiverName())
               .ge(query.getCollectionDateStart() != null,
                    AssetReceipt::getCollectionDate, query.getCollectionDateStart())
               .le(query.getCollectionDateEnd() != null,
                    AssetReceipt::getCollectionDate, query.getCollectionDateEnd())
               .eq(query.getIsLongTermUse() != null,
                    AssetReceipt::getIsLongTermUse, query.getIsLongTermUse())
               .eq(query.getReturnStatus() != null,
                    AssetReceipt::getReturnStatus, query.getReturnStatus())
               .eq(query.getReviewStatus() != null,
                    AssetReceipt::getReviewStatus, query.getReviewStatus())
               .orderByDesc(AssetReceipt::getCreateTime);
        
        IPage<AssetReceipt> pageResult = assetReceiptMapper.selectPage(page, wrapper);
        return Result.success(pageResult);
    }

    @Override
    public Result<AssetReceipt> getById(Long id) {
        AssetReceipt assetsReceipt = assetReceiptMapper.selectById(id);
        return Result.success(assetsReceipt);
    }

    @Override
    @Transactional
    public Result<Boolean> add(AssetReceipt assetsReceipt) {
        EntityUtils.setCreateAndUpdateInfo(assetsReceipt, true);
        assetsReceipt.setReceiverName(EntityUtils.setUserName());
        int rows = assetReceiptMapper.insert(assetsReceipt);
        return Result.success(rows > 0);
    }

    @Override
    @Transactional
    public Result<Boolean> update(AssetReceipt assetsReceipt) {
        EntityUtils.setCreateAndUpdateInfo(assetsReceipt, false);
        int rows = assetReceiptMapper.updateById(assetsReceipt);
        return Result.success(rows > 0);
    }

    @Override
    @Transactional
    public Result<Boolean> deleteById(Long id) {
        int rows = assetReceiptMapper.deleteById(id);
        return Result.success(rows > 0);
    }
} 