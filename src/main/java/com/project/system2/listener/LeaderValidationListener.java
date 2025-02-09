package com.project.system2.listener;

import com.project.system2.service.ISysUserRoleService;
import lombok.extern.slf4j.Slf4j;
import org.flowable.common.engine.api.FlowableException;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
*@Author: BlueberryLee
*@Date: 2025/2/9 21:40
*@Description: TODO 
*/
@Component
@Slf4j
public class LeaderValidationListener implements ExecutionListener {
    @Autowired
    private ISysUserRoleService sysUserRoleService;

    @Override
    public void notify(DelegateExecution execution) {
        String leaderId = (String) execution.getVariable("leaderId");
        String starterId = (String) execution.getVariable("userId");

        if (!sysUserRoleService.checkUserIsValidLeader(starterId, leaderId)) {
            throw new FlowableException("指定的审核人无审批权限");
        }
    }

}
