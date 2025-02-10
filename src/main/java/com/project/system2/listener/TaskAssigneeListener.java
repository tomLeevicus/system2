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
        // 添加详细日志输出
        log.debug("处理任务[{}]分配，当前流程变量：{}", 
            delegateTask.getId(), delegateTask.getVariables());
        
        Object leaderId = delegateTask.getVariable("leaderId");
        if (leaderId == null) {
            log.error("流程变量缺失：leaderId未设置");
            throw new FlowableException("leaderId流程变量未定义");
        }
        
        // 添加变量类型检查
        if (!(leaderId instanceof String)) {
            log.error("非法leaderId类型：{}", leaderId.getClass());
            throw new FlowableException("leaderId必须是字符串类型");
        }
        
        // 如果是驳回后再次到达用户申请节点
        if("用户申请".equals(delegateTask.getName())){
            String initiator = (String) delegateTask.getVariable("userId");
            delegateTask.setAssignee(initiator);
            log.info("驳回后重置处理人为发起人：{}", initiator);
        } else {
            delegateTask.setAssignee(leaderId.toString());
            log.info("任务[{}]分配成功，处理人：{}", delegateTask.getId(), leaderId);
        }
    }
    
    private String getDepartmentManager(String department) {
        // 实现部门负责人查询逻辑
        return "manager_" + department; 
    }
} 