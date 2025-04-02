package com.project.system2.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.system2.common.core.utils.EntityUtils;
import com.project.system2.common.core.utils.SecurityUtils;
import com.project.system2.common.core.utils.StringUtils;
import com.project.system2.domain.entity.ActProcessDefinition;
import com.project.system2.domain.entity.ProcessReviewer;
import com.project.system2.domain.entity.SysUser;
import com.project.system2.domain.entity.SysProcessConfig;
import com.project.system2.domain.entity.SysProcessVariableConfig;
import com.project.system2.mapper.ActProcessDefinitionMapper;
import com.project.system2.mapper.ProcessReviewerMapper;
import com.project.system2.mapper.SysUserMapper;
import com.project.system2.mapper.SysProcessConfigMapper;
import com.project.system2.mapper.SysProcessVariableConfigMapper;
import com.project.system2.service.IActProcessDefinitionService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.IOUtils;
import org.flowable.common.engine.api.FlowableException;
import org.flowable.common.engine.impl.identity.Authentication;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.flowable.bpmn.converter.BpmnXMLConverter;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.FormProperty;
import org.flowable.bpmn.model.FlowElement;
import org.flowable.bpmn.model.Process;
import org.flowable.bpmn.model.UserTask;
import org.flowable.bpmn.model.ExtensionAttribute;

@Slf4j
@Service
public class ActProcessDefinitionServiceImpl implements IActProcessDefinitionService {

    @Autowired
    private     RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private ProcessReviewerMapper processReviewerMapper;

    @Autowired
    private ActProcessDefinitionMapper processDefinitionMapper;

    @Autowired
    private SysUserMapper userMapper;

    @Autowired
    private SysProcessConfigMapper processConfigMapper;

    @Autowired
    private SysProcessVariableConfigMapper variableConfigMapper;

    private static final String AUTO_COMPLETE_TASK_KEY = "userApply"; // 假设这是用户申请节点的task key

