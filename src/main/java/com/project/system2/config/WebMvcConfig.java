package com.project.system2.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 如果确实需要添加拦截器，请替换YourCustomInterceptor为实际类
        // 例如：registry.addInterceptor(new LogInterceptor())...
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 删除原有Swagger资源映射配置
    }
} 