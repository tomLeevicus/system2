package com.project.system2.service;

import com.project.system2.domain.entity.ActProcessDefinition;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IActProcessDefinitionService {
    
    /**
     * 查询流程定义列表
     */
    List<ActProcessDefinition> selectProcessDefinitionList(ActProcessDefinition processDefinition);
    
    /**
     * 部署流程定义
     */
    String deployProcessDefinition(String name, String category, MultipartFile file);
    
    /**
     * 删除流程定义
     */
    void deleteProcessDefinition(String deploymentId);
    
    /**
     * 激活或挂起流程定义
     */
    void updateProcessDefinitionState(String id, boolean active);
    
    /**
     * 获取流程定义图像
     */
    byte[] getProcessDefinitionImage(String processDefinitionId);
    
    /**
     * 获取流程定义XML
     */
    String getProcessDefinitionXML(String processDefinitionId);
} 