package com.project.system2.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.project.system2.common.core.domain.Result;
import com.project.system2.domain.entity.AssetClassification;
import com.project.system2.domain.query.AssetClassificationQuery;
import com.project.system2.service.IAssetClassificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/asset/classification")
@Tag(name = "资产分类", description = "资产分类管理接口")
public class AssetClassificationController {

    @Autowired
    private IAssetClassificationService assetClassificationService;

    /**
     * 分页查询资产分类列表
     */
    @GetMapping("/list")
    @Operation(summary = "分页查询分类", description = "根据条件分页查询资产分类")
    public Result<IPage<AssetClassification>> list(AssetClassificationQuery query) {
        return assetClassificationService.queryList(query);
    }

    /**
     * 获取列表无分页
     */
    @GetMapping("/queryList")
    @Operation(summary = "获取全部分类", description = "获取所有资产分类列表（无分页）")
    public Result<List<AssetClassification>> queryList() {
        return assetClassificationService.getList();
    }

    /**
     * 获取资产分类详细信息
     */
    @GetMapping("/getInfo/{id}")
    @Operation(summary = "获取分类详情", description = "根据分类ID获取详细信息")
    @Parameter(name = "id", description = "分类ID", example = "300", required = true)
    public Result<AssetClassification> getInfo(@PathVariable Long id) {
        return assetClassificationService.getById(id);
    }

    /**
     * 新增资产分类
     */
    @PostMapping("/add")
    @Operation(summary = "新增分类", description = "创建新的资产分类")
    @Parameter(name = "classification", description = "分类对象", required = true)
    public Result<Boolean> add(@RequestBody AssetClassification classification) {
        return assetClassificationService.add(classification);
    }

    /**
     * 修改资产分类
     */
    @PutMapping("/edit")
    @Operation(summary = "修改分类", description = "更新现有资产分类信息")
    @Parameter(name = "classification", description = "分类对象", required = true)
    public Result<Boolean> edit(@RequestBody AssetClassification classification) {
        return assetClassificationService.update(classification);
    }

    /**
     * 删除资产分类
     */
    @DeleteMapping("/remove/{id}")
    @Operation(summary = "删除分类", description = "根据ID删除资产分类")
    @Parameter(name = "id", description = "分类ID", example = "300", required = true)
    public Result<Boolean> remove(@PathVariable Long id) {
        return assetClassificationService.deleteById(id);
    }
} 