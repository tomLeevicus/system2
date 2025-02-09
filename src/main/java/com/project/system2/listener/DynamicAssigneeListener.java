package com.project.system2.listener;

import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.FlowElement;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.delegate.TaskListener;
import org.flowable.task.service.delegate.DelegateTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;

@Component
public class DynamicAssigneeListener implements TaskListener {
    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private RepositoryService repositoryService;

    @Override
    public void notify(DelegateTask delegateTask) {
        // 获取流程定义中的自定义配置
        BpmnModel bpmnModel = repositoryService.getBpmnModel(
            delegateTask.getProcessDefinitionId()
        );
        
        FlowElement flowElement = bpmnModel.getFlowElement(
            delegateTask.getTaskDefinitionKey()
        );
        
        // 从流程定义中获取配置的变量名
        String variableName = flowElement.getAttributeValue(
            "http://flowable.org/bpmn", 
            "assigneeVariable"
        );
        
        // 默认使用 approverList
        if (StringUtils.isEmpty(variableName)) {
            variableName = "approverList";
        }

        // 从流程变量获取审批人列表
        List<String> approvers = (List<String>) runtimeService.getVariable(
            delegateTask.getProcessInstanceId(),
            variableName
        );

        // 设置候选人
        if (approvers != null && !approvers.isEmpty()) {
            for (String userId : approvers) {
                delegateTask.addCandidateUser(userId);
            }
        }
    }
} 