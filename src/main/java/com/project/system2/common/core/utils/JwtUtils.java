package com.project.system2.common.core.utils;

import java.util.Map;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * JWT工具类
 */
public class JwtUtils {
    /**
     * 从数据声明生成令牌
     *
     * @param claims 数据声明
     * @param secret 密钥
     * @return 令牌
     */
    public static String createToken(Map<String, Object> claims, String secret) {
        return Jwts.builder()
                .setClaims(claims)
                .signWith(generalKey(secret), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 从令牌中获取数据声明
     *
     * @param token 令牌
     * @param secret 密钥
     * @return 数据声明
     */
    public static Map<String, Object> parseToken(String token, String secret) {
        return Jwts.parserBuilder()
                .setSigningKey(generalKey(secret))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 生成加密后的秘钥
     *
     * @param secret 密钥
     * @return 加密后的密钥
     */
    private static SecretKey generalKey(String secret) {
        byte[] encodedKey = Base64.decodeBase64(secret);
        return new SecretKeySpec(encodedKey, 0, encodedKey.length, SignatureAlgorithm.HS256.getJcaName());
    }
} 