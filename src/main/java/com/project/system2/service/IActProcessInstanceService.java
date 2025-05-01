package com.project.system2.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.system2.domain.entity.ActProcessInstance;
import com.project.system2.domain.entity.ActTaskInfo;
import com.project.system2.domain.query.ProcessInstanceQuery;
import org.flowable.bpmn.model.FlowElement;

import java.util.List;
import java.util.Map;

public interface IActProcessInstanceService {
    
    /**
     * 启动流程实例
     */
    String startProcess(String processDefinitionKey, Map<String, Object> variables);
    
    /**
     * 分页查询流程实例
     */
    Page<ActProcessInstance> listProcessInstances(ProcessInstanceQuery query);
    
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
     * 获取用户待办任务
     * 
     * @param page 分页参数
     * @param userId 用户ID
     * @return 待办任务分页列表
     */
    Page<ActTaskInfo> getTodoInstances(Page<ActTaskInfo> page, String userId);
    
    /**
     * 完成用户任务（需要指定处理人）
     */
    void completeUserTask(String taskId, Map<String, Object> variables);
    
    /**
     * 处理网关任务（无需指定处理人）
     */
    void completeGatewayTask(String taskId, Map<String, Object> variables);
    
    /**
     * 完成任务（旧方法，建议弃用）
     */
    @Deprecated
    void completeTask(String taskId, Map<String, Object> variables);
    
    /**
     * 同步流程实例
     */
    void syncInstance(ActProcessInstance instance);

    void updateStatus(String processInstanceId, String completed);

    /**
     * 检查后续是否存在网关任务
     */
    boolean hasNextGateway(String taskId);

    /**
     * 获取任务后续流程节点
     * @param taskId 任务ID
     * @return 后续节点列表
     */
    List<FlowElement> getNextFlowElements(String taskId);

    /**
     * 分页查询指定用户发起的流程任务列表
     * @param query 查询条件 (包含分页信息, 使用 TaskQuery 类型接收前端参数)
     * @return 分页结果
     */
    Page<ActProcessInstance> listUserTasks(com.project.system2.domain.query.TaskQuery query);
}