package com.project.system2.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.system2.domain.entity.AssetScrapRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface AssetScrapRecordMapper extends BaseMapper<AssetScrapRecord> {
    IPage<AssetScrapRecord> selectPage(Page<AssetScrapRecord> page, @Param("ew") Wrapper<AssetScrapRecord> queryWrapper);
    
    AssetScrapRecord selectByAssetId(@Param("assetId") Long assetId);
    
    AssetScrapRecord selectByScrapId(@Param("scrapId") Long scrapId);
    
    List<AssetScrapRecord> selectPendingApproval();
    
    List<AssetScrapRecord> selectByUserId(@Param("userId") Long userId);
    
    Integer countByUserId(@Param("userId") Long userId);
    
    List<AssetScrapRecord> selectApproved();
} 