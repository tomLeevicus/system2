package com.project.system2.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.system2.common.core.domain.Result;
import com.project.system2.common.core.exception.ServiceException;
import com.project.system2.common.core.utils.EntityUtils;
import com.project.system2.common.core.utils.SecurityUtils;
import com.project.system2.config.SecurityConfig;
import com.project.system2.domain.entity.AssetScrap;
import com.project.system2.domain.entity.AssetScrapRecord;
import com.project.system2.domain.entity.Assets;
import com.project.system2.domain.dto.AssetScrapApprovalDTO;
import com.project.system2.domain.query.AssetScrapPageQuery;
import com.project.system2.mapper.AssetScrapMapper;
import com.project.system2.mapper.AssetScrapRecordMapper;
import com.project.system2.mapper.AssetsMapper;
import com.project.system2.service.IAssetScrapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import java.util.Date;
import org.apache.commons.lang3.StringUtils;

@Service
@RequiredArgsConstructor
public class AssetScrapServiceImpl implements IAssetScrapService {

    @Autowired
    private  AssetScrapRecordMapper scrapRecordMapper;

    @Autowired
    private  AssetsMapper assetsMapper;

    @Autowired
    private  AssetScrapMapper assetScrapMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Boolean> scrapAsset(AssetScrap assetScrap) {
        // 处理资产报废的逻辑
        Assets assets = assetsMapper.selectById(assetScrap.getAssetId());
        if (assets == null) {
            return Result.error("未查到对应资产信息");
        }
        Long userId = SecurityUtils.getUserId();
        if (userId != assets.getAssetUserId()) {
            return Result.error("不是资产领用者,不可报废");
        }
        assetScrap.setAssetName(assets.getAssetName());
        assetScrap.setStartTime(assets.getAssetUseTime());
        EntityUtils.setCreateAndUpdateInfo(assetScrap, true);
        int rows = assetScrapMapper.insert(assetScrap);
        if (rows == 0) {
            throw new RuntimeException("新增记录失败");
        }
        AssetScrapRecord assetScrapRecord = new AssetScrapRecord();
        assetScrapRecord.setAssetId(assetScrap.getAssetId());
        assetScrapRecord.setScrapId(assetScrap.getId());
        assetScrapRecord.setScrapUserId(userId);

        int insert = scrapRecordMapper.insert(assetScrapRecord);

        return Result.success(insert > 0 );
    }

    @Override
    @Transactional
    public Result<Boolean> approveScrap(AssetScrapApprovalDTO dto) {
        // 1. 查询报废记录
        AssetScrapRecord record = scrapRecordMapper.selectById(dto.getScrapRecordId());
        if (record == null) {
            return Result.error("报废记录不存在");
        }
        
        // 2. 更新审批信息
        record.setApproverId(SecurityUtils.getUserId());
        record.setApprovalComment(dto.getApprovalComment());
        record.setIsAgreed(dto.getIsAgreed());
        record.setScrapTime(new Date());
        scrapRecordMapper.updateById(record);

        // 3. 审批通过时更新资产状态
        if (dto.getIsAgreed() == 1) {
            Assets asset = assetsMapper.selectById(record.getAssetId());
            if (asset == null) {
                throw new ServiceException("关联资产不存在");
            }
            asset.setAssetUseStatus(3); // 3-已报废
            assetsMapper.updateById(asset);
        }
        
        return Result.success(true);
    }

    @Override
    public Result<IPage<AssetScrap>> pageScrapRecords(AssetScrapPageQuery query) {
        Page<AssetScrap> page = new Page<>(query.getPageNum(), query.getPageSize());
        LambdaQueryWrapper<AssetScrap> wrapper = new LambdaQueryWrapper<>();
        
        wrapper.like(StringUtils.isNotEmpty(query.getAssetName()), AssetScrap::getAssetName, query.getAssetName())
              .ge(StringUtils.isNotEmpty(query.getStartTime()), AssetScrap::getCreateTime, query.getStartTime())
              .le(StringUtils.isNotEmpty(query.getEndTime()), AssetScrap::getCreateTime, query.getEndTime())
              .orderByDesc(AssetScrap::getCreateTime);

        Page<AssetScrap> resultPage = assetScrapMapper.selectPage(page, wrapper);
        return Result.success(resultPage);
    }
} 