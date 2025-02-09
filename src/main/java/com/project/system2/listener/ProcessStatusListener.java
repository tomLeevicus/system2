package com.project.system2.listener;

import com.project.system2.domain.entity.ActProcessInstance;
import com.project.system2.service.IActProcessInstanceService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import com.project.system2.mapper.ActProcessInstanceMapper;
import java.util.Date;

@Component
@Slf4j
public class ProcessStatusListener implements ExecutionListener {

    @Autowired
    private IActProcessInstanceService processInstanceService;

    @Autowired
    private ActProcessInstanceMapper processInstanceMapper;

    @Override
    public void notify(DelegateExecution execution) {
        String eventName = execution.getEventName();
        if ("end".equals(eventName)) {
            // 流程结束时更新状态
            ActProcessInstance instance = new ActProcessInstance();
            instance.setId(execution.getProcessInstanceId());
            instance.setStatus("completed");
            instance.setUpdateTime(new Date());
            processInstanceMapper.updateById(instance);
            
            log.info("流程实例[{}]已结束", execution.getProcessInstanceId());
        }
    }
} 