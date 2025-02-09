package com.project.system2.domain.converter;

import java.util.Map;

/**
 * 流程变量转换器接口
 * 用于在流程启动前对变量进行转换处理
 */
public interface ProcessVariableConverter {
    /**
     * 转换流程变量
     * @param variables 原始变量Map
     * @return 转换后的变量Map
     */
    Map<String, Object> convert(Map<String, Object> variables);
} 