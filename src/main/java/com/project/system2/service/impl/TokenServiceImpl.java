package com.project.system2.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import com.project.system2.common.core.redis.RedisCache;
import com.project.system2.config.SecurityTokenConfig;
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
    private SecurityTokenConfig securityTokenConfig;

    @Autowired
    private HttpServletRequest request;

    protected static final long MILLIS_MINUTE = 60 * 1000L;
    private static final Long MILLIS_MINUTE_TEN = 20 * 60 * 1000L;

    @Override
    public String createToken(LoginUser loginUser) {
        String token = UUID.randomUUID().toString();
        loginUser.setToken(token);
        refreshToken(loginUser);

        Map<String, Object> claims = new HashMap<>();
        claims.put(CacheConstants.LOGIN_USER_KEY, token);
        
        return JwtUtils.generateToken(
            claims, 
            securityTokenConfig.getSecret(),
            securityTokenConfig.getExpireTime()
        );
    }

    @Override
    public LoginUser getLoginUser(HttpServletRequest request) {
        String token = getToken(request);
        if (StringUtils.isNotEmpty(token)) {
            try {
                Claims claims = JwtUtils.parseToken(token, securityTokenConfig.getSecret());
                String uuid = (String) claims.get(CacheConstants.LOGIN_USER_KEY);
                String userKey = getTokenKey(uuid);
                return redisCache.getCacheObject(userKey);
            } catch (Exception e) {
                logger.error("获取用户信息异常", e);
            }
        }
        return null;
    }

    @Override
    public String getToken(HttpServletRequest request) {
        String token = request.getHeader(securityTokenConfig.getHeader());
        if (StringUtils.isNotEmpty(token) && token.startsWith(securityTokenConfig.getPrefix())) {
            token = token.substring(securityTokenConfig.getPrefix().length());
        }
        return token;
    }

    @Override
    public void deleteToken(String token) {
        if (StringUtils.isNotEmpty(token)) {
            String userKey = getTokenKey(token);
            redisCache.deleteObject(userKey);
        }
    }

    @Override
    public void verifyToken(LoginUser loginUser) {
        long expireTime = loginUser.getExpireTime();
        long currentTime = System.currentTimeMillis();
        if (expireTime - currentTime <= MILLIS_MINUTE_TEN) {
            refreshToken(loginUser);
        }
    }

    @Override
    public void refreshToken(LoginUser loginUser) {
        long expireTimeMillis = securityTokenConfig.getExpireTime() * MILLIS_MINUTE;
        loginUser.setExpireTime(System.currentTimeMillis() + expireTimeMillis);
        
        redisCache.setCacheObject(
            getTokenKey(loginUser.getToken()),
            loginUser,
            securityTokenConfig.getExpireTime(),
            TimeUnit.MINUTES
        );
    }

    @Override
    public boolean validateToken(String token) {
        try {
            Claims claims = JwtUtils.parseToken(token, securityTokenConfig.getSecret());
            String uuid = (String) claims.get(CacheConstants.LOGIN_USER_KEY);
            String userKey = getTokenKey(uuid);
            return redisCache.hasKey(userKey);
        } catch (Exception e) {
            logger.error("Token validation failed: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public UsernamePasswordAuthenticationToken getAuthentication(String token) {
        try {
            Claims claims = JwtUtils.parseToken(token, securityTokenConfig.getSecret());
            String uuid = (String) claims.get(CacheConstants.LOGIN_USER_KEY);
            String userKey = getTokenKey(uuid);
            LoginUser loginUser = redisCache.getCacheObject(userKey);
            
            if (loginUser != null) {
                return new UsernamePasswordAuthenticationToken(
                    loginUser,
                    null,
                    loginUser.getAuthorities()
                );
            }
        } catch (Exception e) {
            logger.error("Cannot get authentication: {}", e.getMessage());
        }
        return null;
    }

    @Override
    public LoginUser getLoginUser() {
        return getLoginUser(request);
    }

    private String getTokenKey(String uuid) {
        return CacheConstants.LOGIN_TOKEN_KEY + uuid;
    }
} 