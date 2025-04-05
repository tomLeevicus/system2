package com.project.system2.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.system2.common.core.domain.Result;
import com.project.system2.common.core.domain.PageQuery;
import com.project.system2.common.core.utils.SecurityUtils;
import com.project.system2.common.core.utils.StringUtils;
import com.project.system2.domain.ProcessDefinitionConfig;
import com.project.system2.domain.entity.ActProcessDefinition;
import com.project.system2.domain.entity.ActProcessInstance;
import com.project.system2.domain.entity.ActTaskInfo;
import com.project.system2.service.IActProcessDefinitionService;
import com.project.system2.service.IActProcessInstanceService;
import org.flowable.common.engine.api.FlowableException;
import org.flowable.common.engine.api.FlowableObjectNotFoundException;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.flowable.engine.HistoryService;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.repository.Deployment;
import org.springframework.beans.factory.annotation.Value;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.ArrayList;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.flowable.engine.IdentityService;
import com.project.system2.domain.entity.SysProcessConfig;

@RestController
@RequestMapping("/workflow/process")
@Tag(name = "流程定义", description = "工作流程定义管理接口")
public class ActProcessDefinitionController {

    private static final Logger log = LoggerFactory.getLogger(ActProcessDefinitionController.class);

    @Autowired
    private IActProcessDefinitionService processDefinitionService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private IActProcessInstanceService processInstanceService;

    @Autowired
    private Map<String, SysProcessConfig> processConfigs;

    /*@Autowired
    private IdentityService identityService;*/

    private static final String PROCESS_UPLOAD_DIR = "processes/";

    @Value("${flowable.process-def-location}")
    private String processStorageLocation;

