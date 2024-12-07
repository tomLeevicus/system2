package com.project.system2.controller;

import com.project.system2.common.core.domain.Result;
import com.project.system2.service.IActProcessInstanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/workflow/instance")
public class ActProcessInstanceController {

    @Autowired
    private IActProcessInstanceService processInstanceService;

    /**
     * 启动流程实例
     */
    @PreAuthorize("@ss.hasPermi('workflow:instance:start')")
    @PostMapping("/start/{processDefinitionKey}")
    public Result<String> startProcess(@PathVariable String processDefinitionKey,
                                     @RequestBody Map<String, Object> variables) {
        String processInstanceId = processInstanceService.startProcess(processDefinitionKey, variables);
        return Result.success(processInstanceId);
    }

    /**
     * 挂起/激活流程实例
     */
    @PreAuthorize("@ss.hasPermi('workflow:instance:update')")
    @PutMapping("/state/{processInstanceId}/{state}")
    public Result<Void> updateState(@PathVariable String processInstanceId, @PathVariable boolean state) {
        processInstanceService.updateProcessInstanceState(processInstanceId, state);
        return Result.success();
    }

    /**
     * 删除流程实例
     */
    @PreAuthorize("@ss.hasPermi('workflow:instance:remove')")
    @DeleteMapping("/{processInstanceId}")
    public Result<Void> deleteProcessInstance(@PathVariable String processInstanceId, String deleteReason) {
        processInstanceService.deleteProcessInstance(processInstanceId, deleteReason);
        return Result.success();
    }

    /**
     * 获取流程实例详细信息
     */
    @PreAuthorize("@ss.hasPermi('workflow:instance:query')")
    @GetMapping("/{processInstanceId}")
    public Result<Map<String, Object>> getProcessInstanceDetail(@PathVariable String processInstanceId) {
        Map<String, Object> detail = processInstanceService.getProcessInstanceDetail(processInstanceId);
        return Result.success(detail);
    }
} 