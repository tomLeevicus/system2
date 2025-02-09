package com.project.system2.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.system2.common.core.domain.Result;
import com.project.system2.common.core.utils.SecurityUtils;
import com.project.system2.domain.entity.ActProcessInstance;
import com.project.system2.service.IActProcessInstanceService;
import lombok.extern.slf4j.Slf4j;
import org.flowable.common.engine.api.FlowableException;
import org.flowable.common.engine.api.FlowableObjectNotFoundException;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StringUtils;
import org.flowable.common.engine.impl.identity.Authentication;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.Date;

@Slf4j
@RestController
@RequestMapping("/workflow/instance")
@Tag(name = "流程实例", description = "工作流程实例管理接口")
public class ActProcessInstanceController {

    @Autowired
    private IActProcessInstanceService processInstanceService;
    
    @Autowired
    private TaskService taskService;
    
    @Autowired
    private RuntimeService runtimeService;

    /**
     * 启动流程实例（迁移自ActProcessDefinitionController）
     */
    @PostMapping("/start/{processKey}")
//    @PreAuthorize("@ss.hasPermi('workflow:instance:start')")
    @Operation(summary = "启动流程实例", description = "根据流程Key启动新的流程实例")
    public Result<Map<String, Object>> startProcess(
        @PathVariable String processKey,
        @RequestBody Map<String, Object> requestBody) {
        
        try {
            // 获取当前登录用户ID
            String userId = SecurityUtils.getUserId().toString();
            
            Map<String, Object> variables = (Map<String, Object>) requestBody.get("variables");
            variables.put("userId", userId);

            // 初始化leaderId变量
            variables.putIfAbsent("leaderId", "default_leader_id");

            // 启动流程实例
            ProcessInstance instance = runtimeService.startProcessInstanceByKey(
                processKey, 
                (String) requestBody.get("businessKey"),
                variables
            );

            // 设置流程实例名称
            runtimeService.setProcessInstanceName(instance.getId(), (String) variables.get("processName"));

            // 补充实例信息到自定义表
            ActProcessInstance customInstance = new ActProcessInstance();
            customInstance.setId(instance.getId());
            customInstance.setProcessDefinitionId(instance.getProcessDefinitionId());
            customInstance.setProcessDefinitionKey(processKey);
            customInstance.setStartUserId(SecurityUtils.getUserId().toString());
            customInstance.setStatus("running");
            customInstance.setProcessDefinitionVersion(instance.getProcessDefinitionVersion());
            customInstance.setStartTime(instance.getStartTime());
            customInstance.setStartUserId(SecurityUtils.getUserId().toString());
            processInstanceService.syncInstance(customInstance);

            Map<String, Object> result = new HashMap<>();
            result.put("processInstanceId", instance.getId());
            result.put("processDefinitionId", instance.getProcessDefinitionId());
            result.put("businessKey", instance.getBusinessKey());
            
            return Result.success(result);
        } catch (FlowableObjectNotFoundException e) {
            log.error("流程不存在: {}", processKey, e);
            return Result.error("流程不存在");
        } catch (FlowableException e) {
            log.error("流程启动失败", e);
            return Result.error("流程启动失败: " + e.getMessage());
        }
    }

    /**
     * 完成任务（迁移自ActProcessDefinitionController）
     */
    @PostMapping("/task/complete")
//    @PreAuthorize("@ss.hasPermi('workflow:instance:complete')")
    @Operation(summary = "完成任务", description = "处理流程任务并推进流程")
    public Result<Void> completeTask(@RequestBody Map<String, Object> requestBody) {
        try {
            String taskId = (String) requestBody.get("taskId");
            Map<String, Object> variables = (Map<String, Object>) requestBody.get("variables");
            
            // 确保传递leaderId参数
            if (variables == null) {
                variables = new HashMap<>();
            }
            
            // 校验领导ID是否合法
            String leaderId = (String) variables.get("leaderId");
            String currentUserId = SecurityUtils.getUserId().toString();
            
            if (StringUtils.isEmpty(leaderId)) {
                return Result.error("审核人不能为空");
            }
            if (leaderId.equals(currentUserId)) {
                return Result.error("不能指定自己为审核人");
            }

            if (StringUtils.isEmpty(taskId)) {
                return Result.error("任务ID不能为空");
            }
            processInstanceService.completeTask(taskId, variables);
            return Result.success();
        } catch (Exception e) {
            log.error("完成任务失败", e);
            return Result.error("完成任务失败: " + e.getMessage());
        }
    }

