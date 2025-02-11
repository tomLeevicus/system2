package com.project.system2.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.system2.common.core.utils.SecurityUtils;
import com.project.system2.domain.entity.ActProcessInstance;
import com.project.system2.mapper.ActProcessInstanceMapper;
import com.project.system2.service.IActProcessInstanceService;
import lombok.extern.slf4j.Slf4j;
import org.flowable.bpmn.model.*;
import org.flowable.common.engine.api.FlowableException;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.flowable.engine.RepositoryService;

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
    public Page<ActProcessInstance> listProcessInstances(Page<ActProcessInstance> page, ActProcessInstance processInstance) {
        return processInstanceMapper.selectPage(page, new LambdaQueryWrapper<ActProcessInstance>()
            .eq(processInstance.getProcessDefinitionId() != null,
                ActProcessInstance::getProcessDefinitionId, processInstance.getProcessDefinitionId())
            .like(processInstance.getName() != null,
                ActProcessInstance::getName, processInstance.getName())
            .eq(processInstance.getSuspended() != null,
                ActProcessInstance::getSuspended, processInstance.getSuspended())
            .orderByDesc(ActProcessInstance::getStartTime));
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
    public Page<ActProcessInstance> getTodoInstances(Page<ActProcessInstance> page, String userId) {
        return page.setRecords(
            taskService.createTaskQuery()
                .taskAssignee(userId)
                .orderByTaskCreateTime().desc()
                .list()
                .stream()
                .map(task -> {
                    ActProcessInstance instance = new ActProcessInstance();
                    instance.setId(task.getProcessInstanceId());
                    instance.setTaskId(task.getId());
                    instance.setTaskName(task.getName());
                    instance.setAssignee(task.getAssignee());
                    instance.setTaskEndTime(task.getCreateTime());
                    instance.setTaskStatus(task.isSuspended() ? "suspended" : "active");

                    ActProcessInstance customInfo = processInstanceMapper.selectById(task.getProcessInstanceId());
                    if(customInfo != null){
                        instance.setBusinessKey(customInfo.getBusinessKey());
                        instance.setStartTime(customInfo.getStartTime());
                        instance.setStatus(customInfo.getStatus());
                    }
                    return instance;
                })
                .sorted((a, b) -> b.getTaskEndTime().compareTo(a.getTaskEndTime()))
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
                processInstance.setTaskStatus(task.isSuspended() ? "suspended" : "active");
                
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
            endedInstance.setStatus("ended");
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
        Task task = validateTask(taskId);
        List<FlowElement> nextElements = getNextFlowElements(task);
        return nextElements.stream()
            .anyMatch(e -> e instanceof ExclusiveGateway);
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
} 