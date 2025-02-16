package com.project.system2.common.core.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.nio.charset.StandardCharsets;

/**
 * JWT工具类
 */
@Component
public class JwtUtils {
    /**
     * 生成jwt token
     */
    public static String generateToken(Map<String, Object> claims, String secret, int expireMinutes) {
        return Jwts.builder()
            .setClaims(claims)
            .setExpiration(new Date(System.currentTimeMillis() + expireMinutes * 60 * 1000))
            .signWith(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)))
            .compact();
    }

    /**
     * 解析token
     */
    public static Claims parseToken(String token, String secret) {
        return Jwts.parserBuilder()
            .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)))
            .build()
            .parseClaimsJws(token)
            .getBody();
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
            Claims claims = parseToken(token, "");
            
            // 获取token签发时间
            long issuedAt = claims.get("iat", Long.class);
            
            // 检查token是否过期
            long currentTime = System.currentTimeMillis();
            if (currentTime - issuedAt > 60 * 1000) {
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
        Claims claims = parseToken(token, "");
        claims.put("iat", new Date().getTime());
        return generateToken(claims, "", 1);
    }
} 