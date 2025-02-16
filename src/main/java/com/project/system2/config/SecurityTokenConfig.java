package com.project.system2.config;

import java.nio.charset.StandardCharsets;

import javax.crypto.SecretKey;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import io.jsonwebtoken.security.Keys;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
@Validated
@Configuration
@ConfigurationProperties(prefix = "security.token")
public class SecurityTokenConfig {
    /**
     * 令牌密钥（至少32字符）
     */
    @NotBlank(message = "令牌密钥不能为空")
    private String secret;
    
    /**
     * 令牌有效期（分钟）
     */
    private int expireTime = 120;
    
    /**
     * 自动刷新阈值（分钟）
     */
    private int refreshTime = 30;
    
    /**
     * 令牌前缀
     */
    private String prefix = "Bearer ";
    
    /**
     * 请求头字段
     */
    private String header = "Authorization";

    @Bean
    public SecretKey jwtSecretKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }
}