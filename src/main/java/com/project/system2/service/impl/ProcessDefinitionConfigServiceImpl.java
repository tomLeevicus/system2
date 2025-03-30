package com.project.system2.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.project.system2.domain.entity.SysProcessConfig;
import com.project.system2.mapper.SysProcessConfigMapper;
import com.project.system2.service.IProcessDefinitionConfigService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProcessDefinitionConfigServiceImpl extends ServiceImpl<SysProcessConfigMapper, SysProcessConfig> implements IProcessDefinitionConfigService {

    @Override
    public Map<String, SysProcessConfig> getProcessConfigMap() {
        LambdaQueryWrapper<SysProcessConfig> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysProcessConfig::getStatus, "0"); // 只获取正常状态的配置
        List<SysProcessConfig> configList = list(wrapper);
        
        return configList.stream()
            .collect(Collectors.toMap(
                SysProcessConfig::getProcessKey,
                config -> config,
                (existing, replacement) -> existing
            ));
    }
    
    @Override
    public SysProcessConfig getByProcessKey(String processKey) {
        LambdaQueryWrapper<SysProcessConfig> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysProcessConfig::getProcessKey, processKey)
               .eq(SysProcessConfig::getStatus, "0"); // 只获取正常状态的配置
        return getOne(wrapper);
    }
} 