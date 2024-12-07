package com.project.system2.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.system2.domain.entity.SysDictData;
import java.util.List;

public interface SysDictDataService {
    
    Page<SysDictData> selectDictDataPage(Page<SysDictData> page, SysDictData dictData);
    
    List<SysDictData> selectDictDataList(SysDictData dictData);
    
    List<SysDictData> selectDictDataByType(String dictType);
    
    String selectDictLabel(String dictType, String dictValue);
    
    SysDictData selectDictDataById(Long dictCode);
    
    boolean insertDictData(SysDictData dictData);
    
    boolean updateDictData(SysDictData dictData);
    
    boolean deleteDictDataByIds(Long[] dictCodes);
    
    void loadingDictCache();
    
    void clearDictCache();
    
    void resetDictCache();
} 