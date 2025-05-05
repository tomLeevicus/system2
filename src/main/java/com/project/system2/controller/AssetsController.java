package com.project.system2.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.project.system2.common.core.domain.Result;
import com.project.system2.domain.entity.Assets;
import com.project.system2.domain.query.AssetsQuery;
import com.project.system2.service.IAssetsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "资产管理", description = "固定资产管理相关接口")
@RestController
@RequestMapping("/assets")
public class AssetsController {

    @Autowired
    private IAssetsService assetsService;

    /**
     * 分页查询
     * @param query
     * @return
     */
    @PostMapping("/query")
    @Operation(summary = "资产分页查询", description = "根据条件分页查询资产信息")
    public Result<IPage<Assets>> query(@RequestBody AssetsQuery query) {
        return assetsService.queryAssetsList(query);
    }

    /**
     * 获取单个资产信息
     * @param id
     * @return
     */
    @GetMapping("/getInfo/{id}")
    @Operation(summary = "获取资产详情", description = "根据资产ID获取详细信息")
    @Parameter(name = "id", description = "资产ID", example = "1001", required = true)
    public Result<Assets> getInfo(@PathVariable Long id) {
        return assetsService.getAssetById(id);
    }

    /**
     * 新增资产
     * @param assets
     * @return
     */
    @PostMapping("/add")
    @Operation(summary = "新增资产", description = "创建新的资产记录")
    @Parameter(name = "assets", description = "资产对象", required = true)
    public Result<Boolean> add(@RequestBody Assets assets) {
        return assetsService.addAsset(assets);
    }

    /**
     * 编辑资产
     * @param assets
     * @return
     */
    @PutMapping("/edit")
    @Operation(summary = "更新资产", description = "修改现有资产信息")
    @Parameter(name = "assets", description = "资产对象", required = true)
    public Result<Boolean> edit(@RequestBody Assets assets) {
        return assetsService.updateAsset(assets);
    }

    /**
     * 删除资产
     * @param id
     * @return
     */
    @DeleteMapping("/remove/{id}")
    @Operation(summary = "删除资产", description = "根据ID删除资产记录")
    @Parameter(name = "id", description = "资产ID", example = "1001", required = true)
    public Result<Boolean> remove(@PathVariable Long id) {
        return assetsService.deleteAsset(id);
    }

    @Operation(summary = "批量计算并更新资产折旧")
    @PostMapping("/calculate-depreciation")
    public Result<Map<String, Object>> calculateDepreciation(@RequestBody List<Long> assetIds) {
        if (assetIds == null || assetIds.isEmpty()) {
            return Result.error("资产ID列表不能为空");
        }
        return assetsService.calculateAndApplyDepreciation(assetIds);
    }

} 