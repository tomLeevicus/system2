package com.project.system2.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.project.system2.common.core.domain.Result;
import com.project.system2.domain.entity.AssetRepair;
import com.project.system2.domain.query.AssetRepairQuery;
import com.project.system2.service.IAssetRepairService;
import com.project.system2.service.IAssetScrapService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: BlueberryLee
 * @Date: 2025/1/27 13:22
 * @Description: TODO
 */
@RestController
@RequestMapping("/asset/repair")
@Tag(name = "资产维修")
public class AssetRepairController {
    @Autowired
    private IAssetRepairService assetRepairService;

    @Autowired
    private IAssetScrapService assetScrapService;

    /**
     * 分页查询维修记录
     */
    @GetMapping("/list")
    @Operation(summary = "分页查询维修记录", description = "根据条件分页查询维修记录")
    public Result<IPage<AssetRepair>> list(AssetRepairQuery query) {
        return assetRepairService.queryList(query);
    }

    /**
     * 获取维修记录详细信息
     */
    @GetMapping("/getInfo/{id}")
    @Operation(summary = "获取维修详情", description = "根据维修ID获取详细信息")
    @Parameter(name = "id", description = "维修记录ID", example = "500", required = true)
    public Result<AssetRepair> getInfo(@PathVariable Long id) {
        return assetRepairService.getById(id);
    }

    /**
     * 新增维修记录
     */
    @PostMapping("/add")
    @Operation(summary = "新增维修记录", description = "创建新的资产维修记录")
    @Parameter(name = "assetRepair", description = "维修记录对象", required = true)
    public Result<Boolean> add(@RequestBody AssetRepair assetRepair) {
        return assetRepairService.add(assetRepair);
    }

    /**
     * 修改维修记录
     */
    @PutMapping("/edit")
    @Operation(summary = "修改维修记录", description = "更新现有维修记录信息")
    @Parameter(name = "assetRepair", description = "维修记录对象", required = true)
    public Result<Boolean> edit(@RequestBody AssetRepair assetRepair) {
        return assetRepairService.update(assetRepair);
    }

    /**
     * 删除维修记录
     */
    @DeleteMapping("/remove/{id}")
    @Operation(summary = "删除维修记录", description = "根据ID删除维修记录")
    @Parameter(name = "id", description = "维修记录ID", example = "500", required = true)
    public Result<Boolean> remove(@PathVariable Long id) {
        return assetRepairService.deleteById(id);
    }

}
