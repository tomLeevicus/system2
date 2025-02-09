package com.project.system2.domain;

import com.project.system2.domain.validator.VariableValidator;
import lombok.Data;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
@Builder
public class ProcessVariableConfig {
    /**
     * 参数名称
     */
    private String name;
    
    /**
     * 参数标签
     */
    private String label;
    
    /**
     * 参数类型
     */
    private String type;
    
    /**
     * 是否必填
     */
    private boolean required;
    
    /**
     * 默认值
     */
    private Object defaultValue;
    
    /**
     * 验证规则
     */
    private List<VariableValidator> validators;
    
    /**
     * 验证参数
     */
    public void validate(Map<String, Object> variables) {
        Object value = variables.get(name);
        
        // 必填验证
        if (required && (value == null || String.valueOf(value).trim().isEmpty())) {
            throw new RuntimeException(String.format("参数[%s]不能为空", label));
        }
        
        // 类型验证
        if (value != null) {
            validateType(value);
        }
        
        // 自定义验证
        if (validators != null) {
            for (VariableValidator validator : validators) {
                validator.validate(name, value);
            }
        }
    }
    
    private void validateType(Object value) {
        switch (type.toLowerCase()) {
            case "number":
                if (!(value instanceof Number)) {
                    throw new RuntimeException(String.format("参数[%s]必须是数字", label));
                }
                break;
            case "integer":
                if (!(value instanceof Integer)) {
                    throw new RuntimeException(String.format("参数[%s]必须是整数", label));
                }
                break;
            case "decimal":
                if (!(value instanceof Double || value instanceof Float || value instanceof BigDecimal)) {
                    throw new RuntimeException(String.format("参数[%s]必须是小数", label));
                }
                break;
            case "boolean":
                if (!(value instanceof Boolean)) {
                    throw new RuntimeException(String.format("参数[%s]必须是布尔值", label));
                }
                break;
            case "date":
                // 日期格式验证
                break;
        }
    }
} 