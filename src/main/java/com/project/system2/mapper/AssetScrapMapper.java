package com.project.system2.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.project.system2.domain.entity.AssetScrap;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AssetScrapMapper extends BaseMapper<AssetScrap> {
    IPage<AssetScrap> selectPage(Page<AssetScrap> page, @Param("ew") Wrapper<AssetScrap> queryWrapper);
} 