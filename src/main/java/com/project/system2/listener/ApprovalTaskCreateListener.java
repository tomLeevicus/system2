package com.project.system2.listener;

import com.project.system2.mapper.SysUserMapper;
import org.flowable.engine.delegate.TaskListener;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.ProcessEngines;
import org.flowable.task.service.delegate.DelegateTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Date;
import org.flowable.engine.RuntimeService;

@Component
public class ApprovalTaskCreateListener implements TaskListener {
    
    @Autowired
    private SysUserMapper userMapper;
    
    @Autowired
    private RuntimeService runtimeService;
    
    @Override
    public void notify(DelegateTask delegateTask) {
        // 获取流程变量
        String processInstanceId = delegateTask.getProcessInstanceId();
        
        // 获取发起人ID（通过 RuntimeService 获取变量）
        String initiatorId = (String) runtimeService.getVariable(processInstanceId, "initiatorId");
        
        // 设置任务相关变量
        delegateTask.setVariable("taskCreateTime", new Date());
        delegateTask.setVariable("initiatorId", initiatorId);
        
        // 可以在这里添加其他任务相关的处理逻辑
    }
} 