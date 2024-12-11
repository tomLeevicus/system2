package com.project.system2.common.core.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;
import java.util.Map;

/**
 * JWT工具类
 */
public class JwtUtils {
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
} 