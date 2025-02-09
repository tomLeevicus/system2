package com.project.system2.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.project.system2.domain.entity.SysProcessConfig;
import com.project.system2.domain.entity.SysProcessVariableConfig;
import com.project.system2.mapper.SysProcessConfigMapper;
import com.project.system2.mapper.SysProcessVariableConfigMapper;
import com.project.system2.service.ISysProcessConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SysProcessConfigServiceImpl implements ISysProcessConfigService {
    
    @Autowired
    private SysProcessConfigMapper processConfigMapper;
    
    @Autowired
    private SysProcessVariableConfigMapper variableConfigMapper;
    
    @Override
    public List<SysProcessConfig> selectProcessConfigList(SysProcessConfig config) {
        return processConfigMapper.selectList(new LambdaQueryWrapper<SysProcessConfig>()
            .eq(config.getStatus() != null, SysProcessConfig::getStatus, config.getStatus())
            .orderByDesc(SysProcessConfig::getCreateTime));
    }
    
    @Override
    public List<SysProcessVariableConfig> selectVariableConfigList(String processKey) {
        return variableConfigMapper.selectList(new LambdaQueryWrapper<SysProcessVariableConfig>()
            .eq(SysProcessVariableConfig::getProcessKey, processKey)
            .eq(SysProcessVariableConfig::getStatus, "0")
            .orderByAsc(SysProcessVariableConfig::getSortOrder));
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveProcessConfig(SysProcessConfig config) {
        if (config.getId() == null) {
            processConfigMapper.insert(config);
        } else {
            processConfigMapper.updateById(config);
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveVariableConfigs(List<SysProcessVariableConfig> configs) {
        if (configs != null && !configs.isEmpty()) {
            String processKey = configs.get(0).getProcessKey();
            // 删除原有配置
            variableConfigMapper.delete(new LambdaQueryWrapper<SysProcessVariableConfig>()
                .eq(SysProcessVariableConfig::getProcessKey, processKey));
            // 保存新配置
            for (SysProcessVariableConfig config : configs) {
                variableConfigMapper.insert(config);
            }
        }
    }
} 