package com.project.system2.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.system2.common.core.domain.Result;
import com.project.system2.common.core.domain.PageQuery;
import com.project.system2.domain.entity.SysDictType;
import com.project.system2.service.ISysDictTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/system/dict/type")
@Tag(name = "字典类型", description = "数据字典类型管理接口")
public class SysDictTypeController {
    
    @Autowired
    private ISysDictTypeService dictTypeService;

    @PreAuthorize("@ss.hasPermi('system:dict:list')")
    @GetMapping("/list")
    @Operation(summary = "分页查询字典类型", description = "根据条件分页查询字典类型")
    @Parameter(name = "pageNum", description = "页码", example = "1")
    @Parameter(name = "pageSize", description = "每页数量", example = "10")
    public Result<Page<SysDictType>> list(SysDictType dictType, PageQuery pageQuery) {
        Page<SysDictType> page = new Page<>(pageQuery.getPageNum(), pageQuery.getPageSize());
        return Result.success(dictTypeService.selectDictTypePage(page, dictType));
    }

    @PreAuthorize("@ss.hasPermi('system:dict:query')")
    @GetMapping(value = "/{dictId}")
    @Operation(summary = "获取字典类型详情", description = "根据字典类型ID获取详细信息")
    @Parameter(name = "dictId", description = "字典类型ID", example = "600", required = true)
    public Result<SysDictType> getInfo(@PathVariable Long dictId) {
        return Result.success(dictTypeService.selectDictTypeById(dictId));
    }

    @PreAuthorize("@ss.hasPermi('system:dict:add')")
    @PostMapping
    @Operation(summary = "新增字典类型", description = "创建新的字典类型")
    @Parameter(name = "dict", description = "字典类型对象", required = true)
    public Result<Void> add(@Validated @RequestBody SysDictType dict) {
        if (!dictTypeService.checkDictTypeUnique(dict)) {
            return Result.error("新增字典'" + dict.getDictType() + "'失败，字典类型已存在");
        }
        return dictTypeService.insertDictType(dict) ? Result.success() : Result.error();
    }

    @PreAuthorize("@ss.hasPermi('system:dict:edit')")
    @PutMapping
    @Operation(summary = "修改字典类型", description = "更新现有字典类型信息")
    @Parameter(name = "dict", description = "字典类型对象", required = true)
    public Result<Void> edit(@Validated @RequestBody SysDictType dict) {
        if (!dictTypeService.checkDictTypeUnique(dict)) {
            return Result.error("修改字典'" + dict.getDictType() + "'失败，字典类型已存在");
        }
        return dictTypeService.updateDictType(dict) ? Result.success() : Result.error();
    }

    @PreAuthorize("@ss.hasPermi('system:dict:remove')")
    @DeleteMapping("/{dictIds}")
    @Operation(summary = "删除字典类型", description = "根据ID批量删除字典类型")
    @Parameter(name = "dictIds", description = "字典类型ID数组", example = "[2001,2002]", required = true)
    public Result<Void> remove(@PathVariable Long[] dictIds) {
        return dictTypeService.deleteDictTypeByIds(dictIds) ? Result.success() : Result.error();
    }

    /*@PutMapping("/refreshCache")
    @Operation(summary = "刷新字典缓存", description = "强制刷新字典缓存到Redis")
    public Result<Void> refreshCache() {
        // ...
    }*/
} 