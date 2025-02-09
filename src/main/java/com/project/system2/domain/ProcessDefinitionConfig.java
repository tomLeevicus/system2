package com.project.system2.domain;

import com.project.system2.domain.converter.ProcessVariableConverter;
import lombok.Data;
import lombok.Builder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 流程定义配置类
 * 用于封装流程定义的配置信息，包括:
 * 1. 流程基本信息(名称、标识、分类等)
 * 2. 审批角色配置
 * 3. 流程参数配置
 * 4. 参数验证和转换功能
 */
@Data
@Builder
public class ProcessDefinitionConfig {
    /**
     * 流程名称
     */
    private String name;
    
    /**
     * 流程标识
     */
    private String key;
    
    /**
     * 流程分类
     */
    private String category;
    
    /**
     * 审批角色标识
     */
    private String roleKey;
    
    /**
     * 必填参数列表
     */
    private List<String> requiredVariables;
    
    /**
     * 参数配置列表
     */
    private List<ProcessVariableConfig> variableConfigs;
    
    /**
     * 参数转换器
     */
    private ProcessVariableConverter variableConverter;
    
    /**
     * 验证流程变量
     * 1. 检查必填参数是否存在
     * 2. 执行参数格式验证
     */
    public void validateVariables(Map<String, Object> variables) {
        // 基础必填参数验证
        if (requiredVariables != null) {
            for (String required : requiredVariables) {
                if (!variables.containsKey(required)) {
                    throw new RuntimeException(String.format("缺少必填参数: %s", required));
                }
            }
        }
        
        // 参数格式验证
        if (variableConfigs != null) {
            for (ProcessVariableConfig config : variableConfigs) {
                config.validate(variables);
            }
        }
    }
    
    /**
     * 转换流程变量
     * 对流程变量进行类型转换和格式化处理
     */
    public Map<String, Object> convertVariables(Map<String, Object> variables) {
        Map<String, Object> converted = new HashMap<>(variables);
        
        // 使用自定义转换器
        if (variableConverter != null) {
            converted = variableConverter.convert(converted);
        }
        
        return converted;
    }
} 