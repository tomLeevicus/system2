package com.project.system2.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.project.system2.domain.converter.ProcessVariableConverter;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.io.Serializable;
import java.time.LocalDateTime;
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
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_process_definition_config")
public class ProcessDefinitionConfig implements Serializable {
    
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 流程标识
     */
    private String processKey;

    /**
     * 流程名称
     */
    private String processName;

    /**
     * 审批角色标识
     */
    private String roleKey;
    
    /**
     * 表单路径
     */
    private String formPath;
    
    /**
     * 状态（0正常 1停用）
     */
    private String status;
    
    /**
     * 备注
     */
    private String remark;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    
    /**
     * 流程分类
     */
    private String category;
    
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
     * 用于临时构造特定配置的构造函数
     */
    public ProcessDefinitionConfig(String roleKey) {
        this.roleKey = roleKey;
    }
    
    /**
     * 用于创建流程配置的便捷构造函数
     */
    public ProcessDefinitionConfig(String processKey, String processName, String roleKey, String formPath) {
        this.processKey = processKey;
        this.processName = processName;
        this.roleKey = roleKey;
        this.formPath = formPath;
        this.status = "0"; // 默认正常
    }
    
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