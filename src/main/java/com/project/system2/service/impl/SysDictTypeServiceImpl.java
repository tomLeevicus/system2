package com.project.system2.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.system2.common.core.utils.QueryHelper;
import com.project.system2.domain.entity.SysDictType;
import com.project.system2.domain.query.SysDictTypeQuery;
import com.project.system2.mapper.SysDictTypeMapper;
import com.project.system2.service.ISysDictTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;

@Service
public class SysDictTypeServiceImpl implements ISysDictTypeService {

    @Autowired
    private SysDictTypeMapper dictTypeMapper;

    @Override
    public Page<SysDictType> selectDictTypePage(SysDictTypeQuery query) {
        Page<SysDictType> page = new Page<>(query.getPageNum(), query.getPageSize());
        LambdaQueryWrapper<SysDictType> wrapper = createWrapper(query);
        return dictTypeMapper.selectPage(page, wrapper);
    }


    @Override
    public SysDictType selectDictTypeById(Long dictId) {
        return dictTypeMapper.selectById(dictId);
    }

    @Override
    @Transactional
    public boolean insertDictType(SysDictType dictType) {
        return dictTypeMapper.insert(dictType) > 0;
    }

    @Override
    @Transactional
    public boolean updateDictType(SysDictType dictType) {
        return dictTypeMapper.updateById(dictType) > 0;
    }

    @Override
    @Transactional
    public boolean deleteDictTypeByIds(Long[] dictIds) {
        return Arrays.stream(dictIds)
                .map(dictTypeMapper::deleteById)
                .reduce(0, Integer::sum) == dictIds.length;
    }

    @Override
    public boolean checkDictTypeUnique(SysDictType dictType) {
        Long dictId = dictType.getDictId() == null ? -1L : dictType.getDictId();
        LambdaQueryWrapper<SysDictType> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysDictType::getDictType, dictType.getDictType())
                .ne(SysDictType::getDictId, dictId);
        return dictTypeMapper.selectCount(wrapper) == 0;
    }
    
    /**
     * 创建查询包装器
     *
     * @param query 查询对象
     * @return 查询包装器
     */
    private LambdaQueryWrapper<SysDictType> createWrapper(SysDictTypeQuery query) {
        LambdaQueryWrapper<SysDictType> wrapper = new LambdaQueryWrapper<>();
        
        // 添加条件查询
        if (StringUtils.hasText(query.getDictName())) {
            QueryHelper.setLike(wrapper, query.getDictName(), SysDictType::getDictName);
        }
        if (StringUtils.hasText(query.getDictType())) {
            QueryHelper.setLike(wrapper, query.getDictType(), SysDictType::getDictType);
        }
        if (query.getStatus() != null) {
            QueryHelper.setStatus(wrapper, query.getStatus(), SysDictType::getStatus);
        }
        
        // 添加排序
        if (StringUtils.hasText(query.getOrderByColumn())) {
            boolean isAsc = "asc".equalsIgnoreCase(query.getIsAsc());
            
            // 根据排序字段动态设置排序
            switch (query.getOrderByColumn()) {
                case "dictId":
                    wrapper.orderBy(true, isAsc, SysDictType::getDictId);
                    break;
                case "dictName":
                    wrapper.orderBy(true, isAsc, SysDictType::getDictName);
                    break;
                case "dictType":
                    wrapper.orderBy(true, isAsc, SysDictType::getDictType);
                    break;
                case "status":
                    wrapper.orderBy(true, isAsc, SysDictType::getStatus);
                    break;
                case "createTime":
                    wrapper.orderBy(true, isAsc, SysDictType::getCreateTime);
                    break;
                default:
                    // 默认按照字典ID排序
                    wrapper.orderByAsc(SysDictType::getDictId);
                    break;
            }
        } else {
            // 默认按照字典ID排序
            wrapper.orderByAsc(SysDictType::getDictId);
        }
        
        return wrapper;
    }
} 