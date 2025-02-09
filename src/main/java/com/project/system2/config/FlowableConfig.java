package com.project.system2.config;

import org.flowable.engine.*;
import org.flowable.spring.SpringProcessEngineConfiguration;
import org.flowable.spring.boot.EngineConfigurationConfigurer;
import org.flowable.spring.boot.ProcessEngineConfigurationConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.io.IOException;

@Configuration
public class FlowableConfig implements ProcessEngineConfigurationConfigurer {
    
    @Override
    public void configure(SpringProcessEngineConfiguration config) {
        // 7.x必须配置项
        config.setDatabaseType("mysql");
        config.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_CREATE_DROP);
        config.setDatabaseCatalog("flowable_db");
        config.setHistory("audit");
        config.setAsyncExecutorActivate(false);
        
        // 自动部署配置
        config.setDeploymentMode("single-resource");
        config.setDeploymentName("System2 Deployments");
        
        // 7.x版本通过排除依赖替代直接禁用
        // 已在pom.xml中排除flowable-event-registry-spring依赖
    }

    // 移除所有手动定义的Service Bean
} 