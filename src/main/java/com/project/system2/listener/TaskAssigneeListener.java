package com.project.system2.listener;

import org.flowable.common.engine.api.FlowableException;
import org.flowable.engine.delegate.TaskListener;
import org.flowable.task.service.delegate.DelegateTask;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

@Component
@Slf4j
public class TaskAssigneeListener implements TaskListener {
    
    @Override
    public void notify(DelegateTask delegateTask) {
        // 从流程变量获取leaderId
        String leaderId = (String) delegateTask.getVariable("leaderId");
        String processStarter = (String) delegateTask.getVariable("userId");
        
        // 校验审核人合法性
        if (StringUtils.isEmpty(leaderId)) {
            throw new FlowableException("审核人未指定");
        }
        if (leaderId.equals(processStarter)) {
            throw new FlowableException("审核人不能是流程发起人");
        }
        
        delegateTask.setAssignee(leaderId);
        log.info("任务[{}]分配处理人：{}", delegateTask.getName(), leaderId);
    }
    
    private String getDepartmentManager(String department) {
        // 实现部门负责人查询逻辑
        return "manager_" + department; 
    }
} 