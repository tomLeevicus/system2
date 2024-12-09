package com.project.system2.common.core.utils;

/**
 * Base64工具类
 */
public class Base64 {
    
    private static final java.util.Base64.Encoder encoder = java.util.Base64.getEncoder();
    private static final java.util.Base64.Decoder decoder = java.util.Base64.getDecoder();

    /**
     * 获取Base64编码器
     */
    public static java.util.Base64.Encoder getEncoder() {
        return encoder;
    }

    /**
     * 获取Base64解码器
     */
    public static java.util.Base64.Decoder getDecoder() {
        return decoder;
    }

    /**
     * Base64编码
     */
    public static String encode(byte[] bytes) {
        return encoder.encodeToString(bytes);
    }

    /**
     * Base64解码
     */
    public static byte[] decode(String str) {
        return decoder.decode(str);
    }
} 