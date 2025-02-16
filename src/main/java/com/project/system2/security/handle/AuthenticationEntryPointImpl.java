package com.project.system2.security.handle;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.system2.common.core.domain.R;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.springframework.security.authentication.BadCredentialsException;

/**
 * 认证失败处理类 返回未授权
 */
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e)
            throws IOException, ServletException {
        
        String msg = "认证失败，请重新登录";
        Throwable cause = e.getCause();
        
        if (cause instanceof ExpiredJwtException) {
            msg = "Token已过期，请重新登录";
        } else if (cause instanceof JwtException) {
            msg = "Token无效或已失效";
        } else if (e instanceof BadCredentialsException) {
            msg = "无效的验证码或凭证";
        }

        response.setStatus(200);
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), R.fail(msg));
    }
} 