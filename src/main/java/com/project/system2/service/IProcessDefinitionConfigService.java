package com.project.system2.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.project.system2.domain.entity.SysProcessConfig;
import java.util.Map;

public interface IProcessDefinitionConfigService extends IService<SysProcessConfig> {
    
    /**
     * 获取所有流程配置的映射
     * @return 以流程键为Key的配置Map
     */
    Map<String, SysProcessConfig> getProcessConfigMap();
    
    /**
     * 根据流程键获取流程配置
     * @param processKey 流程键
     * @return 流程配置
     */
    SysProcessConfig getByProcessKey(String processKey);
} 