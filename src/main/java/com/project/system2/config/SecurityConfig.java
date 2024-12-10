package com.project.system2.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.project.system2.security.filter.JwtAuthenticationTokenFilter;
import com.project.system2.security.handle.AuthenticationEntryPointImpl;
import com.project.system2.security.handle.LogoutSuccessHandlerImpl;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private AuthenticationEntryPointImpl unauthorizedHandler;

    @Autowired
    private LogoutSuccessHandlerImpl logoutSuccessHandler;

    @Autowired
    private JwtAuthenticationTokenFilter authenticationTokenFilter;

    /**
     * 允许匿名访问的地址
     */
    private static final String[] ANONYMOUS_URLS = {
        "/auth/login",
        "/auth/register",
        "/auth/captcha",
        "/profile/*",
        "/common/download",
        "/common/download/resource",
        "/swagger-ui.html",
        "/swagger-resources",
        "/swagger-resources/*",
        "/v2/api-docs",
        "/v3/api-docs",
        "/druid",
        "/druid/*",
        "/actuator",
        "/actuator/*"
    };

    /**
     * 静态资源
     */
    private static final String[] STATIC_URLS = {
        "/",
        "/*.html",
        "/static/*",
        "/static/css/*",
        "/static/js/*",
        "/static/img/*",
        "/profile/*",
        "/avatar/*",
        "/file/*"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/login", "/auth/captcha").permitAll()
                .requestMatchers("/profile/**").permitAll()
                .requestMatchers("/common/download/**").permitAll()
                .requestMatchers("/swagger-ui.html", "/swagger-resources/**", "/webjars/**", "/*/api-docs").permitAll()
                .requestMatchers("/druid/**").permitAll()
                .requestMatchers("/actuator/**").permitAll()
                .requestMatchers("/", "/*.html", "/**/*.html", "/**/*.css", "/**/*.js").permitAll()
                .anyRequest().authenticated()
            );

        http.addFilterBefore(authenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * 强散列哈希加密实现
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 认证管理器
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
} 