package com.project.system2.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.project.system2.common.core.domain.Result;
import com.project.system2.domain.dto.AssetScrapApprovalDTO;
import com.project.system2.domain.entity.AssetScrap;
import com.project.system2.domain.entity.AssetScrapRecord;
import com.project.system2.domain.query.AssetScrapPageQuery;
import com.project.system2.service.IAssetScrapService;
import com.project.system2.service.ISysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/asset/scrap")
@Tag(name = "资产报废管理")
@RequiredArgsConstructor
public class AssetScrapController {

    @Autowired
    private IAssetScrapService assetScrapService;

    /**
     * 资产报废
     */
    @PostMapping("/scrap")
    @Operation(summary = "资产报废", description = "处理资产报废请求")
    @Parameter(name = "assetScrap", description = "资产报废对象", required = true)
    public Result<Boolean> scrap(@RequestBody AssetScrap assetScrap) {
        return assetScrapService.scrapAsset(assetScrap);
    }

    @PostMapping("/approve")
    @Operation(summary = "审批报废申请")
    @PreAuthorize("@ss.hasPermi('asset:repair:approve')")
    public Result<Boolean> approveScrap(@RequestBody AssetScrapApprovalDTO dto) {
        return assetScrapService.approveScrap(dto);
    }

    @GetMapping("/page")
    @Operation(summary = "分页查询报废记录")
    public Result<IPage<AssetScrap>> pageList(@Valid AssetScrapPageQuery query) {
        return assetScrapService.pageScrapRecords(query);
    }
} 