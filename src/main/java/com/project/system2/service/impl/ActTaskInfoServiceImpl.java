package com.project.system2.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.project.system2.domain.entity.ActTaskInfo;
import com.project.system2.mapper.ActTaskInfoMapper;
import com.project.system2.service.IActTaskInfoService;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;

@Slf4j
@Service
public class ActTaskInfoServiceImpl extends ServiceImpl<ActTaskInfoMapper, ActTaskInfo> implements IActTaskInfoService {

    @Autowired
    private TaskService taskService;
    
    @Autowired
    private RuntimeService runtimeService;
    
    @Autowired
    private HistoryService historyService;

    @Override
    public Page<ActTaskInfo> getUserTasks(Page<ActTaskInfo> page, String userId, String taskName, String processInstanceId) {
        return baseMapper.selectUserTasks(page, userId, taskName, processInstanceId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean syncFlowableTask(String taskId) {
        try {
            // 从Flowable获取任务
            Task task = taskService.createTaskQuery()
                .taskId(taskId)
                .singleResult();
                
            if (task == null) {
                log.warn("任务不存在: {}", taskId);
                return false;
            }
            
            // 转换为ActTaskInfo
            ActTaskInfo taskInfo = new ActTaskInfo();
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
            taskInfo.setStatus(task.isSuspended() ? "suspended" : "pending");
            taskInfo.setSuspended(task.isSuspended());
            
            // 获取业务键
            String businessKey = runtimeService.createProcessInstanceQuery()
                .processInstanceId(task.getProcessInstanceId())
                .singleResult()
                .getBusinessKey();
            taskInfo.setBusinessKey(businessKey);
            
            // 保存或更新
            return saveOrUpdate(taskInfo);
        } catch (Exception e) {
            log.error("同步任务失败: {}", taskId, e);
            throw new RuntimeException("同步任务失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int syncTasksByProcessInstanceId(String processInstanceId) {
        try {
            // 从Flowable获取所有任务
            var tasks = taskService.createTaskQuery()
                .processInstanceId(processInstanceId)
                .list();
                
            int count = 0;
            for (Task task : tasks) {
                if (syncFlowableTask(task.getId())) {
                    count++;
                }
            }
            
            return count;
        } catch (Exception e) {
            log.error("同步流程实例任务失败: {}", processInstanceId, e);
            throw new RuntimeException("同步流程实例任务失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean completeTask(String taskId, Map<String, Object> variables, String comment) {
        try {
            // 添加评论
            if (comment != null && !comment.isEmpty()) {
                Task task = taskService.createTaskQuery()
                    .taskId(taskId)
                    .singleResult();
                    
                if (task != null) {
                    taskService.addComment(taskId, task.getProcessInstanceId(), comment);
                }
            }
            
            // 完成任务
            taskService.complete(taskId, variables);
            
            // 更新任务状态
            ActTaskInfo taskInfo = getById(taskId);
            if (taskInfo != null) {
                taskInfo.setStatus("completed");
                taskInfo.setUpdateTime(new Date());
                updateById(taskInfo);
            }
            
            return true;
        } catch (Exception e) {
            log.error("完成任务失败: {}", taskId, e);
            throw new RuntimeException("完成任务失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean claimTask(String taskId, String userId) {
        try {
            taskService.claim(taskId, userId);
            
            // 更新任务信息
            ActTaskInfo taskInfo = getById(taskId);
            if (taskInfo != null) {
                taskInfo.setAssignee(userId);
                taskInfo.setClaimTime(new Date());
                updateById(taskInfo);
            }
            
            return true;
        } catch (Exception e) {
            log.error("认领任务失败: {}", taskId, e);
            throw new RuntimeException("认领任务失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean unclaimTask(String taskId) {
        try {
            taskService.unclaim(taskId);
            
            // 更新任务信息
            ActTaskInfo taskInfo = getById(taskId);
            if (taskInfo != null) {
                taskInfo.setAssignee(null);
                taskInfo.setClaimTime(null);
                updateById(taskInfo);
            }
            
            return true;
        } catch (Exception e) {
            log.error("取消认领任务失败: {}", taskId, e);
            throw new RuntimeException("取消认领任务失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delegateTask(String taskId, String userId) {
        try {
            taskService.delegateTask(taskId, userId);
            
            // 更新任务信息
            ActTaskInfo taskInfo = getById(taskId);
            if (taskInfo != null) {
                taskInfo.setOwner(taskInfo.getAssignee());
                taskInfo.setAssignee(userId);
                updateById(taskInfo);
            }
            
            return true;
        } catch (Exception e) {
            log.error("委派任务失败: {}", taskId, e);
            throw new RuntimeException("委派任务失败: " + e.getMessage());
        }
    }

    @Override
    public ActTaskInfo getTaskDetail(String taskId) {
        return baseMapper.selectById(taskId);
    }
} 