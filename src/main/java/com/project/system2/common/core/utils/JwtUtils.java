package com.project.system2.common.core.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT工具类
 */
@Component
public class JwtUtils {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expireTime}")
    private long expireTime;

    private static final SignatureAlgorithm ALGORITHM = SignatureAlgorithm.HS256;

    /**
     * 生成jwt token
     */
    public static String generateToken(Map<String, Object> claims, String secret) {
        // 使用Keys工具类生成安全的密钥
        Key key = Keys.hmacShaKeyFor(secret.getBytes());
        
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .signWith(key, ALGORITHM)
                .compact();
    }

    /**
     * 解析token
     */
    public static Claims parseToken(String token, String secret) {
        // 使用相同的方式生成密钥
        Key key = Keys.hmacShaKeyFor(secret.getBytes());
        
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 从数据声明生成令牌
     *
     * @param claims 数据声明
     * @return 令牌
     */
    private String createToken(Map<String, Object> claims) {
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 从令牌中获取数据声明
     *
     * @param token 令牌
     * @return 数据声明
     */
    private Claims parseToken(String token) {
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 生成令牌
     *
     * @param userKey 用户标识
     * @return 令牌
     */
    public String createToken(String userKey) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("login_user_key", userKey);
        claims.put("iat", new Date().getTime());
        return createToken(claims);
    }

    /**
     * 从令牌中获取用户标识
     *
     * @param token 令牌
     * @return 用户标识
     */
    public String getUserKey(String token) {
        Claims claims = parseToken(token);
        return claims.get("login_user_key", String.class);
    }

    /**
     * 验证令牌
     *
     * @param token 令牌
     * @return 是否有效
     */
    public boolean validateToken(String token) {
        try {
            // 解析token，如果解析失败会抛出异常
            Claims claims = parseToken(token);
            
            // 获取token签发时间
            long issuedAt = claims.get("iat", Long.class);
            
            // 检查token是否过期
            long currentTime = System.currentTimeMillis();
            if (currentTime - issuedAt > expireTime * 60 * 1000) {
                return false;
            }
            
            // 验证用户标识是否存在
            String userKey = claims.get("login_user_key", String.class);
            return userKey != null && !userKey.isEmpty();
            
        } catch (ExpiredJwtException e) {
            // token过期
            return false;
        } catch (Exception e) {
            // 其他验证失败情况
            return false;
        }
    }

    /**
     * 刷新令牌
     *
     * @param token 原令牌
     * @return 新令牌
     */
    public String refreshToken(String token) {
        Claims claims = parseToken(token);
        claims.put("iat", new Date().getTime());
        return createToken(claims);
    }
} 