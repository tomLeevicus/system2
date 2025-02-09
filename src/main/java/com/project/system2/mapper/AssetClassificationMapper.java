package com.project.system2.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.system2.domain.entity.AssetClassification;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AssetClassificationMapper extends BaseMapper<AssetClassification> {
    
    /**
     * 查询分类关联的资产数量
     */
    int checkAssetsExist(Long classificationId);

    /**
     * 分页查询资产分类列表（包含资产数量）
     */
    IPage<AssetClassification> selectClassificationPage(Page<AssetClassification> page, @Param("query") AssetClassification classification);
}