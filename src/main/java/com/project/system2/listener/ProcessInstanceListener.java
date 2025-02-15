package com.project.system2.listener;

import com.project.system2.domain.entity.ActProcessInstance;
import com.project.system2.service.IActProcessInstanceService;
import org.flowable.common.engine.api.delegate.Expression;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;

@Component("processInstanceListener")
public class ProcessInstanceListener implements ExecutionListener {

    private final IActProcessInstanceService processInstanceService;
    private final RuntimeService runtimeService;

    @Autowired
    public ProcessInstanceListener(
        IActProcessInstanceService processInstanceService,
        RuntimeService runtimeService
    ) {
        this.processInstanceService = processInstanceService;
        this.runtimeService = runtimeService;
    }

    @Override
    public void notify(DelegateExecution execution) {
        String eventName = execution.getEventName();
        String processInstanceId = execution.getProcessInstanceId();

        switch (eventName) {
            case ExecutionListener.EVENTNAME_START:
                handleProcessStart(execution);
                break;
            case ExecutionListener.EVENTNAME_END:
                handleProcessEnd(processInstanceId);
                break;
        }
    }

    private void handleProcessStart(DelegateExecution execution) {
        ActProcessInstance instance = new ActProcessInstance();
        
        // 基础信息
        instance.setId(execution.getProcessInstanceId());
        instance.setProcessDefinitionId(execution.getProcessDefinitionId());
        instance.setBusinessKey(execution.getProcessInstanceBusinessKey());
        instance.setStatus("running");
        instance.setSuspended(false);
        instance.setStartTime(new Date());
        instance.setDelFlag(0);
        
        // 解析流程定义信息
        String[] defParts = execution.getProcessDefinitionId().split(":");
        instance.setProcessDefinitionKey(defParts[0]);
        instance.setProcessDefinitionVersion(Integer.parseInt(defParts[1]));
        
        // 获取流程实例名称
        Optional.ofNullable(
            runtimeService.createProcessInstanceQuery()
                .processInstanceId(execution.getProcessInstanceId())
                .singleResult()
        ).ifPresent(processInstance -> 
            instance.setName(processInstance.getName())
        );
        
        processInstanceService.syncInstance(instance);
    }

    private void handleProcessEnd(String processInstanceId) {
        ActProcessInstance instance = new ActProcessInstance();
        instance.setId(processInstanceId);
        instance.setStatus("completed");
        instance.setEndTime(new Date());
        processInstanceService.syncInstance(instance);
    }
} 