    /**
     * 获取流程定义列表
     */
    @PreAuthorize("@ss.hasPermi('workflow:process:list')")
    @GetMapping("/list")
    @Operation(summary = "分页查询流程定义", description = "查询部署的流程定义列表")
    @Parameter(name = "processName", description = "流程名称", example = "资产审批流程")
    public Result<Page<ActProcessDefinition>> list(ActProcessDefinition processDefinition, PageQuery pageQuery) {
        Page<ActProcessDefinition> page = new Page<>(pageQuery.getPageNum(), pageQuery.getPageSize());
        page = processDefinitionService.selectProcessDefinitionPage(page, processDefinition);
        return Result.success(page);
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
     * 直接部署流程定义（不保存文件）
     */
    @PreAuthorize("@ss.hasPermi('workflow:process:deploy')")
    @PostMapping("/deploy/direct")
    @Operation(summary = "直接部署流程", description = "上传并部署BPMN流程定义文件")
    public Result<String> deployDirect(@RequestParam("file") MultipartFile file,
                                     @RequestParam(required = false) String category) {
        try {
            // 基础校验
            if (file == null || file.isEmpty()) {
                return Result.error("上传文件不能为空");
            }
            
            // 校验文件类型
            String fileName = file.getOriginalFilename();
            if (fileName == null || 
                (!fileName.toLowerCase().endsWith(".bpmn") && 
                 !fileName.toLowerCase().endsWith(".bpmn20.xml"))) {
                return Result.error("只支持 .bpmn 或 .bpmn20.xml 格式的文件");
            }

            // 执行部署
            String deploymentId = processDefinitionService.deployProcessDefinition(
                fileName,  // 使用文件名作为流程名称
                StringUtils.defaultString(category, "default"),
                file
            );
            
            // 获取部署后的流程定义信息
            ActProcessDefinition definition = processDefinitionService.getByDeploymentId(deploymentId);
            
            return Result.success("部署成功", definition != null ? definition.getId() : deploymentId);
        } catch (Exception e) {
            log.error("直接部署流程失败", e);
            return Result.error("部署失败: " + e.getMessage());
        }
    }

    /**
     * 从已上传文件部署流程定义
     */
    @PreAuthorize("@ss.hasPermi('workflow:process:deploy')")
    @PostMapping("/deploy/storage/{fileName}")
    public Result<String> deployFromStorage(@PathVariable String fileName,
                                          @RequestParam(required = false) String category) {
        try {
            // 验证文件名安全
            if (!fileName.matches("[a-zA-Z0-9_\\-.]+\\.bpmn20\\.xml")) {
                return Result.error("文件名不合法");
            }
            
            Path filePath = Paths.get(processStorageLocation, fileName).normalize();
            
            // 防止路径遍历
            if (!filePath.startsWith(Paths.get(processStorageLocation))) {
                return Result.error("非法文件路径");
            }
            
            // 检查文件是否存在
            if (!Files.exists(filePath)) {
                return Result.error("文件不存在: " + fileName);
            }

            // 执行部署
            try (InputStream inputStream = Files.newInputStream(filePath)) {
                Deployment deployment = repositoryService.createDeployment()
                    .name(fileName)
                    .category(StringUtils.defaultString(category, "default"))
                    .addInputStream(fileName, inputStream)
                    .deploy();
                    
                // 保存部署记录到数据库
                ActProcessDefinition definition = new ActProcessDefinition();
                ProcessDefinition pd = repositoryService.createProcessDefinitionQuery()
                    .deploymentId(deployment.getId())
                    .singleResult();
                    
                definition.setId(pd.getId());
                definition.setName(pd.getName());
                definition.setProcessKey(pd.getKey());
                definition.setCategory(pd.getCategory());
                definition.setVersion(pd.getVersion());
                definition.setDeploymentId(deployment.getId());
                definition.setResourceName(pd.getResourceName());
                definition.setDiagramResourceName(pd.getDiagramResourceName());
                processDefinitionService.insertProcessDefinition(definition);
                
                return Result.success("部署成功", deployment.getId());
            }
        } catch (Exception e) {
            log.error("从存储部署流程失败", e);
            return Result.error("部署失败: " + e.getMessage());
        }
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
    @GetMapping("/image/{processInstanceId}")
    public Result<byte[]> getProcessImage(@PathVariable String processInstanceId) {
        try {
            log.info("开始获取流程图 - 流程实例ID: {}", processInstanceId);
            
            String processDefinitionId = null;
            
            // 优先从历史表中查询
            HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult();
                
            if (historicProcessInstance != null) {
                processDefinitionId = historicProcessInstance.getProcessDefinitionId();
                log.info("从历史记录中找到流程定义ID: {}", processDefinitionId);
            } else {
                // 如果历史表中没有，再从运行时表查询
                ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                    .processInstanceId(processInstanceId)
                    .singleResult();
                    
                if (processInstance != null) {
                    processDefinitionId = processInstance.getProcessDefinitionId();
                    log.info("从运行时表中找到流程定义ID: {}", processDefinitionId);
                }
            }
            
            if (processDefinitionId == null) {
                log.error("流程实例不存在: {}", processInstanceId);
                return Result.error("流程实例不存在");
            }
            
            // 查询流程定义
            ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionId(processDefinitionId)
                .singleResult();
                
            if (processDefinition == null) {
                log.error("流程定义不存在: {}", processDefinitionId);
                return Result.error("流程定义不存在");
            }
            
            log.info("流程定义信息 - deploymentId: {}, resourceName: {}, diagramResourceName: {}", 
                processDefinition.getDeploymentId(),
                processDefinition.getResourceName(),
                processDefinition.getDiagramResourceName());
                
            byte[] image = processDefinitionService.getProcessDefinitionImage(processDefinitionId);
            if (image == null) {
                return Result.error("未找到流程图");
            }
            
            log.info("成功获取流程图，大小: {} bytes", image.length);
            return Result.success(image);
        } catch (Exception e) {
            log.error("获取流程图失败", e);
            return Result.error("获取流程图失败：" + e.getMessage());
        }
    }

    /**
     * 获取流程定义XML
     */
    @GetMapping("/xml/{processDefinitionId}")
    @Operation(summary = "获取流程BPMN XML", description = "根据流程定义ID获取BPMN文件内容")
    @Parameter(name = "processDefinitionId", description = "流程定义ID", example = "asset-approval:1", required = true)
    public Result<String> getProcessXml(@PathVariable String processDefinitionId) {
        String xml = processDefinitionService.getProcessDefinitionXML(processDefinitionId);
        return Result.success(xml);
    }

    /**
     * 获取审批人列表
     */
    @GetMapping("/task/{taskId}/approvers")
    @Operation(summary = "获取任务审批人", description = "根据任务ID获取可选的审批人列表")
    @Parameter(name = "taskId", description = "任务ID", example = "task-1001", required = true)
    public Result<List<Map<String, Object>>> getApprovers(@PathVariable String taskId) {
        try {
            // 获取任务信息
            Task task = taskService.createTaskQuery()
                .taskId(taskId)
                .singleResult();

            if (task == null) {
                return Result.error("任务不存在");
            }

            // 获取流程定义信息
            ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionId(task.getProcessDefinitionId())
                .singleResult();

            if (processDefinition == null) {
                return Result.error("流程定义不存在");
            }

            // 获取流程配置
            SysProcessConfig config = processConfigs.get(processDefinition.getKey());
            if (config == null) {
                return Result.error("不支持的流程类型");
            }

            // 根据角色获取审批人列表
            List<Map<String, Object>> approvers = processDefinitionService.getApproversByRoleKey(config.getRoleKey());
            return Result.success(approvers);
        } catch (Exception e) {
            log.error("获取审批人列表失败", e);
            return Result.error("获取审批人列表失败：" + e.getMessage());
        }
    }

    /**
     * 上传流程图文件
     */
    @PreAuthorize("@ss.hasPermi('workflow:process:upload')")
    @PostMapping("/upload")
    @Operation(summary = "上传流程文件", description = "上传BPMN流程定义文件")
    @Parameter(name = "file", description = "BPMN文件", required = true)
    public Result<String> uploadProcessFile(@RequestParam("file") MultipartFile file) {
        try {
            // 验证文件类型
            String fileName = file.getOriginalFilename();
            if (fileName == null || !fileName.toLowerCase().endsWith(".bpmn20.xml")) {
                return Result.error("只支持 .bpmn20.xml 格式的文件");
            }

            // 创建绝对路径并标准化
            Path storagePath = Paths.get(processStorageLocation).normalize().toAbsolutePath();
            if (!Files.exists(storagePath)) {
                Files.createDirectories(storagePath);
            }

            // 生成安全文件名并验证路径
            String safeFileName = fileName.replaceAll("[^a-zA-Z0-9.-]", "_");
            Path targetLocation = storagePath.resolve(safeFileName).normalize();
            
            // 防止路径遍历攻击
            if (!targetLocation.startsWith(storagePath)) {
                throw new IOException("非法文件路径: " + targetLocation);
            }

            // 保存文件
            file.transferTo(targetLocation);
            
            log.info("流程图文件上传成功，保存路径：{}", targetLocation);
            return Result.success("文件上传成功");
        } catch (IOException e) {
            log.error("文件上传失败", e);
            return Result.error("文件上传失败：" + e.getMessage());
        } catch (Exception e) {
            log.error("处理文件时发生错误", e);
            return Result.error("处理文件时发生错误：" + e.getMessage());
        }
    }

    /**
     * 获取已上传的流程图列表
     */
    @PreAuthorize("@ss.hasPermi('workflow:process:list')")
    @GetMapping("/uploaded")
    @Operation(summary = "获取已上传文件", description = "获取已上传的流程文件列表")
    public Result<List<String>> getUploadedProcessFiles() {
        try {
            Path dirPath = Paths.get(processStorageLocation);
            if (!Files.exists(dirPath)) {
                return Result.success(new ArrayList<>());
            }

            List<String> fileList = Files.list(dirPath)
                .filter(path -> path.toString().endsWith(".bpmn20.xml"))
                .map(Path::getFileName)
                .map(Path::toString)
                .collect(Collectors.toList());

            return Result.success(fileList);
        } catch (IOException e) {
            log.error("获取文件列表失败", e);
            return Result.error("获取文件列表失败：" + e.getMessage());
        }
    }

    /**
     * 获取已部署流程定义列表
     */
    @PreAuthorize("@ss.hasPermi('workflow:process:list')")
    @GetMapping("/deployed-list")
    @Operation(summary = "获取已部署流程列表", description = "获取所有已部署的流程定义列表")
    public Result<List<Map<String, Object>>> getDeployedProcessList() {
        try {
            List<ProcessDefinition> processDefinitions = repositoryService.createProcessDefinitionQuery()
                .latestVersion()
                .orderByProcessDefinitionKey().asc()
                .orderByProcessDefinitionVersion().desc()
                .list();

            List<Map<String, Object>> result = processDefinitions.stream().map(pd -> {
                Map<String, Object> processInfo = new HashMap<>();
                processInfo.put("id", pd.getId());
                processInfo.put("name", pd.getName());
                processInfo.put("key", pd.getKey());
                processInfo.put("version", pd.getVersion());
                processInfo.put("deploymentId", pd.getDeploymentId());
                processInfo.put("resourceName", pd.getResourceName());
                processInfo.put("suspended", pd.isSuspended());
                return processInfo;
            }).collect(Collectors.toList());

            return Result.success(result);
        } catch (FlowableException e) {
            log.error("获取已部署流程列表失败", e);
            return Result.error("获取流程列表失败: " + e.getMessage());
        }
    }

    /**
     * 获取用户待办任务（迁移自ActProcessDefinitionController）
     */
    @GetMapping("/tasks/todo")
    @PreAuthorize("@ss.hasPermi('workflow:task:todo')")
    @Operation(summary = "获取待办任务", description = "查询当前用户的待办任务列表")
    public Result<Page<ActTaskInfo>> getTodoTasks(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") Integer pageSize) {
        try {
            String userId = SecurityUtils.getUserId().toString();
            Page<ActTaskInfo> page = new Page<>(pageNum, pageSize);
            page = processInstanceService.getTodoInstances(page, userId);
            return Result.success(page);
        } catch (Exception e) {
            log.error("获取用户待办任务失败", e);
            return Result.error("获取用户待办任务失败: " + e.getMessage());
        }
    }

    /**
     * 通过流程定义ID直接获取流程图
     */
    @GetMapping("/deployGetImage/{id}")
    @Operation(summary = "获取流程定义图像", description = "通过流程定义ID直接获取流程图，无需流程实例")
    @Parameter(name = "processDefinitionId", description = "流程定义ID", example = "asset-approval:1", required = true)
    public Result<byte[]> getProcessDefinitionImage(@PathVariable String id) {
        try {
            log.info("开始获取流程定义图像 - 流程定义ID: {}", id);
            
            // 查询流程定义
            ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionId(id)
                .singleResult();
                
            if (processDefinition == null) {
                log.error("流程定义不存在: {}", id);
                return Result.error("流程定义不存在");
            }
            
            log.info("流程定义信息 - deploymentId: {}, resourceName: {}, diagramResourceName: {}", 
                processDefinition.getDeploymentId(),
                processDefinition.getResourceName(),
                processDefinition.getDiagramResourceName());
                
            byte[] image = processDefinitionService.getProcessDefinitionImage(id);
            if (image == null) {
                return Result.error("未找到流程图");
            }
            
            log.info("成功获取流程图，大小: {} bytes", image.length);
            return Result.success(image);
        } catch (Exception e) {
            log.error("获取流程图失败", e);
            return Result.error("获取流程图失败：" + e.getMessage());
        }
    }
} 