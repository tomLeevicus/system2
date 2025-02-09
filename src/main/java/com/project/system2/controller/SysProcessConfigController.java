package com.project.system2.controller;

import com.project.system2.common.core.domain.Result;
import com.project.system2.domain.entity.SysProcessConfig;
import com.project.system2.domain.entity.SysProcessVariableConfig;
import com.project.system2.service.ISysProcessConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/system/process-config")
@Tag(name = "流程配置", description = "系统流程配置管理接口")
public class SysProcessConfigController {
    
    @Autowired
    private ISysProcessConfigService processConfigService;
    
    /**
     * 获取流程配置列表
     */
    @GetMapping("/list")
    @Operation(summary = "获取流程配置列表", description = "查询系统流程配置列表")
    public Result<List<SysProcessConfig>> list(SysProcessConfig config) {
        List<SysProcessConfig> list = processConfigService.selectProcessConfigList(config);
        return Result.success(list);
    }
    
    /**
     * 获取流程参数配置
     */
    @GetMapping("/variables/{processKey}")
    @Operation(summary = "获取流程参数", description = "根据流程Key获取参数配置")
    @Parameter(name = "processKey", description = "流程标识", example = "ASSET_APPROVAL", required = true)
    public Result<List<SysProcessVariableConfig>> getVariables(@PathVariable String processKey) {
        List<SysProcessVariableConfig> list = processConfigService.selectVariableConfigList(processKey);
        return Result.success(list);
    }
    
    /**
     * 保存流程配置
     */
    @PostMapping("/save")
    @Operation(summary = "保存流程配置", description = "创建或更新流程配置")
    @Parameter(name = "config", description = "流程配置对象", required = true)
    public Result<Void> save(@RequestBody SysProcessConfig config) {
        processConfigService.saveProcessConfig(config);
        return Result.success();
    }
    
    /**
     * 保存流程参数配置
     */
    @PostMapping("/variables")
    @Operation(summary = "保存流程参数", description = "批量保存流程变量配置")
    @Parameter(name = "configs", description = "流程变量配置列表", required = true)
    public Result<Void> saveVariables(@RequestBody List<SysProcessVariableConfig> configs) {
        processConfigService.saveVariableConfigs(configs);
        return Result.success();
    }
} 