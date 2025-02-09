package com.project.system2.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.system2.domain.entity.SysDictData;
import com.project.system2.mapper.SysDictDataMapper;
import com.project.system2.service.ISysDictDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SysDictDataServiceImpl implements ISysDictDataService {

    @Autowired
    private SysDictDataMapper dictDataMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final String DICT_KEY = "sys_dict:";

    @Override
    public Page<SysDictData> selectDictDataPage(Page<SysDictData> page, SysDictData dictData) {
        LambdaQueryWrapper<SysDictData> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(dictData.getDictType() != null, SysDictData::getDictType, dictData.getDictType())
                .like(dictData.getDictLabel() != null, SysDictData::getDictLabel, dictData.getDictLabel())
                .eq(dictData.getStatus() != null, SysDictData::getStatus, dictData.getStatus())
                .orderByAsc(SysDictData::getDictSort);
        return dictDataMapper.selectPage(page, wrapper);
    }

    @Override
    public List<SysDictData> selectDictDataList(SysDictData dictData) {
        LambdaQueryWrapper<SysDictData> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(dictData.getDictType() != null, SysDictData::getDictType, dictData.getDictType())
                .like(dictData.getDictLabel() != null, SysDictData::getDictLabel, dictData.getDictLabel())
                .eq(dictData.getStatus() != null, SysDictData::getStatus, dictData.getStatus())
                .orderByAsc(SysDictData::getDictSort);
        return dictDataMapper.selectList(wrapper);
    }

    @Cacheable(value = "dict", key = "#dictType")
    @Override
    public List<SysDictData> selectDictDataByType(String dictType) {
        Object cacheObj = redisTemplate.opsForValue().get(DICT_KEY + dictType);
        if (cacheObj != null) {
            return (List<SysDictData>) cacheObj;
        }
        
        LambdaQueryWrapper<SysDictData> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysDictData::getDictType, dictType)
                .eq(SysDictData::getStatus, "0")
                .orderByAsc(SysDictData::getDictSort);
        List<SysDictData> dictDatas = dictDataMapper.selectList(wrapper);
        
        redisTemplate.opsForValue().set(DICT_KEY + dictType, dictDatas);
        return dictDatas;
    }

    @Override
    public String selectDictLabel(String dictType, String dictValue) {
        List<SysDictData> dictDatas = selectDictDataByType(dictType);
        return dictDatas.stream()
                .filter(d -> d.getDictValue().equals(dictValue))
                .map(SysDictData::getDictLabel)
                .findFirst()
                .orElse("");
    }

    @Override
    public SysDictData selectDictDataById(Long dictCode) {
        return dictDataMapper.selectById(dictCode);
    }

    @Override
    @Transactional
    public boolean insertDictData(SysDictData dictData) {
        int row = dictDataMapper.insert(dictData);
        if (row > 0) {
            clearDictCache();
        }
        return row > 0;
    }

    @Override
    @Transactional
    public boolean updateDictData(SysDictData dictData) {
        int row = dictDataMapper.updateById(dictData);
        if (row > 0) {
            clearDictCache();
        }
        return row > 0;
    }

    @Override
    @Transactional
    public boolean deleteDictDataByIds(Long[] dictCodes) {
        int rows = Arrays.stream(dictCodes)
                .map(dictDataMapper::deleteById)
                .reduce(0, Integer::sum);
        if (rows > 0) {
            clearDictCache();
        }
        return rows == dictCodes.length;
    }

    @Override
    public void loadingDictCache() {
        LambdaQueryWrapper<SysDictData> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysDictData::getStatus, "0");
        List<SysDictData> dictDatas = dictDataMapper.selectList(wrapper);
        
        Map<String, List<SysDictData>> dictDataMap = dictDatas.stream()
                .collect(Collectors.groupingBy(SysDictData::getDictType));
        
        dictDataMap.forEach((key, value) -> {
            redisTemplate.opsForValue().set(DICT_KEY + key, value);
        });
    }

    @CacheEvict(value = "dict", allEntries = true)
    @Override
    public void clearDictCache() {
        Set<String> keys = redisTemplate.keys(DICT_KEY + "*");
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }

    @Override
    public void resetDictCache() {
        clearDictCache();
        loadingDictCache();
    }
} 