package com.project.system2.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.update.UpdateChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.system2.common.core.domain.Result;
import com.project.system2.common.core.utils.SecurityUtils;
import com.project.system2.domain.entity.AssetReceipt;
import com.project.system2.domain.entity.Assets;
import com.project.system2.domain.model.AssetReceiptQuery;
import com.project.system2.domain.dto.PersonalAssetDTO;
import com.project.system2.domain.query.AssetReceiptRecordQuery;
import com.project.system2.mapper.AssetReceiptMapper;
import com.project.system2.mapper.AssetReceiptRecordMapper;
import com.project.system2.mapper.AssetsMapper;
import com.project.system2.service.IAssetsReceiptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.lang3.StringUtils;
import com.project.system2.common.core.utils.EntityUtils;
import com.project.system2.domain.entity.AssetReceiptRecord;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class AssetsReceiptServiceImpl implements IAssetsReceiptService {

    private static final Logger log = LoggerFactory.getLogger(AssetsReceiptServiceImpl.class);

    @Autowired
    private AssetReceiptMapper assetReceiptMapper;

    @Autowired
    private AssetsMapper assetMapper;

    @Autowired
    private AssetReceiptRecordMapper assetReceiptRecordMapper;

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
    @Transactional(rollbackFor = Exception.class)
    public Result<Boolean> add(AssetReceipt assetsReceipt) {
        // 查询资产信息
        Assets asset = assetMapper.selectById(assetsReceipt.getAssetId());
        
        // 检查asset_user_id是否有数据
        if (asset.getAssetUserId() != null) {
            return Result.error("资产已被领用");
        }

        // 填充并保存AssetReceipt
        EntityUtils.setCreateAndUpdateInfo(assetsReceipt, true);
        assetsReceipt.setAssetName(asset.getAssetName());
        assetsReceipt.setReceiverId(SecurityUtils.getUserId());
        assetsReceipt.setReceiverName(SecurityUtils.getUsername());
        assetsReceipt.setReturnStatus(0);
        assetsReceipt.setReviewStatus(0);
        int rows = assetReceiptMapper.insert(assetsReceipt);
        
        if (rows == 0){
            return Result.error("领用申请提交失败");
        }

        int i = assetReceiptRecordMapper.checkAssetId(assetsReceipt.getAssetId());
        if (i != 0) {
            throw new RuntimeException("资产已被申请领用在审核中");
        }
        AssetReceiptRecord record = new AssetReceiptRecord();
        record.setReceiptId(assetsReceipt.getId());
        record.setAssetId(assetsReceipt.getAssetId());
        record.setRecipientId(assetsReceipt.getReceiverId());
        record.setApproverId(null); // 审批人ID需要后续审批流程设置
        record.setApplyTime(new Date());
        record.setStatus(0); // 初始状态

        // 保存领用记录
        int insert = assetReceiptRecordMapper.insert(record);
        if (insert >0){
            return Result.success(true);
        }
        throw new RuntimeException("提交失败");
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

    @Override
    public Result<IPage<PersonalAssetDTO>> getPersonalAssets(Long userId, int pageNum, int pageSize) {
        Page<PersonalAssetDTO> page = new Page<>(pageNum, pageSize);
        IPage<PersonalAssetDTO> resultPage = assetReceiptMapper.selectPersonalAssets(page, userId);
        return Result.success(resultPage);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Boolean> approveReceipt(AssetReceiptRecordQuery query) {
        try {
            // 1. 查询领用记录
            AssetReceiptRecord record = assetReceiptRecordMapper.selectByReceiptId(query.getReceiptId());
            if (record == null) {
                return Result.error("领用记录不存在");
            }

            // 2. 检查审批状态
            if (record.getStatus() != 0) {
                return Result.error("该申请已处理，不可重复操作");
            }

            // 3. 更新审批记录
            record.setStatus(query.getIsAgree() ? 1 : 2);
            record.setApproverId(SecurityUtils.getUserId());
            record.setApprovalTime(new Date());
            record.setRemark(query.getRemark());
            int updateResult = assetReceiptRecordMapper.updateById(record);
            if (updateResult <= 0) {
                throw new RuntimeException("更新审批记录失败");
            }

            // 4. 审批通过处理
            if (query.getIsAgree()) {
                int result = assetMapper.updateReceiptStatus(
                    record.getAssetId(),
                    record.getRecipientId(),
                    new Date()
                );
                
                if (result == 0) {
                    throw new RuntimeException("资产状态更新失败，可能已被他人领用");
                }
            }
            
            return Result.success(true);
        } catch (Exception e) {
            log.error("审批操作异常：", e);
            throw new RuntimeException("审批流程异常：" + e.getMessage());
        }
    }

} 