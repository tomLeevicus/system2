package com.project.system2.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.project.system2.domain.entity.ActTaskInfo;

import java.util.List;
import java.util.Map;

/**
 * 任务信息服务接口
 */
public interface IActTaskInfoService extends IService<ActTaskInfo> {

    /**
     * 获取用户待办任务
     *
     * @param page 分页参数
     * @param userId 用户ID
     * @param taskName 任务名称
     * @param processInstanceId 流程实例ID
     * @return 任务列表
     */
    Page<ActTaskInfo> getUserTasks(Page<ActTaskInfo> page, String userId, String taskName, String processInstanceId);

    /**
     * 同步Flowable任务到自定义任务表
     *
     * @param taskId 任务ID
     * @return 是否同步成功
     */
    boolean syncFlowableTask(String taskId);

    /**
     * 通过流程实例ID同步所有任务
     *
     * @param processInstanceId 流程实例ID
     * @return 同步的任务数量
     */
    int syncTasksByProcessInstanceId(String processInstanceId);

    /**
     * 完成任务
     *
     * @param taskId 任务ID
     * @param variables 任务变量
     * @param comment 任务评论
     * @return 是否完成成功
     */
    boolean completeTask(String taskId, Map<String, Object> variables, String comment);

    /**
     * 认领任务
     *
     * @param taskId 任务ID
     * @param userId 用户ID
     * @return 是否认领成功
     */
    boolean claimTask(String taskId, String userId);

    /**
     * 取消认领任务
     *
     * @param taskId 任务ID
     * @return 是否取消认领成功
     */
    boolean unclaimTask(String taskId);

    /**
     * 委派任务
     *
     * @param taskId 任务ID
     * @param userId 用户ID
     * @return 是否委派成功
     */
    boolean delegateTask(String taskId, String userId);

    /**
     * 获取任务详情
     *
     * @param taskId 任务ID
     * @return 任务详情
     */
    ActTaskInfo getTaskDetail(String taskId);
} 