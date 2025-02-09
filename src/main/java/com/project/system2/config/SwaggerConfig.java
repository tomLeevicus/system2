package com.project.system2.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    @Primary
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("资产管理系统API文档")
                .description("固定资产管理相关接口")
                .version("v1.0"))
            .externalDocs(new ExternalDocumentation()
                .description("接口规范文档")
                .url("http://internal.wiki/api-spec"))
            .components(new Components()
                .addSecuritySchemes("Authorization", new SecurityScheme()
                    .name("Authorization")
                    .type(SecurityScheme.Type.HTTP)
                    .scheme("bearer")
                    .bearerFormat("JWT")));
    }
} 