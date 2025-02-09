package com.project.system2.domain.validator;

/**
 * 参数验证器接口
 */
public interface VariableValidator {
    /**
     * 验证参数值
     * @param name 参数名称
     * @param value 参数值
     */
    void validate(String name, Object value);
} 