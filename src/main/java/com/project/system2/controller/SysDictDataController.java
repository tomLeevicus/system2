package com.project.system2.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.system2.common.core.domain.Result;
import com.project.system2.common.core.domain.PageQuery;
import com.project.system2.domain.entity.SysDictData;
import com.project.system2.service.SysDictDataService;
import com.project.system2.utils.ExcelUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/system/dict/data")
public class SysDictDataController {
    
    @Autowired
    private SysDictDataService dictDataService;

    @PreAuthorize("@ss.hasPermi('system:dict:list')")
    @GetMapping("/list")
    public Result<Page<SysDictData>> list(SysDictData dictData, PageQuery pageQuery) {
        Page<SysDictData> page = new Page<>(pageQuery.getPageNum(), pageQuery.getPageSize());
        return Result.success(dictDataService.selectDictDataPage(page, dictData));
    }

    @GetMapping(value = "/type/{dictType}")
    public Result<List<SysDictData>> dictType(@PathVariable String dictType) {
        return Result.success(dictDataService.selectDictDataByType(dictType));
    }

    @PreAuthorize("@ss.hasPermi('system:dict:query')")
    @GetMapping(value = "/{dictCode}")
    public Result<SysDictData> getInfo(@PathVariable Long dictCode) {
        return Result.success(dictDataService.selectDictDataById(dictCode));
    }

    @PreAuthorize("@ss.hasPermi('system:dict:add')")
    @PostMapping
    public Result<Void> add(@Validated @RequestBody SysDictData dict) {
        return dictDataService.insertDictData(dict) ? Result.success() : Result.error();
    }

    @PreAuthorize("@ss.hasPermi('system:dict:edit')")
    @PutMapping
    public Result<Void> edit(@Validated @RequestBody SysDictData dict) {
        return dictDataService.updateDictData(dict) ? Result.success() : Result.error();
    }

    @PreAuthorize("@ss.hasPermi('system:dict:remove')")
    @DeleteMapping("/{dictCodes}")
    public Result<Void> remove(@PathVariable Long[] dictCodes) {
        return dictDataService.deleteDictDataByIds(dictCodes) ? Result.success() : Result.error();
    }

    @PreAuthorize("@ss.hasPermi('system:dict:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysDictData dictData) {
        List<SysDictData> list = dictDataService.selectDictDataList(dictData);
        ExcelUtil<SysDictData> util = new ExcelUtil<>(SysDictData.class);
        util.exportExcel(response, list, "字典数据");
    }
} 