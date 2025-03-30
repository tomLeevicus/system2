package com.project.system2.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.project.system2.domain.ProcessDefinitionConfig;
import com.project.system2.domain.ProcessVariableConfig;
import com.project.system2.domain.entity.SysProcessConfig;
import com.project.system2.domain.entity.SysProcessVariableConfig;
import com.project.system2.domain.validator.VariableValidator;
import com.project.system2.domain.validator.MinValueValidator;
import com.project.system2.domain.validator.MaxValueValidator;
import com.project.system2.mapper.SysProcessConfigMapper;
import com.project.system2.mapper.SysProcessVariableConfigMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.*;
import java.util.stream.Collectors;

@Configuration
@EnableScheduling
@Slf4j
public class ProcessConfig {
    
    @Autowired
    private SysProcessConfigMapper processConfigMapper;
    
    @Autowired
    private SysProcessVariableConfigMapper variableConfigMapper;
    
    @Bean
    public Map<String, SysProcessConfig> processConfigs() {
        Map<String, SysProcessConfig> configs = new HashMap<>();
        
        try {
            // 查询所有有效的流程配置
            List<SysProcessConfig> processConfigs = processConfigMapper.selectList(
                new LambdaQueryWrapper<SysProcessConfig>()
                    .eq(SysProcessConfig::getStatus, "0")
            );
            
            for (SysProcessConfig processConfig : processConfigs) {
                // 查询流程参数配置
                List<SysProcessVariableConfig> variableConfigs = variableConfigMapper.selectList(
                    new LambdaQueryWrapper<SysProcessVariableConfig>()
                        .eq(SysProcessVariableConfig::getProcessKey, processConfig.getProcessKey())
                        .eq(SysProcessVariableConfig::getStatus, "0")
                        .orderByAsc(SysProcessVariableConfig::getSortOrder)
                );
                
                // 在这里我们可以扩展SysProcessConfig的功能，如果需要的话
                // 例如，可以将变量配置等信息存储在适当的地方
                
                // 直接使用SysProcessConfig，无需转换为ProcessDefinitionConfig
                configs.put(processConfig.getProcessKey(), processConfig);
            }
        } catch (Exception e) {
            log.error("加载流程配置失败", e);
        }
        
        return configs;
    }
    
    /**
     * 刷新流程配置
     */
    @Scheduled(fixedDelay = 300000) // 每5分钟刷新一次
    public void refreshProcessConfigs() {
        log.info("开始刷新流程配置...");
        processConfigs();
        log.info("流程配置刷新完成");
    }
} 