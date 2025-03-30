package com.project.system2.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.system2.common.core.domain.Result;
import com.project.system2.common.core.domain.PageQuery;
import com.project.system2.domain.entity.SysProcessConfig;
import com.project.system2.service.IProcessDefinitionConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/workflow/process-config")
@Tag(name = "流程配置管理", description = "流程定义配置管理接口")
public class ProcessDefinitionConfigController {
    
    private static final Logger log = LoggerFactory.getLogger(ProcessDefinitionConfigController.class);
    
    @Autowired
    private IProcessDefinitionConfigService processDefinitionConfigService;
    
    /**
     * 获取流程配置列表
     */
    @GetMapping("/list")
    @PreAuthorize("@ss.hasPermi('workflow:process-config:list')")
    @Operation(summary = "分页查询流程配置", description = "查询流程定义配置列表")
    public Result<Page<SysProcessConfig>> list(SysProcessConfig config, PageQuery pageQuery) {
        LambdaQueryWrapper<SysProcessConfig> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(config.getName() != null, SysProcessConfig::getName, config.getName())
               .like(config.getProcessKey() != null, SysProcessConfig::getProcessKey, config.getProcessKey())
               .like(config.getRoleKey() != null, SysProcessConfig::getRoleKey, config.getRoleKey())
               .eq(config.getStatus() != null, SysProcessConfig::getStatus, config.getStatus())
               .orderByAsc(SysProcessConfig::getProcessKey);
               
        Page<SysProcessConfig> page = processDefinitionConfigService.page(
            new Page<>(pageQuery.getPageNum(), pageQuery.getPageSize()), 
            wrapper
        );
        
        return Result.success(page);
    }
    
    /**
     * 获取流程配置详细信息
     */
    @GetMapping("/{id}")
    @PreAuthorize("@ss.hasPermi('workflow:process-config:query')")
    @Operation(summary = "获取流程配置详情", description = "根据ID查询流程配置")
    public Result<SysProcessConfig> getInfo(@PathVariable Long id) {
        return Result.success(processDefinitionConfigService.getById(id));
    }
    
    /**
     * 新增流程配置
     */
    @PostMapping
    @PreAuthorize("@ss.hasPermi('workflow:process-config:add')")
    @Operation(summary = "新增流程配置", description = "新增流程定义配置")
    public Result<Void> add(@RequestBody SysProcessConfig config) {
        // 检查流程键是否唯一
        LambdaQueryWrapper<SysProcessConfig> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysProcessConfig::getProcessKey, config.getProcessKey());
        if (processDefinitionConfigService.count(wrapper) > 0) {
            return Result.error("流程标识已存在");
        }
        
        if (processDefinitionConfigService.save(config)) {
            return Result.success();
        }
        return Result.error("新增流程配置失败");
    }
    
    /**
     * 修改流程配置
     */
    @PutMapping
    @PreAuthorize("@ss.hasPermi('workflow:process-config:edit')")
    @Operation(summary = "修改流程配置", description = "修改流程定义配置")
    public Result<Void> edit(@RequestBody SysProcessConfig config) {
        // 检查流程键是否唯一（排除自身）
        LambdaQueryWrapper<SysProcessConfig> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysProcessConfig::getProcessKey, config.getProcessKey())
               .ne(SysProcessConfig::getId, config.getId());
        if (processDefinitionConfigService.count(wrapper) > 0) {
            return Result.error("流程标识已存在");
        }
        
        if (processDefinitionConfigService.updateById(config)) {
            return Result.success();
        }
        return Result.error("修改流程配置失败");
    }
    
    /**
     * 删除流程配置
     */
    @DeleteMapping("/{ids}")
    @PreAuthorize("@ss.hasPermi('workflow:process-config:remove')")
    @Operation(summary = "删除流程配置", description = "删除流程定义配置")
    public Result<Void> remove(@PathVariable Long[] ids) {
        if (processDefinitionConfigService.removeByIds(Arrays.asList(ids))) {
            return Result.success();
        }
        return Result.error("删除流程配置失败");
    }
    
    /**
     * 获取所有有效的流程配置（用于下拉选择）
     */
    @GetMapping("/all")
    @Operation(summary = "获取所有有效流程配置", description = "获取所有状态正常的流程配置，用于下拉选择")
    public Result<List<SysProcessConfig>> getAllConfigs() {
        LambdaQueryWrapper<SysProcessConfig> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysProcessConfig::getStatus, "0") // 只获取正常状态
               .orderByAsc(SysProcessConfig::getProcessKey);
        return Result.success(processDefinitionConfigService.list(wrapper));
    }
} 