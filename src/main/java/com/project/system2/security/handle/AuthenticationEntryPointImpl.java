package com.project.system2.security.handle;

import java.io.IOException;

import com.project.system2.common.core.constant.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.system2.common.core.domain.R;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;

/**
 * 认证失败处理类 返回未授权
 */
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
    private static final Logger log = LoggerFactory.getLogger(AuthenticationEntryPointImpl.class);

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e)
            throws IOException, ServletException {
        
        int code = HttpStatus.UNAUTHORIZED;
        String msg = "认证失败，请重新登录";
        Throwable cause = e.getCause();
        
        if (e instanceof BadCredentialsException) {
            code = HttpStatus.BAD_CREDENTIALS;
            msg = "无效的凭证或验证码";
        } else if (e instanceof InsufficientAuthenticationException) {
            code = HttpStatus.MISSING_TOKEN;
            msg = "请求未携带有效Token";
        } else if (cause != null) {
            if (cause instanceof ExpiredJwtException) {
                code = HttpStatus.TOKEN_EXPIRED;
                msg = "Token已过期，请重新登录";
            } else if (cause instanceof JwtException) {
                code = HttpStatus.INVALID_TOKEN;
                msg = "Token无效或已失效";
            }
        }
        response.setStatus(HttpStatus.UNAUTHORIZED);  // Main status code
        response.setHeader("X-Error-Code", String.valueOf(code));
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), R.fail(code, msg));
    }
} 