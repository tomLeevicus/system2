package com.project.system2.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.system2.domain.entity.SysDictType;
import com.project.system2.domain.query.SysDictTypeQuery;
import java.util.List;

public interface ISysDictTypeService {
    
    /**
     * 查询字典类型分页列表
     * 
     * @param query 查询对象
     * @return 字典类型分页列表
     */
    Page<SysDictType> selectDictTypePage(SysDictTypeQuery query);
    
    /**
     * 根据字典类型ID查询字典类型
     * 
     * @param dictId 字典类型ID
     * @return 字典类型
     */
    SysDictType selectDictTypeById(Long dictId);
    
    /**
     * 新增字典类型
     * 
     * @param dictType 字典类型信息
     * @return 结果
     */
    boolean insertDictType(SysDictType dictType);
    
    /**
     * 修改字典类型
     * 
     * @param dictType 字典类型信息
     * @return 结果
     */
    boolean updateDictType(SysDictType dictType);
    
    /**
     * 批量删除字典类型
     * 
     * @param dictIds 需要删除的字典类型ID
     * @return 结果
     */
    boolean deleteDictTypeByIds(Long[] dictIds);
    
    /**
     * 校验字典类型是否唯一
     * 
     * @param dictType 字典类型信息
     * @return 结果
     */
    boolean checkDictTypeUnique(SysDictType dictType);
} 