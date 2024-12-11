package com.project.system2.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import com.project.system2.common.core.redis.RedisCache;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import com.project.system2.common.constant.CacheConstants;
import com.project.system2.common.constant.TokenConstants;
import com.project.system2.common.core.domain.model.LoginUser;
import com.project.system2.common.core.utils.JwtUtils;
import com.project.system2.common.core.utils.StringUtils;
import com.project.system2.config.TokenConfig;
import com.project.system2.service.TokenService;

import io.jsonwebtoken.Claims;

@Service
public class TokenServiceImpl implements TokenService {
    private static final Logger logger = LoggerFactory.getLogger(TokenServiceImpl.class);

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private TokenConfig tokenConfig;

    protected static final long MILLIS_SECOND = 1000;
    protected static final long MILLIS_MINUTE = 60 * MILLIS_SECOND;
    private static final Long MILLIS_MINUTE_TEN = 20 * 60 * 1000L;

    /**
     * 创建令牌
     */
    @Override
    public String createToken(LoginUser loginUser) {
        String token = UUID.randomUUID().toString();
        loginUser.setToken(token);
        refreshToken(loginUser);

        Map<String, Object> claims = new HashMap<>();
        claims.put(CacheConstants.LOGIN_USER_KEY, token);
        
        return JwtUtils.generateToken(claims, tokenConfig.getSecret());
    }

    /**
     * 获取用户身份信息
     */
    @Override
    public LoginUser getLoginUser(HttpServletRequest request) {
        // 获取请求携带的令牌
        String token = getToken(request);
        if (StringUtils.isNotEmpty(token)) {
            try {
                Claims claims = JwtUtils.parseToken(token, tokenConfig.getSecret());
                String uuid = (String) claims.get(CacheConstants.LOGIN_USER_KEY);
                String userKey = getTokenKey(uuid);
                return redisCache.getCacheObject(userKey);
            } catch (Exception e) {
                logger.error("获取用户信息异常", e);
            }
        }
        return null;
    }

    /**
     * 获取请求token
     */
    @Override
    public String getToken(HttpServletRequest request) {
        String token = request.getHeader(tokenConfig.getHeader());
        if (StringUtils.isNotEmpty(token) && token.startsWith(tokenConfig.getTokenPrefix())) {
            token = token.substring(tokenConfig.getTokenPrefix().length());
        }
        return token;
    }

    /**
     * 删除用户身份信息
     */
    @Override
    public void deleteToken(String token) {
        if (StringUtils.isNotEmpty(token)) {
            String userKey = getTokenKey(token);
            redisCache.deleteObject(userKey);
        }
    }

    /**
     * 验证令牌有效期,相差不足20分钟,自动刷新缓存
     */
    @Override
    public void verifyToken(LoginUser loginUser) {
        long expireTime = loginUser.getExpireTime();
        long currentTime = System.currentTimeMillis();
        if (expireTime - currentTime <= MILLIS_MINUTE_TEN) {
            refreshToken(loginUser);
        }
    }

    /**
     * 刷新令牌有效期
     */
    @Override
    public void refreshToken(LoginUser loginUser) {
        loginUser.setLoginTime(System.currentTimeMillis());
        loginUser.setExpireTime(loginUser.getLoginTime() + tokenConfig.getExpireTime() * MILLIS_MINUTE);
        // 根据uuid将loginUser缓存
        String userKey = getTokenKey(loginUser.getToken());
        redisCache.setCacheObject(userKey, loginUser, tokenConfig.getExpireTime(), TimeUnit.MINUTES);
    }

    /**
     * 验证令牌有效性
     */
    @Override
    public boolean validateToken(String token) {
        try {
            Claims claims = JwtUtils.parseToken(token, tokenConfig.getSecret());
            String uuid = (String) claims.get(CacheConstants.LOGIN_USER_KEY);
            String userKey = getTokenKey(uuid);
            return redisCache.hasKey(userKey);
        } catch (Exception e) {
            logger.error("Token validation failed: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 获取认证信息
     */
    @Override
    public UsernamePasswordAuthenticationToken getAuthentication(String token) {
        Claims claims = JwtUtils.parseToken(token, tokenConfig.getSecret());
        String uuid = (String) claims.get(CacheConstants.LOGIN_USER_KEY);
        String userKey = getTokenKey(uuid);
        LoginUser user = redisCache.getCacheObject(userKey);
        if (user != null) {
            return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        }
        return null;
    }

    private String getTokenKey(String uuid) {
        return CacheConstants.LOGIN_TOKEN_KEY + uuid;
    }
} 