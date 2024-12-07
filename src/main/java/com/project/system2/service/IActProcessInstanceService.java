package com.project.system2.service;

import java.util.Map;

public interface IActProcessInstanceService {
    
    /**
     * 启动流程实例
     */
    String startProcess(String processDefinitionKey, Map<String, Object> variables);
    
    /**
     * 更新流程实例状态
     */
    void updateProcessInstanceState(String processInstanceId, boolean suspended);
    
    /**
     * 删除流程实例
     */
    void deleteProcessInstance(String processInstanceId, String deleteReason);
    
    /**
     * 获取流程实例详细信息
     */
    Map<String, Object> getProcessInstanceDetail(String processInstanceId);
} 