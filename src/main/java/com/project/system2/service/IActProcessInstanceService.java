package com.project.system2.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.system2.domain.entity.ActProcessInstance;
import java.util.Map;

public interface IActProcessInstanceService {
    
    /**
     * 启动流程实例
     */
    String startProcess(String processDefinitionKey, Map<String, Object> variables);
    
    /**
     * 分页查询流程实例
     */
    Page<ActProcessInstance> listProcessInstances(Page<ActProcessInstance> page, ActProcessInstance processInstance);
    
    /**
     * 获取流程实例详情
     */
    ActProcessInstance getProcessInstanceById(String processInstanceId);
    
    /**
     * 挂起/激活流程实例
     */
    void updateProcessInstanceState(String processInstanceId, boolean suspended);
    
    /**
     * 删除流程实例
     */
    void deleteProcessInstance(String processInstanceId, String deleteReason);
    
    /**
     * 获取流程实例的变量
     */
    Map<String, Object> getProcessInstanceVariables(String processInstanceId);
    
    /**
     * 获取用户待办流程实例
     */
    Page<ActProcessInstance> getTodoInstances(Page<ActProcessInstance> page, String userId);
    
    /**
     * 完成任务
     */
    void completeTask(String taskId, Map<String, Object> variables);
    
    /**
     * 同步流程实例
     */
    void syncInstance(ActProcessInstance instance);

    void updateStatus(String processInstanceId, String completed);
}