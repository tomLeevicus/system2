package com.project.system2.service;

import com.project.system2.common.core.domain.model.LoginUser;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public interface TokenService {
    /**
     * 创建令牌
     */
    String createToken(LoginUser loginUser);

    /**
     * 获取用户身份信息
     */
    LoginUser getLoginUser(HttpServletRequest request);

    /**
     * 获取当前用户身份信息
     */
    LoginUser getLoginUser();

    /**
     * 获取请求token
     */
    String getToken(HttpServletRequest request);

    /**
     * 删除用户身份信息
     */
    void deleteToken(String token);

    /**
     * 验证令牌有效期,相差不足20分钟,自动刷新缓存
     */
    void verifyToken(LoginUser loginUser);

    /**
     * 刷新令牌有效期
     */
    void refreshToken(LoginUser loginUser);

    /**
     * 验证令牌有效性
     * 
     * @param token 令牌
     * @return 是否有效
     */
    boolean validateToken(String token);

    /**
     * 获取认证信息
     * 
     * @param token 令牌
     * @return 认证信息
     */
    UsernamePasswordAuthenticationToken getAuthentication(String token);
} 