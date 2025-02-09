package com.project.system2.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.system2.domain.entity.SysDictType;
import com.project.system2.mapper.SysDictTypeMapper;
import com.project.system2.service.ISysDictTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
public class SysDictTypeServiceImpl implements ISysDictTypeService {

    @Autowired
    private SysDictTypeMapper dictTypeMapper;

    @Override
    public Page<SysDictType> selectDictTypePage(Page<SysDictType> page, SysDictType dictType) {
        LambdaQueryWrapper<SysDictType> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(dictType.getDictName() != null, SysDictType::getDictName, dictType.getDictName())
                .eq(dictType.getStatus() != null, SysDictType::getStatus, dictType.getStatus())
                .orderByAsc(SysDictType::getDictId);
        return dictTypeMapper.selectPage(page, wrapper);
    }

    @Override
    public List<SysDictType> selectDictTypeList(SysDictType dictType) {
        LambdaQueryWrapper<SysDictType> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(dictType.getDictName() != null, SysDictType::getDictName, dictType.getDictName())
                .eq(dictType.getStatus() != null, SysDictType::getStatus, dictType.getStatus())
                .orderByAsc(SysDictType::getDictId);
        return dictTypeMapper.selectList(wrapper);
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
} 