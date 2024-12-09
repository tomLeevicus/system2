package com.project.system2.config;

import com.project.system2.security.filter.JwtAuthenticationTokenFilter;
import com.project.system2.security.handle.AuthenticationEntryPointImpl;
import com.project.system2.security.handle.LogoutSuccessHandlerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public LogoutSuccessHandlerImpl logoutSuccessHandler() {
        return new LogoutSuccessHandlerImpl();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // CSRF禁用
            .csrf().disable()
            // 认证失败处理类
            .exceptionHandling()
                .authenticationEntryPoint(new AuthenticationEntryPointImpl())
                .and()
            // 基于token，不需要session
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
            // 过滤请求
            .authorizeHttpRequests()
                // 对于登录、验证码等接口允许匿名访问
                .requestMatchers("/auth/login", "/auth/captcha").permitAll()
                // 静态资源允许匿名访问
                .requestMatchers("/", "/favicon.ico").permitAll()
                .requestMatchers("/static/**").permitAll()
                .requestMatchers("/*.html", "/**/*.html").permitAll()
                .requestMatchers("/*.css", "/**/*.css").permitAll()
                .requestMatchers("/*.js", "/**/*.js").permitAll()
                // 除上面外的所有请求全部需要鉴权认证
                .anyRequest().authenticated()
                .and()
            // 添加JWT filter
            .addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class)
            // 添加CORS filter
            .cors().and()
            // 登出配置
            .logout()
                .logoutUrl("/auth/logout")
                .logoutSuccessHandler(logoutSuccessHandler())
                .deleteCookies("JSESSIONID");
        
        return http.build();
    }
} 