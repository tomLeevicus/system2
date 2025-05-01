package com.project.system2.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.system2.common.core.utils.SecurityUtils;
import com.project.system2.domain.entity.ActProcessInstance;
import com.project.system2.domain.entity.ActTaskInfo;
import com.project.system2.domain.entity.SysUser;
import com.project.system2.domain.query.ProcessInstanceQuery;
import com.project.system2.domain.query.TaskQuery;
import com.project.system2.mapper.ActProcessInstanceMapper;
import com.project.system2.mapper.SysUserMapper;
import com.project.system2.service.IActProcessInstanceService;
import lombok.extern.slf4j.Slf4j;
import org.flowable.bpmn.model.*;
import org.flowable.common.engine.api.FlowableException;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.flowable.engine.RepositoryService;
import org.springframework.util.StringUtils;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.ArrayList;
@Slf4j
@Service
public class ActProcessInstanceServiceImpl implements IActProcessInstanceService {

    @Autowired
    private RuntimeService runtimeService;
    
    @Autowired
    private ActProcessInstanceMapper processInstanceMapper;

    @Autowired
    private TaskService taskService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String startProcess(String processDefinitionKey, Map<String, Object> variables) {
        try {
            ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(
                processDefinitionKey, variables);
            
            // 保存到数据库
            ActProcessInstance actProcessInstance = new ActProcessInstance();
            actProcessInstance.setId(processInstance.getId());
            actProcessInstance.setProcessDefinitionId(processInstance.getProcessDefinitionId());
            actProcessInstance.setProcessDefinitionKey(processInstance.getProcessDefinitionKey());
            actProcessInstance.setName(processInstance.getName());
            actProcessInstance.setBusinessKey(processInstance.getBusinessKey());
            actProcessInstance.setStartTime(processInstance.getStartTime());
            actProcessInstance.setSuspended(processInstance.isSuspended());
            
            processInstanceMapper.insert(actProcessInstance);
            
            return processInstance.getId();
        } catch (Exception e) {
            log.error("启动流程实例失败", e);
            throw new RuntimeException("启动流程实例失败: " + e.getMessage());
        }
    }

    @Override
    public Page<ActProcessInstance> listProcessInstances(ProcessInstanceQuery query) {
        // 构建分页对象
        Page<ActProcessInstance> page = new Page<>(query.getPageNum(), query.getPageSize());
        
        // 构建查询条件
        LambdaQueryWrapper<ActProcessInstance> wrapper = new LambdaQueryWrapper<ActProcessInstance>()
            // 流程定义相关
            .eq(StringUtils.hasText(query.getProcessDefinitionId()), 
                ActProcessInstance::getProcessDefinitionId, query.getProcessDefinitionId())
            .eq(StringUtils.hasText(query.getProcessDefinitionKey()), 
                ActProcessInstance::getProcessDefinitionKey, query.getProcessDefinitionKey())
            
            // 实例基本信息
            .like(StringUtils.hasText(query.getName()), 
                ActProcessInstance::getName, query.getName())
            .eq(StringUtils.hasText(query.getBusinessKey()), 
                ActProcessInstance::getBusinessKey, query.getBusinessKey())
            
            // 状态查询
            .eq(query.getSuspended() != null, 
                ActProcessInstance::getSuspended, query.getSuspended())
            .inSql(StringUtils.hasText(query.getStatus()),
                ActProcessInstance::getStatus, 
                "SELECT dict_value FROM sys_dict_data WHERE dict_type = 'flowable_instance_status' AND dict_label = '" + query.getStatus() + "'")
            
            // 发起人信息
            .eq(query.getStartUserId() != null, 
                ActProcessInstance::getStartUserId, query.getStartUserId())
            .like(StringUtils.hasText(query.getStartUserName()), 
                ActProcessInstance::getStartUserName, query.getStartUserName())
            
            // 时间范围查询
            .ge(query.getStartTimeBegin() != null, 
                ActProcessInstance::getStartTime, query.getStartTimeBegin())
            .le(query.getStartTimeEnd() != null, 
                ActProcessInstance::getStartTime, query.getStartTimeEnd())
            
            ; // Add semicolon to terminate the wrapper chain here

        // 处理排序
        if (StringUtils.hasText(query.getOrderBy())) {
            String[] orders = query.getOrderBy().split(",");
            for (String order : orders) {
                String[] parts = order.trim().split(" ");
                if (parts.length == 2) {
                    wrapper.orderBy(true, "asc".equalsIgnoreCase(parts[1]), 
                        getColumn(parts[0]));
                }
            }
        } else {
            wrapper.orderByDesc(ActProcessInstance::getStartTime);
        }
        
        return processInstanceMapper.selectPage(page, wrapper);
    }

