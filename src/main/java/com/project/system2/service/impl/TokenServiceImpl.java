package com.project.system2.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.project.system2.common.constant.CacheConstants;
import com.project.system2.common.core.domain.model.LoginUser;
import com.project.system2.common.core.utils.JwtUtils;
import com.project.system2.common.core.utils.StringUtils;
import com.project.system2.config.JwtConfig;
import com.project.system2.service.TokenService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.security.Key;

@Service
public class TokenServiceImpl implements TokenService {
    @Value("${token.header}")
    private String header;

    @Value("${token.secret}")
    private String secret;

    @Value("${token.expireTime}")
    private int expireTime;

    protected static final long MILLIS_SECOND = 1000;
    protected static final long MILLIS_MINUTE = 60 * MILLIS_SECOND;
    private static final Long MILLIS_MINUTE_TEN = 20 * 60 * 1000L;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private JwtConfig jwtConfig;

    @Autowired
    private Key key;

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
        
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(loginUser.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtConfig.getExpireTime() * 60 * 1000))
                .signWith(key)
                .compact();
    }

    /**
     * 获取用户身份信息
     */
    @Override
    public LoginUser getLoginUser(HttpServletRequest request) {
        // 获取请求携带的令牌
        String token = getToken(request);
        if (StringUtils.isNotEmpty(token)) {
            String userKey = getTokenKey(token);
            return (LoginUser) redisTemplate.opsForValue().get(userKey);
        }
        return null;
    }

    /**
     * 获取请求token
     */
    @Override
    public String getToken(HttpServletRequest request) {
        String token = request.getHeader(header);
        if (StringUtils.isNotEmpty(token) && token.startsWith(TokenConstants.TOKEN_PREFIX)) {
            token = token.substring(TokenConstants.TOKEN_PREFIX.length());
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
            redisTemplate.delete(userKey);
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
        loginUser.setExpireTime(loginUser.getLoginTime() + expireTime * MILLIS_MINUTE);
        // 根据uuid将loginUser缓存
        String userKey = getTokenKey(loginUser.getToken());
        redisTemplate.opsForValue().set(userKey, loginUser, expireTime, TimeUnit.MINUTES);
    }

    /**
     * 验证令牌有效性
     */
    @Override
    public boolean validateToken(String token) {
        if (StringUtils.isEmpty(token)) {
            return false;
        }
        try {
            Map<String, Object> claims = JwtUtils.parseToken(token, secret);
            String uuid = (String) claims.get(Constants.LOGIN_USER_KEY);
            String userKey = getTokenKey(uuid);
            LoginUser user = redisCache.getCacheObject(userKey);
            return user != null;
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
        // 解析JWT令牌
        Map<String, Object> claims = JwtUtils.parseToken(token, secret);
        String userKey = (String) claims.get(CacheConstants.LOGIN_USER_KEY);
        // 获取用户信息
        LoginUser user = (LoginUser) redisTemplate.opsForValue().get(getTokenKey(userKey));
        if (user != null) {
            return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        }
        return null;
    }

    private String getTokenKey(String uuid) {
        return CacheConstants.LOGIN_TOKEN_KEY + uuid;
    }
} 