package com.project.system2.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.system2.domain.entity.ActTask;
import java.util.Map;
import java.util.List;

public interface IActTaskService {
    
    /**
     * 获取用户的任务列表
     */
    Page<ActTask> getTodoTasks(Page<ActTask> page, String userId);
    
    /**
     * 获取用户已完成的任务列表
     */
    Page<ActTask> getDoneTasks(Page<ActTask> page, String userId);
    
    /**
     * 完成任务
     */
    void completeTask(String taskId, Map<String, Object> variables);
    
    /**
     * 签收任务
     */
    void claimTask(String taskId, String userId);
    
    /**
     * 取消签收任务
     */
    void unclaimTask(String taskId);
    
    /**
     * 委派任务
     */
    void delegateTask(String taskId, String userId);
    
    /**
     * 转办任务
     */
    void transferTask(String taskId, String userId);
    
    /**
     * 获取任务表单数据
     */
    Map<String, Object> getTaskFormData(String taskId);
    
    /**
     * 获取任务变量
     */
    Map<String, Object> getTaskVariables(String taskId);
    
    /**
     * 添加任务评论
     */
    void addTaskComment(String taskId, String message);
    
    /**
     * 获取任务评论列表
     */
    List<String> getTaskComments(String taskId);
} 