    /**
     * 获取流程实例状态（迁移自ActProcessDefinitionController）
     */
    @GetMapping("/{processInstanceId}/status")
    @PreAuthorize("@ss.hasPermi('workflow:instance:query')")
    @Operation(summary = "获取流程状态", description = "查询流程实例的当前状态和任务信息")
    @Parameter(name = "processInstanceId", description = "流程实例ID", example = "PI_1001", required = true)
    public Result<Map<String, Object>> getProcessInstanceStatus(@PathVariable String processInstanceId) {
        try {
            Map<String, Object> result = new HashMap<>();
            
            // 查询流程实例
            ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult();
                
            // 查询当前任务
            List<Task> tasks = taskService.createTaskQuery()
                .processInstanceId(processInstanceId)
                .list();
                
            if (processInstance != null) {
                result.put("status", "running");
                result.put("processInstanceId", processInstance.getId());
                result.put("processDefinitionId", processInstance.getProcessDefinitionId());
                result.put("startTime", processInstance.getStartTime());
                
                // 获取流程变量
                Map<String, Object> variables = runtimeService.getVariables(processInstanceId);
                result.put("variables", variables);
                
                // 当前任务信息
                List<Map<String, Object>> taskInfos = tasks.stream()
                    .map(task -> {
                        Map<String, Object> taskMap = new HashMap<>();
                        taskMap.put("taskId", task.getId());
                        taskMap.put("taskName", task.getName());
                        taskMap.put("assignee", task.getAssignee());
                        taskMap.put("createTime", task.getCreateTime());
                        return taskMap;
                    })
                    .collect(Collectors.toList());
                result.put("currentTasks", taskInfos);
            } else {
                result.put("status", "completed");
            }
            
            return Result.success(result);
        } catch (Exception e) {
            log.error("获取流程实例状态失败", e);
            return Result.error("获取流程实例状态失败：" + e.getMessage());
        }
    }

    /**
     * 获取用户待办任务（迁移自ActProcessDefinitionController）
     */
    @GetMapping("/task/todo")
    @PreAuthorize("@ss.hasPermi('workflow:task:todo')")
    @Operation(summary = "获取待办任务", description = "查询当前用户的待办任务列表")
    public Result<Page<ActProcessInstance>> getTodoTasks(
        @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
        @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") Integer pageSize) {
        try {
            String userId = SecurityUtils.getUserId().toString();
            Page<ActProcessInstance> page = new Page<>(pageNum, pageSize);
            page = processInstanceService.getTodoInstances(page, userId);
            return Result.success(page);
        } catch (Exception e) {
            log.error("获取用户待办任务失败", e);
            return Result.error("获取用户待办任务失败: " + e.getMessage());
        }
    }

    /**
     * 分页查询流程实例
     */
    @PreAuthorize("@ss.hasPermi('workflow:instance:list')")
    @GetMapping("/list")
    @Operation(summary = "分页查询流程实例", description = "查询运行中的流程实例")
    @Parameter(name = "processName", description = "流程名称", example = "资产审批流程")
    public Result<Page<ActProcessInstance>> list(ActProcessInstance processInstance,
                                               @RequestParam(defaultValue = "1") Integer pageNum,
                                               @RequestParam(defaultValue = "10") Integer pageSize) {
        try {
            Page<ActProcessInstance> page = new Page<>(pageNum, pageSize);
            page = processInstanceService.listProcessInstances(page, processInstance);
            return Result.success(page);
        } catch (Exception e) {
            log.error("查询流程实例列表失败", e);
            return Result.error("查询流程实例列表失败：" + e.getMessage());
        }
    }

    /**
     * 获取流程实例详情
     */
    @PreAuthorize("@ss.hasPermi('workflow:instance:query')")
    @GetMapping("/{processInstanceId}")
    @Operation(summary = "获取流程实例详情", description = "根据流程实例ID获取详细信息")
    @Parameter(name = "processInstanceId", description = "流程实例ID", example = "12345", required = true)
    public Result<ActProcessInstance> getInfo(@PathVariable String processInstanceId) {
        try {
            ActProcessInstance instance = processInstanceService.getProcessInstanceById(processInstanceId);
            return Result.success(instance);
        } catch (Exception e) {
            log.error("获取流程实例详情失败", e);
            return Result.error("获取流程实例详情失败：" + e.getMessage());
        }
    }

