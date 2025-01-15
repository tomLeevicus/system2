package com.project.system2.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.project.system2.common.core.domain.Result;
import com.project.system2.domain.entity.Assets;
import com.project.system2.domain.model.AssetsQuery;
import com.project.system2.service.IAssetsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/assets")
public class AssetsController {

    @Autowired
    private IAssetsService assetsService;

    @GetMapping("/list")
    public Result<List<Assets>> list() {
        return assetsService.listAssets();
    }

    @GetMapping("/{id}")
    public Result<Assets> getInfo(@PathVariable Long id) {
        return assetsService.getAssetById(id);
    }

    @PostMapping
    public Result<Boolean> add(@RequestBody Assets assets) {
        return assetsService.addAsset(assets);
    }

    @PutMapping
    public Result<Boolean> edit(@RequestBody Assets assets) {
        return assetsService.updateAsset(assets);
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> remove(@PathVariable Long id) {
        return assetsService.deleteAsset(id);
    }

    @PostMapping("/query")
    public Result<IPage<Assets>> query(@RequestBody AssetsQuery query) {
        return assetsService.queryAssetsList(query);
    }
} 