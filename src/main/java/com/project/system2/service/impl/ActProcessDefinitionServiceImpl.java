package com.project.system2.service.impl;

import com.project.system2.domain.entity.ActProcessDefinition;
import com.project.system2.domain.entity.ProcessReviewer;
import com.project.system2.mapper.ProcessReviewerMapper;
import com.project.system2.service.IActProcessDefinitionService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.HashSet;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

@Service
@Transactional
public class ActProcessDefinitionServiceImpl implements IActProcessDefinitionService {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private ProcessReviewerMapper processReviewerMapper;

    @Override
    public List<ActProcessDefinition> selectProcessDefinitionList(ActProcessDefinition processDefinition) {
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery()
                .orderByProcessDefinitionVersion().desc();
        
        if (processDefinition != null) {
            if (processDefinition.getKey() != null) {
                processDefinitionQuery.processDefinitionKeyLike("%" + processDefinition.getKey() + "%");
            }
            if (processDefinition.getName() != null) {
                processDefinitionQuery.processDefinitionNameLike("%" + processDefinition.getName() + "%");
            }
            if (processDefinition.getCategory() != null) {
                processDefinitionQuery.processDefinitionCategoryLike("%" + processDefinition.getCategory() + "%");
            }
        }

        List<ProcessDefinition> processDefinitions = processDefinitionQuery.list();
        List<ActProcessDefinition> actProcessDefinitions = new ArrayList<>();

        for (ProcessDefinition definition : processDefinitions) {
            ActProcessDefinition actDefinition = new ActProcessDefinition();
            actDefinition.setId(definition.getId());
            actDefinition.setKey(definition.getKey());
            actDefinition.setName(definition.getName());
            actDefinition.setCategory(definition.getCategory());
            actDefinition.setVersion(definition.getVersion());
            actDefinition.setDescription(definition.getDescription());
            actDefinition.setDeploymentId(definition.getDeploymentId());
            actDefinition.setResourceName(definition.getResourceName());
            actDefinition.setDiagramResourceName(definition.getDiagramResourceName());
            actDefinition.setSuspended(definition.isSuspended());
            actDefinition.setTenantId(definition.getTenantId());
            actProcessDefinitions.add(actDefinition);
        }

        return actProcessDefinitions;
    }

    @Override
    public String deployProcessDefinition(String name, String category, MultipartFile file, List<String> reviewers) {
        try {
            // 1. 部署流程定义
            Deployment deployment = repositoryService.createDeployment()
                .name(name)
                .category(category)
                .addInputStream(file.getOriginalFilename(), file.getInputStream())
                .deploy();
            
            // 2. 获取流程定义ID
            ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .deploymentId(deployment.getId())
                .singleResult();
            
            // 3. 如果有审核员，将其存储到关系表中
            if (reviewers != null && !reviewers.isEmpty()) {
                for (String reviewerId : reviewers) {
                    ProcessReviewer processReviewer = new ProcessReviewer();
                    processReviewer.setProcessDefinitionId(processDefinition.getId());
                    processReviewer.setReviewerId(reviewerId);
                    processReviewerMapper.insert(processReviewer);
                }
            }
            
            return deployment.getId();
        } catch (IOException e) {
            throw new RuntimeException("部署流程定义失败", e);
        }
    }

    @Override
    public void deleteProcessDefinition(String deploymentId) {
        try {
            // 1. 获取该部署ID对应的所有流程定义
            List<ProcessDefinition> processDefinitions = repositoryService.createProcessDefinitionQuery()
                .deploymentId(deploymentId)
                .list();
            
            // 2. 删除这些流程定义关联的审核员记录
            for (ProcessDefinition processDefinition : processDefinitions) {
                processReviewerMapper.delete(
                    new QueryWrapper<ProcessReviewer>()
                        .eq("process_definition_id", processDefinition.getId())
                );
            }
            
            // 3. 删除流程定义
            repositoryService.deleteDeployment(deploymentId, true);
        } catch (Exception e) {
            throw new RuntimeException("删除流程定义失败", e);
        }
    }

    @Override
    public void updateProcessDefinitionState(String id, boolean active) {
        if (active) {
            repositoryService.activateProcessDefinitionById(id);
        } else {
            repositoryService.suspendProcessDefinitionById(id);
        }
    }

    @Override
    public byte[] getProcessDefinitionImage(String processDefinitionId) {
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionId(processDefinitionId)
                .singleResult();
        try (InputStream inputStream = repositoryService.getResourceAsStream(
                processDefinition.getDeploymentId(),
                processDefinition.getDiagramResourceName())) {
            return IOUtils.toByteArray(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("获取流程定义图像失败", e);
        }
    }

    @Override
    public String getProcessDefinitionXML(String processDefinitionId) {
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionId(processDefinitionId)
                .singleResult();
        try (InputStream inputStream = repositoryService.getResourceAsStream(
                processDefinition.getDeploymentId(),
                processDefinition.getResourceName())) {
            return IOUtils.toString(inputStream, "UTF-8");
        } catch (IOException e) {
            throw new RuntimeException("获取流程定义XML失败", e);
        }
    }

    @Override
    public ActProcessDefinition selectProcessDefinitionById(String processDefinitionId) {
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionId(processDefinitionId)
                .singleResult();
                
        return convertToActProcessDefinition(processDefinition);
    }

    @Override
    public List<ActProcessDefinition> getProcessDefinitionsByReviewer(String reviewerId) {
        // 1. 查询该审核员关联的所有流程定义ID
        List<ProcessReviewer> reviewers = processReviewerMapper.selectList(
            new QueryWrapper<ProcessReviewer>()
                .eq("reviewer_id", reviewerId)
        );
        
        if (reviewers.isEmpty()) {
            return Collections.emptyList();
        }
        
        // 2. 获取所有相关的流程定义ID
        List<String> processDefinitionIds = reviewers.stream()
            .map(ProcessReviewer::getProcessDefinitionId)
            .collect(Collectors.toList());
        
        // 3. 查询流程定义详情
        List<ProcessDefinition> definitions = repositoryService.createProcessDefinitionQuery()
            .processDefinitionIds(new HashSet<>(processDefinitionIds))
            .active()
            .list();
        
        // 4. 转换为ActProcessDefinition对象
        return definitions.stream()
            .map(this::convertToActProcessDefinition)
            .collect(Collectors.toList());
    }

    private ActProcessDefinition convertToActProcessDefinition(ProcessDefinition definition) {
        if (definition == null) {
            return null;
        }
        
        ActProcessDefinition actDefinition = new ActProcessDefinition();
        actDefinition.setId(definition.getId());
        actDefinition.setKey(definition.getKey());
        actDefinition.setName(definition.getName());
        actDefinition.setCategory(definition.getCategory());
        actDefinition.setVersion(definition.getVersion());
        actDefinition.setDescription(definition.getDescription());
        actDefinition.setDeploymentId(definition.getDeploymentId());
        actDefinition.setResourceName(definition.getResourceName());
        actDefinition.setDiagramResourceName(definition.getDiagramResourceName());
        actDefinition.setSuspended(definition.isSuspended());
        actDefinition.setTenantId(definition.getTenantId());
        
        return actDefinition;
    }
} 