package com.project.system2.service.impl;

import com.project.system2.domain.entity.ActProcessDefinition;
import com.project.system2.service.IActProcessDefinitionService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class ActProcessDefinitionServiceImpl implements IActProcessDefinitionService {

    @Autowired
    private RepositoryService repositoryService;

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
    public String deployProcessDefinition(String name, String category, MultipartFile file) {
        try {
            Deployment deployment = repositoryService.createDeployment()
                    .name(name)
                    .category(category)
                    .addInputStream(file.getOriginalFilename(), file.getInputStream())
                    .deploy();
            return deployment.getId();
        } catch (IOException e) {
            throw new RuntimeException("部署流程定义失败", e);
        }
    }

    @Override
    public void deleteProcessDefinition(String deploymentId) {
        repositoryService.deleteDeployment(deploymentId, true);
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