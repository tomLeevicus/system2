package com.project.system2.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.project.system2.common.core.domain.Result;
import com.project.system2.domain.entity.AssetReceipt;
import com.project.system2.domain.entity.SysUser;
import com.project.system2.domain.model.AssetReceiptQuery;
import com.project.system2.domain.query.AssetReceiptRecordQuery;
import com.project.system2.service.IAssetsReceiptService;
import com.project.system2.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/asset/receipt")
@Tag(name = "资产领用", description = "资产领用管理接口")
public class AssetReceiptController {

    @Autowired
    private IAssetsReceiptService assetsReceiptService;


    @Autowired
    private ISysUserService sysUserService;

    /**
     * 分页查询资产领用记录
     */
    @GetMapping("/list")
    @Operation(summary = "分页查询领用记录", description = "根据条件分页查询资产领用记录")
    @Parameter(name = "pageNum", description = "页码", example = "1")
    @Parameter(name = "pageSize", description = "每页数量", example = "10")
    public Result<IPage<AssetReceipt>> list(AssetReceiptQuery query) {
        return assetsReceiptService.queryList(query);
    }

    /**
     * 获取资产领用记录详情
     */
    @GetMapping("/getInfo/{id}")
    @Operation(summary = "获取领用详情", description = "根据领用ID获取详细信息")
    @Parameter(name = "id", description = "领用记录ID", example = "700", required = true)
    public Result<AssetReceipt> getInfo(@PathVariable Long id) {
        return assetsReceiptService.getById(id);
    }

    /**
     * 新增资产领用记录
     */
    @PostMapping("/add")
    @Operation(summary = "新增领用记录", description = "创建新的资产领用记录")
    @Parameter(name = "assetsReceipt", description = "领用记录对象", required = true)
    public Result<Boolean> add(@RequestBody AssetReceipt assetsReceipt) {
        return assetsReceiptService.add(assetsReceipt);
    }

    /**
     * 修改资产领用记录
     */
    @PutMapping("/edit")
    @Operation(summary = "修改领用记录", description = "更新现有领用记录信息")
    @Parameter(name = "assetsReceipt", description = "领用记录对象", required = true)
    public Result<Boolean> edit(@RequestBody AssetReceipt assetsReceipt) {
        return assetsReceiptService.update(assetsReceipt);
    }

    /**
     * 删除资产领用记录
     */
    @DeleteMapping("/remove/{id}")
    @Operation(summary = "删除领用记录", description = "根据ID删除领用记录")
    @Parameter(name = "id", description = "领用记录ID", example = "700", required = true)
    public Result<Boolean> remove(@PathVariable Long id) {
        return assetsReceiptService.deleteById(id);
    }


    /**
     * 获取资产审批员列表
     */
    @GetMapping("/approvalUsers")
    @Operation(summary = "获取资产审批员列表", description = "查询角色为资产审批员的用户")
    public Result<List<SysUser>> getApprovalUsers() {
        return Result.success(sysUserService.getApprovalUsers());
    }

    @PostMapping("/approve")
    @Operation(summary = "审批领用申请")
    public Result<Boolean> approveReceipt(@RequestBody AssetReceiptRecordQuery AssetReceiptRecordQuery) {
        return assetsReceiptService.approveReceipt(AssetReceiptRecordQuery);
    }
} 