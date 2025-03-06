package com.project.system2.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.system2.domain.entity.AssetScrapRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AssetScrapRecordMapper extends BaseMapper<AssetScrapRecord> {
    IPage<AssetScrapRecord> selectPage(Page<AssetScrapRecord> page, @Param("ew") Wrapper<AssetScrapRecord> queryWrapper);
} 