    @Override
    public Page<ActProcessDefinition> selectProcessDefinitionPage(Page<ActProcessDefinition> page, ActProcessDefinition processDefinition) {
        return processDefinitionMapper.selectPage(page, new LambdaQueryWrapper<ActProcessDefinition>()
                .like(StringUtils.isNotBlank(processDefinition.getName()), ActProcessDefinition::getName, processDefinition.getName())
            .like(StringUtils.isNotBlank(processDefinition.getProcessKey()), ActProcessDefinition::getProcessKey, processDefinition.getProcessKey())
            .like(StringUtils.isNotBlank(processDefinition.getCategory()), ActProcessDefinition::getCategory, processDefinition.getCategory())
            .orderByDesc(ActProcessDefinition::getCreateTime));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String deployProcessDefinition(String name, String category, MultipartFile file) {
        try {
            // 执行流程部署
            Deployment deployment = repositoryService.createDeployment()
                .name(name)
                .category(category)
                .addInputStream(file.getOriginalFilename(), file.getInputStream())
                .deploy();
            
            // 获取部署后的流程定义
            ProcessDefinition definition = repositoryService.createProcessDefinitionQuery()
                .deploymentId(deployment.getId())
                .singleResult();
            
            if (definition != null) {
                // 保存流程定义信息到自定义表
                ActProcessDefinition actProcessDefinition = new ActProcessDefinition();
                actProcessDefinition.setId(definition.getId());
                actProcessDefinition.setName(definition.getName());
                actProcessDefinition.setProcessKey(definition.getKey());
                actProcessDefinition.setCategory(definition.getCategory());
                actProcessDefinition.setVersion(definition.getVersion());
                actProcessDefinition.setDeploymentId(definition.getDeploymentId());
                actProcessDefinition.setResourceName(definition.getResourceName());
                actProcessDefinition.setDiagramResourceName(getProcessDiagramResourceName(definition));
                actProcessDefinition.setDescription(definition.getDescription());
                actProcessDefinition.setSuspended(definition.isSuspended());
                actProcessDefinition.setDeployTime(new Date());

                // 保存到数据库
                saveProcessDefinition(actProcessDefinition);
            }
            
            log.info("流程部署成功 - 部署ID: {}, 流程定义ID: {}", 
                    deployment.getId(), definition != null ? definition.getId() : "未知");
            
            return deployment.getId();
        } catch (Exception e) {
            log.error("部署流程定义失败", e);
            throw new RuntimeException("部署流程定义失败: " + e.getMessage(), e);
        }
    }

    /**
     * 获取流程图资源名称
     */
    private String getProcessDiagramResourceName(ProcessDefinition processDefinition) {
        String resourceName = processDefinition.getResourceName();
        
        // 尝试查找对应的图形资源
        if (resourceName != null && resourceName.endsWith(".bpmn20.xml")) {
            String baseName = resourceName.substring(0, resourceName.length() - 11);
            List<String> resourceNames = repositoryService.getDeploymentResourceNames(
                    processDefinition.getDeploymentId());
            
            for (String name : resourceNames) {
                if (name.startsWith(baseName) && (name.endsWith(".png") || name.endsWith(".svg"))) {
                    return name;
                }
            }
        }
        
        return null;
    }

    @Override
    public boolean saveProcessDefinition(ActProcessDefinition processDefinition) {
//        根据key查询是否重复数据
        ActProcessDefinition existing = processDefinitionMapper.selectOne(new QueryWrapper<ActProcessDefinition>()
                .eq("process_key", processDefinition.getProcessKey()));
        
        if (existing != null) {
            EntityUtils.setCreateAndUpdateInfo(processDefinition,false);
//            根据key更新数据
            int update = processDefinitionMapper.update(processDefinition,
                    new QueryWrapper<ActProcessDefinition>()
                        .eq("process_key", processDefinition.getProcessKey()));
            return update > 0?true:false;
        } else {
            EntityUtils.setCreateAndUpdateInfo(processDefinition,true);
            return processDefinitionMapper.insert(processDefinition) > 0;
        }
    }

    @Override
    public ActProcessDefinition getByDeploymentId(String deploymentId) {
        LambdaQueryWrapper<ActProcessDefinition> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ActProcessDefinition::getDeploymentId, deploymentId);
        return processDefinitionMapper.selectOne(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void syncProcessConfig(String processKey) {
        try {
            // 获取最新版本的流程定义
            ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey(processKey)
                .latestVersion()
                .singleResult();
            
            if (processDefinition == null) {
                throw new RuntimeException("未找到流程定义: " + processKey);
            }
            
            // 获取流程定义的XML内容
            InputStream inputStream = repositoryService.getResourceAsStream(
                processDefinition.getDeploymentId(), 
                processDefinition.getResourceName()
            );
            
            // 使用ByteArrayResource替代InputStreamSource
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            IOUtils.copy(inputStream, baos);
            byte[] bytes = baos.toByteArray();
            
            // 使用 InputStreamProvider 替代 InputStreamSource
            BpmnModel bpmnModel = new BpmnXMLConverter().convertToBpmnModel(
                () -> new ByteArrayInputStream(bytes),  // 使用 lambda 表达式创建 InputStreamProvider
                true, 
                false
            );
            
            // 提取用户任务的表单属性
            Process process = bpmnModel.getMainProcess();
            Collection<FlowElement> flowElements = process.getFlowElements();
            
            for (FlowElement element : flowElements) {
                if (element instanceof UserTask) {
                    UserTask userTask = (UserTask) element;
                    List<FormProperty> formProperties = userTask.getFormProperties();
                    
                    // 处理表单属性
                    if (formProperties != null && !formProperties.isEmpty()) {
                        for (FormProperty formProperty : formProperties) {
                            // 创建或更新参数配置
                            SysProcessVariableConfig variableConfig = new SysProcessVariableConfig();
                            variableConfig.setProcessKey(processKey);
                            variableConfig.setName(formProperty.getId());
                            variableConfig.setLabel(formProperty.getName());
                            variableConfig.setVariableType(formProperty.getType());
                            
                            // 从表单属性中获取required属性
                            Map<String, List<ExtensionAttribute>> attributes = formProperty.getAttributes();
                            boolean required = false;
                            if (attributes != null && attributes.containsKey("required")) {
                                List<ExtensionAttribute> requiredAttrs = attributes.get("required");
                                if (requiredAttrs != null && !requiredAttrs.isEmpty()) {
                                    required = Boolean.parseBoolean(requiredAttrs.get(0).getValue());
                                }
                            }
                            variableConfig.setRequired(required);
                            
                            // 保存配置
                            saveOrUpdateVariableConfig(variableConfig);
                        }
                    }
                }
            }
            
        } catch (Exception e) {
            log.error("同步流程配置失败", e);
            throw new RuntimeException("同步流程配置失败: " + e.getMessage());
        }
    }

    /**
     * 保存或更新参数配置
     */
    private void saveOrUpdateVariableConfig(SysProcessVariableConfig config) {
        // 查询是否存在
        SysProcessVariableConfig existConfig = variableConfigMapper.selectOne(
            new LambdaQueryWrapper<SysProcessVariableConfig>()
                .eq(SysProcessVariableConfig::getProcessKey, config.getProcessKey())
                .eq(SysProcessVariableConfig::getName, config.getName())
        );
        
        if (existConfig != null) {
            // 更新
            config.setId(existConfig.getId());
            variableConfigMapper.updateById(config);
        } else {
            // 新增
            config.setStatus(0);
            config.setCreateTime(new Date());
            variableConfigMapper.insert(config);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteProcessDefinition(String deploymentId) {
        // 级联删除流程定义，即使有正在运行的流程实例
        repositoryService.deleteDeployment(deploymentId, true);
        
        // 删除数据库记录
        processDefinitionMapper.delete(new LambdaQueryWrapper<ActProcessDefinition>()
            .eq(ActProcessDefinition::getDeploymentId, deploymentId));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateProcessDefinitionState(String processDefinitionId, boolean suspended) {
        if (suspended) {
            repositoryService.suspendProcessDefinitionById(processDefinitionId, true, null);
        } else {
            repositoryService.activateProcessDefinitionById(processDefinitionId, true, null);
        }
        
        // 更新数据库状态
        ActProcessDefinition processDefinition = new ActProcessDefinition();
        processDefinition.setId(processDefinitionId);
        processDefinition.setSuspended(suspended);
        processDefinitionMapper.updateById(processDefinition);
    }

    @Override
    public byte[] getProcessDefinitionImage(String processDefinitionId) {
        try {
            log.info("开始获取流程图 - 流程定义ID: {}", processDefinitionId);
            
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionId(processDefinitionId)
                .singleResult();
            
            if (processDefinition == null) {
                log.error("流程定义不存在: {}", processDefinitionId);
                return null;
            }
            
            log.info("找到流程定义 - deploymentId: {}, diagramResourceName: {}", 
                processDefinition.getDeploymentId(), 
                processDefinition.getDiagramResourceName());
                
            if (processDefinition.getDiagramResourceName() == null) {
                log.warn("流程定义没有关联的图片资源");
                return null;
            }
            
        try (InputStream inputStream = repositoryService.getResourceAsStream(
                processDefinition.getDeploymentId(),
                processDefinition.getDiagramResourceName())) {
                    
                if (inputStream == null) {
                    log.error("无法获取流程图资源流");
                    return null;
                }
                
                byte[] bytes = inputStream.readAllBytes();
                log.info("成功读取流程图，大小: {} bytes", bytes.length);
                return bytes;
            }
        } catch (Exception e) {
            log.error("获取流程图失败", e);
            throw new RuntimeException("获取流程图失败: " + e.getMessage());
        }
    }

    @Override
    public String getProcessDefinitionXML(String processDefinitionId) {
        try {
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionId(processDefinitionId)
                .singleResult();
            
            if (processDefinition != null && processDefinition.getResourceName() != null) {
                try (var inputStream = repositoryService.getResourceAsStream(
                processDefinition.getDeploymentId(),
                processDefinition.getResourceName())) {
                    return new String(inputStream.readAllBytes());
                }
            }
            return null;
        } catch (IOException e) {
            log.error("获取流程XML失败", e);
            throw new RuntimeException("获取流程XML失败: " + e.getMessage());
        }
    }

    @Override
    public ActProcessDefinition selectProcessDefinitionById(String processDefinitionId) {
        return processDefinitionMapper.selectById(processDefinitionId);
    }

    @Override
    public List<ActProcessDefinition> getProcessDefinitionsByReviewer(String reviewerId) {
        return processDefinitionMapper.selectList(new LambdaQueryWrapper<ActProcessDefinition>()
            .eq(ActProcessDefinition::getSuspended, false));
    }

    @Override
    public List<Map<String, Object>> getApprovalLeaders() {
        try {
            // 获取当前用户ID
            Long currentUserId = SecurityUtils.getUserId();
            
            // 查询具有资产审批权限的用户，排除当前用户
            List<Map<String, Object>> leaders = userMapper.selectUsersByRoleKey("asset_approval", currentUserId);
            
            if (leaders == null || leaders.isEmpty()) {
                log.warn("未找到审批领导");
                return new ArrayList<>();
            }
            
            return leaders;
        } catch (Exception e) {
            log.error("获取审批领导列表失败", e);
            throw new RuntimeException("获取审批领导列表失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ProcessInstance startProcess(String processDefinitionKey, Map<String, Object> variables) {
        try {
            // 获取最新版本的流程定义
            ActProcessDefinition processDefinition = getLatestProcessDefinition(processDefinitionKey);
            if (processDefinition == null) {
                throw new RuntimeException("未找到流程定义或流程已被挂起");
            }

            // 获取流程配置
            SysProcessConfig processConfig = processConfigMapper.selectOne(
                new LambdaQueryWrapper<SysProcessConfig>()
                    .eq(SysProcessConfig::getProcessKey, processDefinitionKey)
                    .eq(SysProcessConfig::getStatus, "0")
            );
            
            if (processConfig == null) {
                throw new RuntimeException("未找到流程配置信息");
            }

            // 设置流程发起人
            String currentUsername = SecurityUtils.getUsername();
            Long currentUserId = SecurityUtils.getUserId();
            variables.put("initiator", currentUsername);
            variables.put("initiatorId", currentUserId);

            // 获取审批人列表
            List<Map<String, Object>> approvers = userMapper.selectUsersByRoleKey(
                processConfig.getRoleKey(), 
                currentUserId
            );
            
            if (approvers.isEmpty()) {
                throw new RuntimeException("未找到有效的审批人");
            }

            // 设置候选审批人
            List<String> approverIds = approvers.stream()
                .map(approver -> approver.get("id").toString())
                .collect(Collectors.toList());
            variables.put("approverList", approverIds);

            // 验证流程变量
            validateProcessVariables(processDefinitionKey, variables);

            // 启动流程实例
            Authentication.setAuthenticatedUserId(currentUsername);
            try {
                ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(
                    processDefinitionKey,
                    variables
                );

                // 设置第一个任务的候选人
                Task task = taskService.createTaskQuery()
                    .processInstanceId(processInstance.getId())
                    .singleResult();
                
                if (task != null) {
                    // 设置任务的候选人组（逐个添加候选人）
                    for (String userId : approverIds) {
                        taskService.addCandidateUser(task.getId(), userId);
                    }
                }

                // 自动完成用户申请节点
                completeAutoTask(processInstance.getId(), variables);

                return processInstance;
            } finally {
                Authentication.setAuthenticatedUserId(null);
            }
        } catch (Exception e) {
            log.error("启动流程失败", e);
            throw new RuntimeException("启动流程失败: " + e.getMessage());
        }
    }

    /**
     * 自动完成指定类型的任务
     */
    private void completeAutoTask(String processInstanceId, Map<String, Object> variables) {
        List<Task> tasks = taskService.createTaskQuery()
            .processInstanceId(processInstanceId)
            .list();

        for (Task task : tasks) {
            if (AUTO_COMPLETE_TASK_KEY.equals(task.getTaskDefinitionKey())) {
                // 自动完成任务
                taskService.complete(task.getId(), variables);
                log.info("自动完成用户申请节点任务: {}", task.getId());
            }
        }
    }

    /**
     * 验证流程变量
     */
    private void validateProcessVariables(String processKey, Map<String, Object> variables) {
        // 获取流程变量配置
        List<SysProcessVariableConfig> configs = variableConfigMapper.selectList(
            new LambdaQueryWrapper<SysProcessVariableConfig>()
                .eq(SysProcessVariableConfig::getProcessKey, processKey)
                .eq(SysProcessVariableConfig::getStatus, "0")
                .orderByAsc(SysProcessVariableConfig::getSortOrder)
        );

        // 验证必填项
        for (SysProcessVariableConfig config : configs) {
            if (config.getRequired()) {
                Object value = variables.get(config.getName());
                if (value == null || String.valueOf(value).trim().isEmpty()) {
                    throw new RuntimeException(String.format("参数[%s]不能为空", config.getLabel()));
                }
            }
        }
    }

    @Override
    public void completeTask(String taskId, Map<String, Object> variables) {
        try {
            // 获取任务信息
            Task task = taskService.createTaskQuery()
                .taskId(taskId)
                .singleResult();
            
            if (task == null) {
                throw new FlowableException("任务不存在或已完成");
            }

            // 完成任务
            if (variables != null && !variables.isEmpty()) {
                taskService.complete(taskId, variables);
            } else {
                taskService.complete(taskId);
            }
        } catch (Exception e) {
            log.error("完成任务失败", e);
            throw new RuntimeException("完成任务失败: " + e.getMessage());
        }
    }

    @Override
    public void claimTask(String taskId, String userId) {
        try {
            taskService.claim(taskId, userId);
        } catch (Exception e) {
            log.error("认领任务失败", e);
            throw new RuntimeException("认领任务失败: " + e.getMessage());
        }
    }

    @Override
    public void unclaimTask(String taskId) {
        try {
            taskService.unclaim(taskId);
        } catch (Exception e) {
            log.error("取消认领任务失败", e);
            throw new RuntimeException("取消认领任务失败: " + e.getMessage());
        }
    }

    @Override
    public List<Task> getTasksByProcessInstanceId(String processInstanceId) {
        try {
            return taskService.createTaskQuery()
                .processInstanceId(processInstanceId)
                .list();
        } catch (Exception e) {
            log.error("获取任务列表失败", e);
            throw new RuntimeException("获取任务列表失败: " + e.getMessage());
        }
    }

    @Override
    public List<Task> getTasksByUserId(String userId) {
        try {
            return taskService.createTaskQuery()
                .taskCandidateOrAssigned(userId)
            .list();
        } catch (Exception e) {
            log.error("获取用户任务列表失败", e);
            throw new RuntimeException("获取用户任务列表失败: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> getProcessVariables(String processInstanceId) {
        try {
            return runtimeService.getVariables(processInstanceId);
        } catch (Exception e) {
            log.error("获取流程变量失败", e);
            throw new RuntimeException("获取流程变量失败: " + e.getMessage());
        }
    }

    @Override
    public void setProcessReviewers(String processDefinitionId, List<Long> reviewerIds) {
        try {
            // 删除原有审批人
            processReviewerMapper.delete(new LambdaQueryWrapper<ProcessReviewer>()
                .eq(ProcessReviewer::getProcessDefinitionId, processDefinitionId));
            
            // 添加新的审批人
            if (reviewerIds != null && !reviewerIds.isEmpty()) {
                for (Long reviewerId : reviewerIds) {
                    ProcessReviewer reviewer = new ProcessReviewer();
                    reviewer.setProcessDefinitionId(processDefinitionId);
                    reviewer.setReviewerId(reviewerId);
                    reviewer.setCreateTime(new Date());
                    processReviewerMapper.insert(reviewer);
                }
            }
        } catch (Exception e) {
            log.error("设置流程审批人失败", e);
            throw new RuntimeException("设置流程审批人失败: " + e.getMessage());
        }
    }

    @Override
    public List<SysUser> getProcessReviewers(String processDefinitionId) {
        try {
            // 查询审批人ID列表
            List<Long> reviewerIds = processReviewerMapper.selectList(
                new LambdaQueryWrapper<ProcessReviewer>()
                    .eq(ProcessReviewer::getProcessDefinitionId, processDefinitionId)
            ).stream()
            .map(ProcessReviewer::getReviewerId)  // 这里返回的是 Long 类型
            .collect(Collectors.toList());
            
            if (reviewerIds.isEmpty()) {
                return new ArrayList<>();
            }
            
            // 查询审批人信息
            return userMapper.selectBatchIds(reviewerIds);  // selectBatchIds 需要 Collection<? extends Serializable>
        } catch (Exception e) {
            log.error("获取流程审批人失败", e);
            throw new RuntimeException("获取流程审批人失败: " + e.getMessage());
        }
    }

    @Override
    public ActProcessDefinition getLatestProcessDefinition(String processKey) {
        return processDefinitionMapper.selectOne(new LambdaQueryWrapper<ActProcessDefinition>()
            .eq(ActProcessDefinition::getProcessKey, processKey)
            .eq(ActProcessDefinition::getSuspended, false)
            .orderByDesc(ActProcessDefinition::getVersion)
            .last("LIMIT 1"));
    }

    @Override
    public List<Map<String, Object>> getApproversByRoleKey(String roleKey) {
        try {
            // 获取当前用户ID
            Long currentUserId = SecurityUtils.getUserId();  // 确保这里返回 Long 类型
            
            // 根据角色标识查询用户列表（排除当前用户）
            List<Map<String, Object>> approvers = userMapper.selectUsersByRoleKey(roleKey, currentUserId);
            
            if (approvers == null || approvers.isEmpty()) {
                log.warn("未找到角色[{}]对应的审批人", roleKey);
                return new ArrayList<>();
            }
            
            return approvers;
        } catch (Exception e) {
            log.error("获取审批人列表失败", e);
            throw new RuntimeException("获取审批人列表失败: " + e.getMessage());
        }
    }

    @Override
    public void insertProcessDefinition(ActProcessDefinition processDefinition) {
        processDefinitionMapper.insert(processDefinition);
    }

    /*public void autoCompleteFirstTask(String processInstanceId) {
        try {
            // 查询需要自动完成的任务
            List<Task> autoCompleteTasks = taskService.createTaskQuery()
                .processInstanceId(processInstanceId)
                .taskDefinitionKeyLike("autoComplete%") // 根据任务key前缀匹配
                .list();

            // 或者通过扩展属性查询
            List<Task> tasksWithProperty = taskService.getTaskEntityManager()
                .findTasksWithProcessVariableValueEquals("autoComplete", true);
            
            tasksWithProperty.forEach(task -> {
                Authentication.setAuthenticatedUserId("system");
                taskService.complete(task.getId());
            });
        } catch (Exception e) {
            log.error("自动完成任务失败", e);
            throw new FlowableException("自动完成初始任务失败: " + e.getMessage());
        }
    }*/
} 