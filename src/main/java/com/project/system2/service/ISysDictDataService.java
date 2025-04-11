package com.project.system2.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.system2.domain.entity.SysDictData;
import com.project.system2.domain.query.SysDictDataQuery;
import java.util.List;

public interface ISysDictDataService {
    
    /**
     * 查询字典数据分页列表
     * 
     * @param query 查询对象
     * @return 字典数据分页列表
     */
    Page<SysDictData> selectDictDataPage(SysDictDataQuery query);
    
    /**
     * 查询字典数据列表
     * 
     * @param query 查询对象
     * @return 字典数据列表
     */
    List<SysDictData> selectDictDataList(SysDictDataQuery query);
    
    /**
     * 根据字典类型查询字典数据
     * 
     * @param dictType 字典类型
     * @return 字典数据列表
     */
    List<SysDictData> selectDictDataByType(String dictType);
    
    /**
     * 根据字典类型和字典键值获取字典标签
     * 
     * @param dictType 字典类型
     * @param dictValue 字典键值
     * @return 字典标签
     */
    String selectDictLabel(String dictType, String dictValue);
    
    /**
     * 根据字典数据ID查询字典数据
     * 
     * @param dictCode 字典数据ID
     * @return 字典数据
     */
    SysDictData selectDictDataById(Long dictCode);
    
    /**
     * 新增字典数据
     * 
     * @param dictData 字典数据信息
     * @return 结果
     */
    boolean insertDictData(SysDictData dictData);
    
    /**
     * 修改字典数据
     * 
     * @param dictData 字典数据信息
     * @return 结果
     */
    boolean updateDictData(SysDictData dictData);
    
    /**
     * 批量删除字典数据
     * 
     * @param dictCodes 需要删除的字典数据ID
     * @return 结果
     */
    boolean deleteDictDataByIds(Long[] dictCodes);
    
    void loadingDictCache();
    
    void clearDictCache();
    
    void resetDictCache();
} 