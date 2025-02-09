package com.project.system2.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.project.system2.domain.entity.ActProcessDefinition;
import com.project.system2.domain.entity.SysUser;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.springframework.web.multipart.MultipartFile;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import java.util.List;
import java.util.Map;

public interface IActProcessDefinitionService {
    
    /**
     * 分页查询流程定义列表
     */
    Page<ActProcessDefinition> selectProcessDefinitionPage(Page<ActProcessDefinition> page, ActProcessDefinition processDefinition);

    /**
     * 根据ID查询流程定义
     */
    ActProcessDefinition selectProcessDefinitionById(String processDefinitionId);

    /**
     * 部署流程定义
     */
    String deployProcessDefinition(String name, String category, MultipartFile file);

    /**
     * 删除流程定义
     */
    void deleteProcessDefinition(String deploymentId);

    /**
     * 更新流程定义状态
     */
    void updateProcessDefinitionState(String processDefinitionId, boolean suspended);

    /**
     * 获取流程定义图片
     */
    byte[] getProcessDefinitionImage(String processDefinitionId);

    /**
     * 获取流程定义XML
     */
    String getProcessDefinitionXML(String processDefinitionId);

    /**
     * 根据审核人获取流程定义列表
     */
    List<ActProcessDefinition> getProcessDefinitionsByReviewer(String reviewerId);

    /**
     * 获取可选的审批领导列表
     */
    List<Map<String, Object>> getApprovalLeaders();

    /**
     * 启动流程实例
     */
    ProcessInstance startProcess(String processDefinitionKey, Map<String, Object> variables);

    /**
     * 完成任务
     */
    void completeTask(String taskId, Map<String, Object> variables);

    /**
     * 认领任务
     */
    void claimTask(String taskId, String userId);

    /**
     * 取消认领任务
     */
    void unclaimTask(String taskId);

    /**
     * 获取流程实例的任务
     */
    List<Task> getTasksByProcessInstanceId(String processInstanceId);

    /**
     * 获取用户的任务
     */
    List<Task> getTasksByUserId(String userId);

    /**
     * 获取流程变量
     */
    Map<String, Object> getProcessVariables(String processInstanceId);

    /**
     * 设置流程审批人
     */
    void setProcessReviewers(String processDefinitionId, List<Long> reviewerIds);

    /**
     * 获取流程审批人
     */
    List<SysUser> getProcessReviewers(String processDefinitionId);

    /**
     * 获取最新版本的流程定义
     */
    ActProcessDefinition getLatestProcessDefinition(String processKey);

    /**
     * 根据角色获取审批人列表
     */
    List<Map<String, Object>> getApproversByRoleKey(String roleKey);

    /**
     * 同步流程配置
     */
    void syncProcessConfig(String processKey);

    /**
     * 插入流程定义
     */
    void insertProcessDefinition(ActProcessDefinition processDefinition);
} 