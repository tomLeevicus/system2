package com.project.system2.controller;

import com.project.system2.common.core.domain.Result;
import com.project.system2.common.core.utils.SecurityUtils;
import com.project.system2.domain.entity.ActProcessDefinition;
import com.project.system2.service.IActProcessDefinitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/workflow/process")
public class ActProcessDefinitionController {

    @Autowired
    private IActProcessDefinitionService processDefinitionService;

    /**
     * 获取流程定义列表
     */
    @PreAuthorize("@ss.hasPermi('workflow:process:list')")
    @GetMapping("/list")
    public Result<List<ActProcessDefinition>> list(ActProcessDefinition processDefinition) {
        List<ActProcessDefinition> list = processDefinitionService.selectProcessDefinitionList(processDefinition);
        return Result.success(list);
    }

    /**
     * 获取流程定义详细信息
     */
    @PreAuthorize("@ss.hasPermi('workflow:process:query')")
    @GetMapping(value = "/{processDefinitionId}")
    public Result<ActProcessDefinition> getInfo(@PathVariable String processDefinitionId) {
        return Result.success(processDefinitionService.selectProcessDefinitionById(processDefinitionId));
    }

    /**
     * 部署流程定义
     */
    @PreAuthorize("@ss.hasPermi('workflow:process:deploy')")
    @PostMapping("/deploy")
    public Result<String> deployProcess(@RequestParam("name") String name,
                                      @RequestParam("category") String category,
                                      @RequestParam("file") MultipartFile file,
                                      @RequestParam(value = "reviewers", required = false) List<String> reviewers) {
        String deploymentId = processDefinitionService.deployProcessDefinition(name, category, file, reviewers);
        return Result.success(deploymentId);
    }

    /**
     * 删除流程定义
     */
    @PreAuthorize("@ss.hasPermi('workflow:process:remove')")
    @DeleteMapping("/{deploymentId}")
    public Result<Void> remove(@PathVariable String deploymentId) {
        processDefinitionService.deleteProcessDefinition(deploymentId);
        return Result.success();
    }

    /**
     * 激活/挂起流程定义
     */
    @PreAuthorize("@ss.hasPermi('workflow:process:update')")
    @PutMapping("/state/{processDefinitionId}/{state}")
    public Result<Void> updateState(@PathVariable String processDefinitionId, @PathVariable boolean state) {
        processDefinitionService.updateProcessDefinitionState(processDefinitionId, state);
        return Result.success();
    }

    /**
     * 获取流程定义图像
     */
    @GetMapping("/image/{processDefinitionId}")
    public Result<byte[]> getProcessImage(@PathVariable String processDefinitionId) {
        byte[] image = processDefinitionService.getProcessDefinitionImage(processDefinitionId);
        return Result.success(image);
    }

    /**
     * 获取流程定义XML
     */
    @GetMapping("/xml/{processDefinitionId}")
    public Result<String> getProcessXml(@PathVariable String processDefinitionId) {
        String xml = processDefinitionService.getProcessDefinitionXML(processDefinitionId);
        return Result.success(xml);
    }

    /**
     * 获取当前用户待审核的流程列表
     */
    @GetMapping("/my-review-tasks")
    public Result<List<ActProcessDefinition>> getMyReviewTasks() {
        // 获取当前登录用户
        String currentUserId = SecurityUtils.getUserId().toString();
        List<ActProcessDefinition> tasks = processDefinitionService.getProcessDefinitionsByReviewer(currentUserId);
        return Result.success(tasks);
    }
} 