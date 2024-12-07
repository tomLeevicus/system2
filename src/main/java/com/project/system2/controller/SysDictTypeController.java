package com.project.system2.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.system2.common.core.domain.Result;
import com.project.system2.common.core.domain.PageQuery;
import com.project.system2.domain.entity.SysDictType;
import com.project.system2.service.SysDictTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system/dict/type")
public class SysDictTypeController {
    
    @Autowired
    private SysDictTypeService dictTypeService;

    @PreAuthorize("@ss.hasPermi('system:dict:list')")
    @GetMapping("/list")
    public Result<Page<SysDictType>> list(SysDictType dictType, PageQuery pageQuery) {
        Page<SysDictType> page = new Page<>(pageQuery.getPageNum(), pageQuery.getPageSize());
        return Result.success(dictTypeService.selectDictTypePage(page, dictType));
    }

    @PreAuthorize("@ss.hasPermi('system:dict:query')")
    @GetMapping(value = "/{dictId}")
    public Result<SysDictType> getInfo(@PathVariable Long dictId) {
        return Result.success(dictTypeService.selectDictTypeById(dictId));
    }

    @PreAuthorize("@ss.hasPermi('system:dict:add')")
    @PostMapping
    public Result<Void> add(@Validated @RequestBody SysDictType dict) {
        if (!dictTypeService.checkDictTypeUnique(dict)) {
            return Result.error("新增字典'" + dict.getDictType() + "'失败，字典类型已存在");
        }
        return dictTypeService.insertDictType(dict) ? Result.success() : Result.error();
    }

    @PreAuthorize("@ss.hasPermi('system:dict:edit')")
    @PutMapping
    public Result<Void> edit(@Validated @RequestBody SysDictType dict) {
        if (!dictTypeService.checkDictTypeUnique(dict)) {
            return Result.error("修改字典'" + dict.getDictType() + "'失败，字典类型已存在");
        }
        return dictTypeService.updateDictType(dict) ? Result.success() : Result.error();
    }

    @PreAuthorize("@ss.hasPermi('system:dict:remove')")
    @DeleteMapping("/{dictIds}")
    public Result<Void> remove(@PathVariable Long[] dictIds) {
        return dictTypeService.deleteDictTypeByIds(dictIds) ? Result.success() : Result.error();
    }
} 