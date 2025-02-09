package com.project.system2.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.system2.domain.entity.SysDictType;
import java.util.List;

public interface ISysDictTypeService {
    
    Page<SysDictType> selectDictTypePage(Page<SysDictType> page, SysDictType dictType);
    
    List<SysDictType> selectDictTypeList(SysDictType dictType);
    
    SysDictType selectDictTypeById(Long dictId);
    
    boolean insertDictType(SysDictType dictType);
    
    boolean updateDictType(SysDictType dictType);
    
    boolean deleteDictTypeByIds(Long[] dictIds);
    
    boolean checkDictTypeUnique(SysDictType dictType);
} 