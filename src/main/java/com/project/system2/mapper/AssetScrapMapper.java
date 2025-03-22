package com.project.system2.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.project.system2.domain.entity.AssetScrap;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AssetScrapMapper extends BaseMapper<AssetScrap> {
    // 不需要自定义selectPage方法，直接使用BaseMapper提供的方法
} 