package com.project.system2.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.system2.common.core.utils.QueryHelper;
import com.project.system2.domain.entity.SysDictData;
import com.project.system2.domain.query.SysDictDataQuery;
import com.project.system2.mapper.SysDictDataMapper;
import com.project.system2.service.ISysDictDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

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
    public Page<SysDictData> selectDictDataPage(SysDictDataQuery query) {
        Page<SysDictData> page = new Page<>(query.getPageNum(), query.getPageSize());
        LambdaQueryWrapper<SysDictData> wrapper = createWrapper(query);
        return dictDataMapper.selectPage(page, wrapper);
    }

    @Override
    public List<SysDictData> selectDictDataList(SysDictDataQuery query) {
        LambdaQueryWrapper<SysDictData> wrapper = createWrapper(query);
        return dictDataMapper.selectList(wrapper);
    }

    @Override
    public List<SysDictData> selectDictDataByType(String dictType) {
        LambdaQueryWrapper<SysDictData> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysDictData::getDictType, dictType)
                .eq(SysDictData::getStatus, "0")
                .orderByAsc(SysDictData::getDictSort);
        return dictDataMapper.selectList(wrapper);
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

    /**
     * 创建查询包装器
     *
     * @param query 查询对象
     * @return 查询包装器
     */
    private LambdaQueryWrapper<SysDictData> createWrapper(SysDictDataQuery query) {
        LambdaQueryWrapper<SysDictData> wrapper = new LambdaQueryWrapper<>();
        
        // 添加条件查询
        if (StringUtils.hasText(query.getDictLabel())) {
            QueryHelper.setLike(wrapper, query.getDictLabel(), SysDictData::getDictLabel);
        }
        if (StringUtils.hasText(query.getDictValue())) {
            QueryHelper.setLike(wrapper, query.getDictValue(), SysDictData::getDictValue);
        }
        if (StringUtils.hasText(query.getDictType())) {
            wrapper.eq(SysDictData::getDictType, query.getDictType());
        }
        if (StringUtils.hasText(query.getStatus())) {
            wrapper.eq(SysDictData::getStatus, query.getStatus());
        }
        
        // 添加排序
        if (StringUtils.hasText(query.getOrderByColumn())) {
            boolean isAsc = "asc".equalsIgnoreCase(query.getIsAsc());
            
            // 根据排序字段动态设置排序
            switch (query.getOrderByColumn()) {
                case "dictCode":
                    wrapper.orderBy(true, isAsc, SysDictData::getDictCode);
                    break;
                case "dictLabel":
                    wrapper.orderBy(true, isAsc, SysDictData::getDictLabel);
                    break;
                case "dictValue":
                    wrapper.orderBy(true, isAsc, SysDictData::getDictValue);
                    break;
                case "dictType":
                    wrapper.orderBy(true, isAsc, SysDictData::getDictType);
                    break;
                case "dictSort":
                    wrapper.orderBy(true, isAsc, SysDictData::getDictSort);
                    break;
                case "status":
                    wrapper.orderBy(true, isAsc, SysDictData::getStatus);
                    break;
                case "createTime":
                    wrapper.orderBy(true, isAsc, SysDictData::getCreateTime);
                    break;
                default:
                    // 默认按照字典排序字段排序
                    wrapper.orderByAsc(SysDictData::getDictSort);
                    break;
            }
        } else {
            // 默认按照字典排序字段排序
            wrapper.orderByAsc(SysDictData::getDictSort);
        }
        
        return wrapper;
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
} 