    // 辅助方法：将字段名转换为Lambda表达式
    private SFunction<ActProcessInstance, ?> getColumn(String fieldName) {
        switch (fieldName) {
            case "start_time": return ActProcessInstance::getStartTime;
            case "end_time": return ActProcessInstance::getEndTime;
            case "process_definition_key": return ActProcessInstance::getProcessDefinitionKey;
            default: return ActProcessInstance::getStartTime;
        }
    }

    @Override
    public ActProcessInstance getProcessInstanceById(String processInstanceId) {
        return processInstanceMapper.selectById(processInstanceId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateProcessInstanceState(String processInstanceId, boolean suspended) {
        try {
            if (suspended) {
                runtimeService.suspendProcessInstanceById(processInstanceId);
            } else {
                runtimeService.activateProcessInstanceById(processInstanceId);
            }
            
            // 更新数据库状态
            ActProcessInstance processInstance = new ActProcessInstance();
            processInstance.setId(processInstanceId);
            processInstance.setSuspended(suspended);
            processInstanceMapper.updateById(processInstance);
            
            // 更新自定义表状态
            processInstance = new ActProcessInstance();
            processInstance.setId(processInstanceId);
            processInstance.setStatus(suspended ? "suspended" : "running");
            processInstanceMapper.updateById(processInstance);
        } catch (Exception e) {
            log.error("更新流程实例状态失败", e);
            throw new RuntimeException("更新流程实例状态失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteProcessInstance(String processInstanceId, String deleteReason) {
        try {
            // 删除运行时的流程实例
            runtimeService.deleteProcessInstance(processInstanceId, deleteReason);
            
            // 更新数据库记录
            ActProcessInstance processInstance = new ActProcessInstance();
            processInstance.setId(processInstanceId);
            processInstance.setDelFlag(0);
            processInstanceMapper.updateById(processInstance);
        } catch (Exception e) {
            log.error("删除流程实例失败", e);
            throw new RuntimeException("删除流程实例失败: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> getProcessInstanceVariables(String processInstanceId) {
        try {
            ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult();
            
            if (processInstance != null) {
                return runtimeService.getVariables(processInstanceId);
            }
            return new HashMap<>();
        } catch (Exception e) {
            log.error("获取流程实例变量失败", e);
            throw new RuntimeException("获取流程实例变量失败: " + e.getMessage());
        }
    }

    @Override
    public Page<ActTaskInfo> getTodoInstances(Page<ActTaskInfo> page, String userId) {
        return page.setRecords(
            taskService.createTaskQuery()
                .taskAssignee(userId)
                .orderByTaskCreateTime().desc()
                .list()
                .stream()
                .map(task -> {
                    ActTaskInfo taskInfo = new ActTaskInfo();
                    // 基本任务信息
                    taskInfo.setId(task.getId());
                    taskInfo.setName(task.getName());
                    taskInfo.setDescription(task.getDescription());
                    taskInfo.setPriority(task.getPriority());
                    taskInfo.setOwner(task.getOwner());
                    taskInfo.setAssignee(task.getAssignee());
                    taskInfo.setProcessInstanceId(task.getProcessInstanceId());
                    taskInfo.setExecutionId(task.getExecutionId());
                    taskInfo.setTaskDefinitionKey(task.getTaskDefinitionKey());
                    taskInfo.setCreateTime(task.getCreateTime());
                    taskInfo.setDueDate(task.getDueDate());
                    taskInfo.setClaimTime(task.getClaimTime());
                    taskInfo.setCategory(task.getCategory());
                    taskInfo.setTenantId(task.getTenantId());
                    taskInfo.setFormKey(task.getFormKey());
                    taskInfo.setProcessDefinitionId(task.getProcessDefinitionId());
                    taskInfo.setSuspended(task.isSuspended());
                    
                    // 附加信息
                    taskInfo.setStatus(task.isSuspended() ? "suspended" : "pending");
                    
                    // 从流程实例获取更多信息
                    ActProcessInstance processInstance = processInstanceMapper.selectById(task.getProcessInstanceId());
                    if (processInstance != null) {
                        taskInfo.setBusinessKey(processInstance.getBusinessKey());
                        taskInfo.setProcessInstanceName(processInstance.getName());
                        
                        // 查询用户名
                        if (taskInfo.getAssignee() != null) {
                            SysUser assigneeUser = sysUserMapper.selectById(taskInfo.getAssignee());
                            if (assigneeUser != null) {
                                taskInfo.setAssigneeName(assigneeUser.getNickname());
                            }
                        }
                    }
                    
                    return taskInfo;
                })
                .collect(Collectors.toList())
        );
    }

    @Override
    public void completeUserTask(String taskId, Map<String, Object> variables) {
        Task currentTask = validateTask(taskId);
        validateTaskType(currentTask, UserTask.class);
        
        List<FlowElement> nextElements = getNextFlowElements(currentTask);
        boolean hasUserTask = nextElements.stream().anyMatch(e -> e instanceof UserTask);
        
        // 增强校验：当后续有用户任务时，必须存在leaderId
        if (hasUserTask) {
            if (!variables.containsKey("leaderId") 
                && !runtimeService.hasVariable(currentTask.getProcessInstanceId(), "leaderId")) {
                throw new FlowableException("后续存在用户任务，必须指定leaderId");
            }
        }
        
        taskService.complete(taskId, variables);
        log.info("用户任务完成 - 任务ID: {}, 流程实例ID: {}", taskId, currentTask.getProcessInstanceId());
        handleNextTasks(currentTask);
    }

    @Override
    public void completeGatewayTask(String taskId, Map<String, Object> variables) {
        Task currentTask = validateTask(taskId);
        validateTaskType(currentTask, ExclusiveGateway.class);
        
        // 获取后续节点信息
        List<FlowElement> nextElements = getNextFlowElements(currentTask);
        
        // 判断后续节点类型
        boolean hasUserTask = nextElements.stream().anyMatch(e -> e instanceof UserTask);
        boolean hasGateway = nextElements.stream().anyMatch(e -> e instanceof ExclusiveGateway);
        
        // 动态参数处理
        if (hasUserTask) {
            if (!variables.containsKey("leaderId") 
                && !runtimeService.hasVariable(currentTask.getProcessInstanceId(), "leaderId")) {
                throw new FlowableException("后续存在用户任务，必须指定leaderId");
            }
        }
        
        if (hasGateway) {
            log.warn("连续网关任务检测 - 任务ID: {}", taskId);
            variables.put("multiGateway", true);
        }
        
        taskService.complete(taskId, variables);
        log.info("网关任务完成 - 任务ID: {}, 流程实例ID: {}", taskId, currentTask.getProcessInstanceId());
        handleNextTasks(currentTask);
    }

    private void handleNextTasks(Task currentTask) {
        String processInstanceId = currentTask.getProcessInstanceId();
        
        // 检查流程实例是否仍然存在
        ProcessInstance instance = runtimeService.createProcessInstanceQuery()
            .processInstanceId(processInstanceId)
            .singleResult();

        if (instance != null && !instance.isEnded()) {
            List<Task> nextTasks = taskService.createTaskQuery()
                .processInstanceId(processInstanceId)
                .list();

            nextTasks.forEach(task -> {
                // 更新任务信息到业务表
                ActProcessInstance processInstance = new ActProcessInstance();
                processInstance.setId(task.getProcessInstanceId());
                processInstance.setTaskId(task.getId());
                processInstance.setTaskName(task.getName());
                processInstance.setAssignee(task.getAssignee());
                processInstance.setTaskEndTime(task.getCreateTime());
//                字段调整
                processInstance.setTaskStatus(task.isSuspended() ? "suspended" : "running");
                
                // 同步业务数据
                ActProcessInstance customInfo = processInstanceMapper.selectById(task.getProcessInstanceId());
                if(customInfo != null){
                    processInstance.setBusinessKey(customInfo.getBusinessKey());
                    processInstance.setStartTime(customInfo.getStartTime());
                    processInstance.setStatus(customInfo.getStatus());
                }
                
                processInstanceMapper.updateById(processInstance);
                log.info("更新后续任务信息 - 任务ID: {}, 处理人: {}", task.getId(), task.getAssignee());
            });
        } else {
            log.info("流程实例已结束，无需更新后续任务");
            // 更新流程实例状态为已完成
            ActProcessInstance endedInstance = new ActProcessInstance();
            endedInstance.setId(processInstanceId);
//            字段调整
            endedInstance.setStatus("completed");
            endedInstance.setEndTime(new Date());
            processInstanceMapper.updateById(endedInstance);
        }
    }

    private Task validateTask(String taskId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (task == null) {
            throw new FlowableException("任务不存在，任务ID: " + taskId);
        }
        log.debug("任务验证通过 - 任务ID: {}, 名称: {}", taskId, task.getName());
        return task;
    }

    private void validateTaskType(Task task, Class<? extends FlowElement> expectedType) {
        BpmnModel bpmnModel = repositoryService.getBpmnModel(task.getProcessDefinitionId());
        FlowElement flowElement = bpmnModel.getFlowElement(task.getTaskDefinitionKey());
        
        if (!expectedType.isInstance(flowElement)) {
            throw new FlowableException("任务类型不匹配，预期类型: " + expectedType.getSimpleName());
        }
    }

    @Override
    public void syncInstance(ActProcessInstance instance) {
        instance.setCreateBy(SecurityUtils.getUserId());
        instance.setCreateTime(new Date());
        
        ActProcessInstance exist = processInstanceMapper.selectById(instance.getId());
        if (exist == null) {
            processInstanceMapper.insert(instance);
        } else {
            instance.setUpdateBy(SecurityUtils.getUserId());
            instance.setUpdateTime(new Date());
            processInstanceMapper.updateById(instance);
        }
    }

    @Override
    public void updateStatus(String processInstanceId, String status) {
        ActProcessInstance instance = new ActProcessInstance();
        instance.setId(processInstanceId);
        instance.setStatus(status);
        instance.setUpdateTime(new Date());
        processInstanceMapper.updateById(instance);
    }

    /**
     * @deprecated 请使用 {@link #completeUserTask(String, Map)} 或 {@link #completeGatewayTask(String, Map)} 替代
     */
    @Deprecated
    @Override
    public void completeTask(String taskId, Map<String, Object> variables) {
        try {
            log.warn("使用已弃用的completeTask方法，建议迁移到completeUserTask/completeGatewayTask");
            
            Task task = validateTask(taskId);
            List<FlowElement> nextElements = getNextFlowElements(task);
            
            // 动态判断是否需要leaderId
            boolean needLeaderId = nextElements.stream()
                .anyMatch(e -> e instanceof UserTask);
            
            if (needLeaderId && !variables.containsKey("leaderId")) {
                throw new FlowableException("必须指定leaderId参数");
            }
            if (!needLeaderId && variables.containsKey("leaderId")) {
                log.warn("当前任务无需leaderId参数，已自动移除");
                variables.remove("leaderId");
            }
            
            BpmnModel bpmnModel = repositoryService.getBpmnModel(task.getProcessDefinitionId());
            FlowElement flowElement = bpmnModel.getFlowElement(task.getTaskDefinitionKey());
            
            if (flowElement instanceof UserTask) {
                completeUserTask(taskId, variables);
            } else if (flowElement instanceof ExclusiveGateway) {
                completeGatewayTask(taskId, variables);
            } else {
                throw new FlowableException("不支持的任务类型: " + flowElement.getClass().getSimpleName());
            }
        } catch (Exception e) {
            throw new FlowableException("任务处理失败: " + e.getMessage(), e);
        }
    }

    private List<FlowElement> getNextFlowElements(Task task) {
        BpmnModel bpmnModel = repositoryService.getBpmnModel(task.getProcessDefinitionId());
        FlowElement currentElement = bpmnModel.getFlowElement(task.getTaskDefinitionKey());
        
        List<FlowElement> nextElements = new ArrayList<>();
        // 实现获取后续节点的逻辑（需要根据实际流程结构遍历）
        // 示例：获取当前任务的所有出口连线
        if (currentElement instanceof Activity) {
            Activity activity = (Activity) currentElement;
            for (SequenceFlow flow : activity.getOutgoingFlows()) {
                nextElements.add(flow.getTargetFlowElement());
            }
        }
        return nextElements;
    }

    @Override
    public boolean hasNextGateway(String taskId) {
        // 查询当前任务
        Task task = taskService.createTaskQuery()
                .taskId(taskId)
                .singleResult();

        if (task == null) {
            throw new RuntimeException("任务不存在: " + taskId);
        }

        // 获取当前任务的流程定义
         ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionId(task.getProcessDefinitionId())
                .singleResult();

        if (processDefinition == null) {
            throw new RuntimeException("流程定义不存在: " + task.getProcessDefinitionId());
        }

        // 获取当前任务的流程模型
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinition.getId());

        // 获取当前任务的流程节点
        FlowElement currentElement = bpmnModel.getFlowElement(task.getTaskDefinitionKey());

        if (currentElement == null) {
            throw new RuntimeException("流程节点不存在: " + task.getTaskDefinitionKey());
        }

        // 检查当前节点是否有下一个网关
        if (currentElement instanceof UserTask) {
            UserTask userTask = (UserTask) currentElement;
            List<SequenceFlow> outgoingFlows = userTask.getOutgoingFlows();

            for (SequenceFlow flow : outgoingFlows) {
                FlowElement targetElement = bpmnModel.getFlowElement(flow.getTargetRef());
                if (targetElement instanceof Gateway) {
                    return true; // 存在下一个网关
                }
            }
        }

        return false; // 不存在下一个网关
    }

    @Override
    public List<FlowElement> getNextFlowElements(String taskId) {
        try {
            Task task = taskService.createTaskQuery()
                .taskId(taskId)
                .singleResult();
            
            if (task == null) {
                throw new FlowableException("任务不存在: " + taskId);
            }
            return getNextFlowElements(task);
        } catch (FlowableException e) {
            log.error("获取后续节点失败: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public Page<ActProcessInstance> listUserTasks(TaskQuery query) {
        // 1. 构建 ProcessInstanceQuery 对象，用于传递给底层的 listProcessInstances 方法
        ProcessInstanceQuery processQuery = new ProcessInstanceQuery();

        // 2. 设置分页参数
        processQuery.setPageNum(query.getPageNum());
        processQuery.setPageSize(query.getPageSize());

        // 3. 设置必须的查询条件 - 发起人ID
        processQuery.setStartUserId(SecurityUtils.getUserId());

        // 4. 条件性地从 TaskQuery 复制查询参数到 ProcessInstanceQuery
        if (org.springframework.util.StringUtils.hasText(query.getProcessName())) {
            processQuery.setName(query.getProcessName());
        }
        if (org.springframework.util.StringUtils.hasText(query.getStatus())) {
            processQuery.setStatus(query.getStatus());
        }
        if (query.getStartTimeBegin() != null) {
            processQuery.setStartTimeBegin(query.getStartTimeBegin());
        }
        if (query.getStartTimeEnd() != null) {
            processQuery.setStartTimeEnd(query.getStartTimeEnd());
        }
        if (org.springframework.util.StringUtils.hasText(query.getOrderByColumn())) {
            // Set the orderBy string directly from TaskQuery's orderByColumn
            // The listProcessInstances method's internal logic handles parsing this.
            processQuery.setOrderBy(query.getOrderByColumn());
        }
        // 注意: TaskQuery 中的其他字段如果 ProcessInstanceQuery 支持，也应在此处映射

        // 5. 调用现有的 listProcessInstances 方法执行查询
        // 这个方法内部已经处理了 Mybatis Plus 的 Wrapper 构建和分页查询
        return this.listProcessInstances(processQuery);
    }
} 