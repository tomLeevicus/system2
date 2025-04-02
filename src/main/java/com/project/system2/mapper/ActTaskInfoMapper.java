package com.project.system2.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.system2.domain.entity.ActTaskInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 任务信息数据访问层
 */
@Mapper
public interface ActTaskInfoMapper extends BaseMapper<ActTaskInfo> {

    /**
     * 获取用户待办任务
     *
     * @param page 分页参数
     * @param userId 用户ID
     * @param taskName 任务名称
     * @param processInstanceId 流程实例ID
     * @return 任务列表
     */
    Page<ActTaskInfo> selectUserTasks(Page<ActTaskInfo> page, 
                                      @Param("userId") String userId,
                                      @Param("taskName") String taskName,
                                      @Param("processInstanceId") String processInstanceId);

    /**
     * 通过流程实例ID获取任务
     *
     * @param processInstanceId 流程实例ID
     * @return 任务列表
     */
    List<ActTaskInfo> selectTasksByProcessInstanceId(@Param("processInstanceId") String processInstanceId);
} 