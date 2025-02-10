package com.project.system2.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.system2.common.core.utils.SecurityUtils;
import com.project.system2.domain.entity.ActProcessInstance;
import com.project.system2.mapper.ActProcessInstanceMapper;
import com.project.system2.service.IActProcessInstanceService;
import lombok.extern.slf4j.Slf4j;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.ExclusiveGateway;
import org.flowable.bpmn.model.FlowElement;
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
    public void completeTask(String taskId, Map<String, Object> variables) {
        try {
            Task currentTask = taskService.createTaskQuery().taskId(taskId).singleResult();
            
            // 动态判断网关任务
            BpmnModel bpmnModel = repositoryService.getBpmnModel(currentTask.getProcessDefinitionId());
            FlowElement flowElement = bpmnModel.getFlowElement(currentTask.getTaskDefinitionKey());
            
            if (flowElement instanceof ExclusiveGateway) {
                if (variables.containsKey("leaderId")) {
                    log.warn("网关任务不需要leaderId参数，已自动过滤");
                    variables.remove("leaderId");
                }
            }
            
            if (currentTask == null) {
                throw new FlowableException("任务不存在，任务ID: " + taskId);
            }
            
            // 添加调试日志
            log.debug("完成任务前流程变量：{}", runtimeService.getVariables(currentTask.getProcessInstanceId()));
            
            taskService.complete(taskId, variables, true);

            
            // 检查流程实例是否仍然存在
            ProcessInstance instance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(currentTask.getProcessInstanceId())
                .singleResult();

            // 只有流程实例仍然运行时才更新后续任务
            if (instance != null && !instance.isEnded()) {
                List<Task> nextTasks = taskService.createTaskQuery()
                    .processInstanceId(currentTask.getProcessInstanceId())
                    .list();

                nextTasks.forEach(task -> {
                    ActProcessInstance processInstance = new ActProcessInstance();
                    processInstance.setId(task.getProcessInstanceId());
                    processInstance.setTaskId(task.getId());
                    processInstance.setTaskName(task.getName());
                    processInstance.setAssignee(task.getAssignee());
                    processInstanceMapper.updateById(processInstance);
                });
            }
        } catch (FlowableException e) {
            log.error("任务处理失败: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("任务处理发生意外错误", e);
            throw new FlowableException("任务处理失败: " + e.getMessage());
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
} 