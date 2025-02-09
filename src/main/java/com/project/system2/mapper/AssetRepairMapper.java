package com.project.system2.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.system2.domain.entity.AssetRepair;
import com.project.system2.domain.model.AssetRepairQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AssetRepairMapper extends BaseMapper<AssetRepair> {
    /**
     * 分页查询维修记录
     */
    IPage<AssetRepair> selectRepairPage(Page<AssetRepair> page, @Param("query") AssetRepairQuery query);
}