package com.project.system2.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.system2.domain.entity.ActTask;
import com.project.system2.mapper.ActTaskMapper;
import com.project.system2.service.IActTaskService;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.HistoryService;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ActTaskServiceImpl implements IActTaskService {

    @Autowired
    private TaskService taskService;
    
    @Autowired
    private ActTaskMapper taskMapper;

    @Autowired
    private HistoryService historyService;

    @Override
    public Page<ActTask> getTodoTasks(Page<ActTask> page, String userId) {
        try {
            List<Task> tasks = taskService.createTaskQuery()
                .taskAssignee(userId)
                .active()
                .orderByTaskCreateTime()
                .desc()
                .listPage((int)page.getCurrent(), (int)page.getSize());
            
            long total = taskService.createTaskQuery()
                .taskAssignee(userId)
                .active()
                .count();
                
            List<ActTask> records = tasks.stream()
                .map(this::convertToActTask)
                .collect(Collectors.toList());
                
            page.setRecords(records);
            page.setTotal(total);
            
            return page;
        } catch (Exception e) {
            log.error("获取待办任务列表失败", e);
            throw new RuntimeException("获取待办任务列表失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void completeTask(String taskId, Map<String, Object> variables) {
        try {
            taskService.complete(taskId, variables);
        } catch (Exception e) {
            log.error("完成任务失败", e);
            throw new RuntimeException("完成任务失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void claimTask(String taskId, String userId) {
        try {
            taskService.claim(taskId, userId);
        } catch (Exception e) {
            log.error("签收任务失败", e);
            throw new RuntimeException("签收任务失败: " + e.getMessage());
        }
    }

    @Override
    public Page<ActTask> getDoneTasks(Page<ActTask> page, String userId) {
        try {
            List<HistoricTaskInstance> historicTasks = historyService.createHistoricTaskInstanceQuery()
                .taskAssignee(userId)
                .finished()
                .orderByHistoricTaskInstanceEndTime()
                .desc()
                .listPage((int)page.getCurrent(), (int)page.getSize());
            
            long total = historyService.createHistoricTaskInstanceQuery()
                .taskAssignee(userId)
                .finished()
                .count();
                
            List<ActTask> records = historicTasks.stream()
                .map(this::convertToActTaskFromHistory)
                .collect(Collectors.toList());
                
            page.setRecords(records);
            page.setTotal(total);
            
            return page;
        } catch (Exception e) {
            log.error("获取已办任务列表失败", e);
            throw new RuntimeException("获取已办任务列表失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void unclaimTask(String taskId) {
        try {
            taskService.unclaim(taskId);
        } catch (Exception e) {
            log.error("取消签收任务失败", e);
            throw new RuntimeException("取消签收任务失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delegateTask(String taskId, String userId) {
        try {
            taskService.delegateTask(taskId, userId);
        } catch (Exception e) {
            log.error("委派任务失败", e);
            throw new RuntimeException("委派任务失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void transferTask(String taskId, String userId) {
        try {
            Task task = taskService.createTaskQuery()
                .taskId(taskId)
                .singleResult();
            if (task != null) {
                taskService.setAssignee(taskId, userId);
            }
        } catch (Exception e) {
            log.error("转办任务失败", e);
            throw new RuntimeException("转办任务失败: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> getTaskFormData(String taskId) {
        try {
            Task task = taskService.createTaskQuery()
                .taskId(taskId)
                .singleResult();
            if (task != null) {
                // 获取任务的表单变量
                Map<String, Object> formData = new HashMap<>();
                
                // 获取任务的所有变量
                Map<String, Object> taskVariables = taskService.getVariables(taskId);
                formData.putAll(taskVariables);
                
                // 获取任务的表单属性（如果有）
                formData.put("formKey", task.getFormKey());
                formData.put("taskId", task.getId());
                formData.put("taskName", task.getName());
                formData.put("processInstanceId", task.getProcessInstanceId());
                formData.put("processDefinitionId", task.getProcessDefinitionId());
                
                return formData;
            }
            return new HashMap<>();
        } catch (Exception e) {
            log.error("获取任务表单数据失败", e);
            throw new RuntimeException("获取任务表单数据失败: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> getTaskVariables(String taskId) {
        try {
            return taskService.getVariables(taskId);
        } catch (Exception e) {
            log.error("获取任务变量失败", e);
            throw new RuntimeException("获取任务变量失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addTaskComment(String taskId, String message) {
        try {
            Task task = taskService.createTaskQuery()
                .taskId(taskId)
                .singleResult();
            if (task != null) {
                taskService.addComment(taskId, task.getProcessInstanceId(), message);
            }
        } catch (Exception e) {
            log.error("添加任务评论失败", e);
            throw new RuntimeException("添加任务评论失败: " + e.getMessage());
        }
    }

    @Override
    public List<String> getTaskComments(String taskId) {
        try {
            return taskService.getTaskComments(taskId).stream()
                .map(comment -> comment.getFullMessage())
                .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("获取任务评论失败", e);
            throw new RuntimeException("获取任务评论失败: " + e.getMessage());
        }
    }

    private ActTask convertToActTask(Task task) {
        ActTask actTask = new ActTask();
        actTask.setId(task.getId());
        actTask.setName(task.getName());
        actTask.setProcessInstanceId(task.getProcessInstanceId());
        actTask.setProcessDefinitionId(task.getProcessDefinitionId());
        actTask.setTaskDefinitionKey(task.getTaskDefinitionKey());
        actTask.setDescription(task.getDescription());
        actTask.setAssignee(task.getAssignee());
        actTask.setOwner(task.getOwner());
        actTask.setDueDate(task.getDueDate());
        actTask.setPriority(task.getPriority());
        actTask.setCategory(task.getCategory());
        actTask.setFormKey(task.getFormKey());
        actTask.setSuspended(task.isSuspended());
        return actTask;
    }

    private ActTask convertToActTaskFromHistory(HistoricTaskInstance historicTask) {
        ActTask actTask = new ActTask();
        actTask.setId(historicTask.getId());
        actTask.setName(historicTask.getName());
        actTask.setProcessInstanceId(historicTask.getProcessInstanceId());
        actTask.setProcessDefinitionId(historicTask.getProcessDefinitionId());
        actTask.setTaskDefinitionKey(historicTask.getTaskDefinitionKey());
        actTask.setDescription(historicTask.getDescription());
        actTask.setAssignee(historicTask.getAssignee());
        actTask.setOwner(historicTask.getOwner());
        actTask.setDueDate(historicTask.getDueDate());
        actTask.setPriority(historicTask.getPriority());
        actTask.setCategory(historicTask.getCategory());
        actTask.setFormKey(historicTask.getFormKey());
        return actTask;
    }
    
    // ... 实现其他方法
} 