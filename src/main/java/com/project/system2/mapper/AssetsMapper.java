package com.project.system2.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.system2.domain.entity.Assets;
import com.project.system2.domain.model.AssetsQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 资产管理 数据层
 */
@Mapper
public interface AssetsMapper extends BaseMapper<Assets> {
    
    /**
     * 条件查询资产列表
     *
     * @param page 分页信息
     * @param query 查询条件
     * @return 分页查询结果
     */
    IPage<Assets> selectAssetsList(Page<Assets> page, @Param("query") AssetsQuery query);
} 