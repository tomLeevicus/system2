package com.project.system2.service;

import com.project.system2.domain.entity.SysProcessConfig;
import com.project.system2.domain.entity.SysProcessVariableConfig;
import java.util.List;

public interface ISysProcessConfigService {
    List<SysProcessConfig> selectProcessConfigList(SysProcessConfig config);
    List<SysProcessVariableConfig> selectVariableConfigList(String processKey);
    void saveProcessConfig(SysProcessConfig config);
    void saveVariableConfigs(List<SysProcessVariableConfig> configs);
} 