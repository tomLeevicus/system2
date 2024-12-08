package com.project.system2.service.impl;

import com.project.system2.service.IActProcessInstanceService;
import jakarta.annotation.Resource;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ActProcessInstanceServiceImpl implements IActProcessInstanceService {

    @Autowired
    private RuntimeService runtimeService;

    @Override
    public String startProcess(String processDefinitionKey, Map<String, Object> variables) {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey, variables);
        return processInstance.getId();
    }

    @Override
    public void updateProcessInstanceState(String processInstanceId, boolean suspended) {
        if (suspended) {
            runtimeService.suspendProcessInstanceById(processInstanceId);
        } else {
            runtimeService.activateProcessInstanceById(processInstanceId);
        }
    }

    @Override
    public void deleteProcessInstance(String processInstanceId, String deleteReason) {
        runtimeService.deleteProcessInstance(processInstanceId, deleteReason);
    }

    @Override
    public Map<String, Object> getProcessInstanceDetail(String processInstanceId) {
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult();
        
        Map<String, Object> result = new HashMap<>();
        if (processInstance != null) {
            result.put("id", processInstance.getId());
            result.put("processDefinitionId", processInstance.getProcessDefinitionId());
            result.put("processDefinitionKey", processInstance.getProcessDefinitionKey());
            result.put("processDefinitionName", processInstance.getProcessDefinitionName());
            result.put("processDefinitionVersion", processInstance.getProcessDefinitionVersion());
            result.put("deploymentId", processInstance.getDeploymentId());
            result.put("businessKey", processInstance.getBusinessKey());
            result.put("isSuspended", processInstance.isSuspended());
            result.put("startTime", processInstance.getStartTime());
            result.put("startUserId", processInstance.getStartUserId());
            result.put("variables", runtimeService.getVariables(processInstanceId));
        }
        return result;
    }
} 