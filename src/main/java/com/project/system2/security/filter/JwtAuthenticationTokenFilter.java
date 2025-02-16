package com.project.system2.security.filter;

import com.project.system2.common.core.domain.model.LoginUser;
import com.project.system2.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;

import java.io.IOException;

@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationTokenFilter.class);

    @Autowired
    private TokenService tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        
        // 排除Swagger相关路径
        if (requestURI.contains("swagger") || requestURI.contains("api-docs")) {
            chain.doFilter(request, response);
            return;
        }
        
        String token = tokenService.getToken(request);
        if (StringUtils.isNotEmpty(token) && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                if (tokenService.validateToken(token)) {
                    UsernamePasswordAuthenticationToken authentication = tokenService.getAuthentication(token);
                    if (authentication != null) {
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }
            } catch (ExpiredJwtException e) {
                SecurityContextHolder.clearContext();
                throw new AuthenticationServiceException("Token过期", e);
            } catch (JwtException | IllegalArgumentException e) {
                SecurityContextHolder.clearContext();
                throw new BadCredentialsException("无效Token", e);
            }
        }
        chain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return path.startsWith("/auth/");
    }
} 