    /**
     * 获取流程实例变量
     */
    @GetMapping("/{processInstanceId}/variables")
    @PreAuthorize("@ss.hasPermi('workflow:instance:query')")
    @Operation(summary = "获取流程变量", description = "查询流程实例的运行时变量")
    @Parameter(name = "processInstanceId", description = "流程实例ID", example = "PI_1001", required = true)
    public Result<Map<String, Object>> getVariables(@PathVariable String processInstanceId) {
        try {
            Map<String, Object> variables = processInstanceService.getProcessInstanceVariables(processInstanceId);
            return Result.success(variables);
        } catch (Exception e) {
            log.error("获取流程实例变量失败", e);
            return Result.error("获取流程实例变量失败：" + e.getMessage());
        }
    }

    /**
     * 挂起/激活流程实例
     */
    @PreAuthorize("@ss.hasPermi('workflow:instance:update')")
    @PutMapping("/state/{processInstanceId}/{state}")
    @Operation(summary = "更新流程实例状态", description = "挂起或激活流程实例")
    @Parameter(name = "processInstanceId", description = "流程实例ID", example = "12345", required = true)
    @Parameter(name = "state", description = "状态(true:激活, false:挂起)", example = "true", required = true)
    public Result<Void> updateState(@PathVariable String processInstanceId, @PathVariable boolean state) {
        try {
            processInstanceService.updateProcessInstanceState(processInstanceId, state);
            return Result.success();
        } catch (Exception e) {
            log.error("更新流程实例状态失败", e);
            return Result.error("更新流程实例状态失败：" + e.getMessage());
        }
    }

    /**
     * 删除流程实例
     */
    @PreAuthorize("@ss.hasPermi('workflow:instance:remove')")
    @DeleteMapping("/{processInstanceId}")
    @Operation(summary = "删除流程实例", description = "终止并删除流程实例")
    @Parameter(name = "processInstanceId", description = "流程实例ID", example = "12345", required = true)
    @Parameter(name = "deleteReason", description = "删除原因", example = "误操作需要删除")
    public Result<Void> remove(@PathVariable String processInstanceId, String deleteReason) {
        try {
            processInstanceService.deleteProcessInstance(processInstanceId, deleteReason);
            return Result.success();
        } catch (Exception e) {
            log.error("删除流程实例失败", e);
            return Result.error("删除流程实例失败：" + e.getMessage());
        }
    }

    @GetMapping("/diagram/{processInstanceId}")
    @Operation(summary = "获取流程图", description = "根据流程实例ID获取流程图")
    @Parameter(name = "processInstanceId", description = "流程实例ID", example = "12345", required = true)
    public Result getDiagram(@PathVariable String processInstanceId) {
        // ...
        return null; // Placeholder return, actual implementation needed
    }

    @PreAuthorize("@ss.hasPermi('workflow:instance:todo')")
    @GetMapping("/todo")
    @Operation(summary = "获取待办流程", description = "查询当前用户的待办流程实例")
    public Result<Page<ActProcessInstance>> getTodoInstances(
        @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
        @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") Integer pageSize) {
        
        Page<ActProcessInstance> page = new Page<>(pageNum, pageSize);
        return Result.success(processInstanceService.getTodoInstances(page, SecurityUtils.getUserId().toString()));
    }

    /*@GetMapping("/history")
    @PreAuthorize("@ss.hasPermi('workflow:instance:history')")
    @Operation(summary = "获取历史流程", description = "查询已完成的流程实例历史记录")
    @Parameter(name = "pageNum", description = "页码", example = "1")
    @Parameter(name = "pageSize", description = "每页数量", example = "10")
    public Result<Page<ActProcessInstance>> getHistoryInstances(
        @RequestParam(defaultValue = "1") Integer pageNum,
        @RequestParam(defaultValue = "10") Integer pageSize) {
        // ...
    }

    @PostMapping("/comment")
    @PreAuthorize("@ss.hasPermi('workflow:instance:comment')")
    @Operation(summary = "添加流程评论", description = "为流程实例添加处理意见")
    @Parameter(name = "processInstanceId", description = "流程实例ID", example = "PI_1001", required = true)
    @Parameter(name = "comment", description = "评论内容", example = "同意申请", required = true)
    public Result<Void> addComment(@RequestParam String processInstanceId, @RequestParam String comment) {
        // ...
    }

    @PostMapping("/attachment")
    @PreAuthorize("@ss.hasPermi('workflow:instance:attachment')")
    @Operation(summary = "添加流程附件", description = "为流程实例上传相关附件")
    @Parameter(name = "processInstanceId", description = "流程实例ID", example = "PI_1001", required = true)
    @Parameter(name = "file", description = "附件文件", required = true)
    public Result<Void> addAttachment(@RequestParam String processInstanceId, @RequestParam MultipartFile file) {
        // ...
    }*/
} 