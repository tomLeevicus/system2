package com.project.system2.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.project.system2.common.core.domain.Result;
import com.project.system2.domain.entity.AssetStorage;
import com.project.system2.domain.query.AssetStorageQuery;
import com.project.system2.service.IAssetStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/asset/storage")
@Tag(name = "资产入库", description = "资产入库管理接口")
public class AssetStorageController {

    @Autowired
    private IAssetStorageService assetStorageService;

    /**
     * 分页查询资产入库记录
     */
    @GetMapping("/list")
    @Operation(summary = "分页查询入库记录", description = "根据条件分页查询入库记录")
    @Parameter(name = "pageNum", description = "页码", example = "1")
    @Parameter(name = "pageSize", description = "每页数量", example = "10")
    public Result<IPage<AssetStorage>> list(AssetStorageQuery query) {
        return assetStorageService.queryList(query);
    }

    /**
     * 获取资产入库记录详情
     */
    @GetMapping("/getInfo/{id}")
    @Operation(summary = "获取入库详情", description = "根据入库ID获取详细信息")
    @Parameter(name = "id", description = "入库记录ID", example = "400", required = true)
    public Result<AssetStorage> getInfo(@PathVariable Long id) {
        return assetStorageService.getById(id);
    }

    /**
     * 新增资产入库记录
     */
    @PostMapping("/add")
    @Operation(summary = "新增入库记录", description = "创建新的资产入库记录")
    @Parameter(name = "assetStorage", description = "入库记录对象", required = true)
    public Result<Boolean> add(@RequestBody AssetStorage assetStorage) {
        return assetStorageService.add(assetStorage);
    }

    /**
     * 修改资产入库记录
     */
    @PutMapping("/edit")
    @Operation(summary = "修改入库记录", description = "更新现有入库记录信息")
    @Parameter(name = "assetStorage", description = "入库记录对象", required = true)
    public Result<Boolean> edit(@RequestBody AssetStorage assetStorage) {
        return assetStorageService.update(assetStorage);
    }

    /**
     * 删除资产入库记录
     */
    @DeleteMapping("/remove/{id}")
    @Operation(summary = "删除入库记录", description = "根据ID删除入库记录")
    @Parameter(name = "id", description = "入库记录ID", example = "400", required = true)
    public Result<Boolean> remove(@PathVariable Long id) {
        return assetStorageService.deleteById(id);
    }
} 