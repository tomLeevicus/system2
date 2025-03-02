package com.project.system2.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.project.system2.domain.entity.AssetReceiptRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AssetReceiptRecordMapper extends BaseMapper<AssetReceiptRecord> {
    int checkAssetId(@Param("assetId") Long assetId);

    AssetReceiptRecord selectByReceiptId(@Param("receiptId") Long receiptId);

    int deleteByReceiptId(@Param("id") Long id);
}