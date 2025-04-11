package com.project.system2.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.system2.common.core.domain.Result;
import com.project.system2.domain.entity.SysDictData;
import com.project.system2.domain.query.SysDictDataQuery;
import com.project.system2.service.ISysDictDataService;
import com.project.system2.utils.ExcelUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/system/dict/data")
@Tag(name = "字典数据", description = "数据字典数据管理接口")
public class SysDictDataController {
    
    @Autowired
    private ISysDictDataService dictDataService;

    @PreAuthorize("@ss.hasPermi('system:dict:list')")
    @GetMapping("/list")
    @Operation(summary = "分页查询字典数据", description = "根据条件分页查询字典数据")
    @Parameter(name = "pageNum", description = "页码", example = "1")
    @Parameter(name = "pageSize", description = "每页数量", example = "10")
    public Result<Page<SysDictData>> list(SysDictDataQuery query) {
        return Result.success(dictDataService.selectDictDataPage(query));
    }

    @GetMapping(value = "/type/{dictType}")
    @Operation(summary = "根据类型查询字典", description = "根据字典类型查询对应字典数据")
    @Parameter(name = "dictType", description = "字典类型", example = "asset_status", required = true)
    public Result<List<SysDictData>> dictType(@PathVariable String dictType) {
        return Result.success(dictDataService.selectDictDataByType(dictType));
    }

    @GetMapping(value = "/types")
    @Operation(summary = "批量获取字典数据", description = "根据多个字典类型批量获取字典数据")
    @Parameter(name = "dictTypes", description = "字典类型，多个用逗号分隔", example = "sys_normal_disable,notice_type", required = true)
    public Result<Map<String, List<SysDictData>>> dictTypes(@RequestParam String dictTypes) {
        String[] types = dictTypes.split(",");
        Map<String, List<SysDictData>> dictMap = new HashMap<>();
        for (String type : types) {
            if (type != null && !type.isEmpty()) {
                dictMap.put(type, dictDataService.selectDictDataByType(type));
            }
        }
        return Result.success(dictMap);
    }

    @PreAuthorize("@ss.hasPermi('system:dict:query')")
    @GetMapping(value = "/getInfo/{dictCode}")
    @Operation(summary = "获取字典数据详情", description = "根据字典编码获取详细信息")
    @Parameter(name = "dictCode", description = "字典数据编码", example = "100", required = true)
    public Result<SysDictData> getInfo(@PathVariable Long dictCode) {
        return Result.success(dictDataService.selectDictDataById(dictCode));
    }

    @PreAuthorize("@ss.hasPermi('system:dict:add')")
    @PostMapping("/add")
    @Operation(summary = "新增字典数据", description = "创建新的字典数据")
    @Parameter(name = "dict", description = "字典数据对象", required = true)
    public Result<Void> add(@Validated @RequestBody SysDictData dict) {
        return dictDataService.insertDictData(dict) ? Result.success() : Result.error();
    }

    @PreAuthorize("@ss.hasPermi('system:dict:edit')")
    @PutMapping("/edit")
    @Operation(summary = "修改字典数据", description = "更新现有字典数据信息")
    @Parameter(name = "dict", description = "字典数据对象", required = true)
    public Result<Void> edit(@Validated @RequestBody SysDictData dict) {
        return dictDataService.updateDictData(dict) ? Result.success() : Result.error();
    }

    @PreAuthorize("@ss.hasPermi('system:dict:remove')")
    @DeleteMapping("/remove/{dictCodes}")
    @Operation(summary = "删除字典数据", description = "根据ID批量删除字典数据")
    @Parameter(name = "dictCodes", description = "字典数据ID数组", example = "[1001,1002]", required = true)
    public Result<Void> remove(@PathVariable Long[] dictCodes) {
        return dictDataService.deleteDictDataByIds(dictCodes) ? Result.success() : Result.error();
    }

    @PreAuthorize("@ss.hasPermi('system:dict:export')")
    @PostMapping("/export")
    @Operation(summary = "导出字典数据", description = "导出字典数据到Excel文件")
    @Parameter(name = "dictData", description = "字典数据查询条件", hidden = true)
    @Parameter(name = "response", description = "HTTP响应对象", hidden = true)
    public void export(HttpServletResponse response, SysDictDataQuery query) {
        List<SysDictData> list = dictDataService.selectDictDataList(query);
        ExcelUtil<SysDictData> util = new ExcelUtil<>(SysDictData.class);
        util.exportExcel(response, list, "字典数据");
    }
} 