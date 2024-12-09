package com.project.system2.common.core.utils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;

public class CaptchaUtil {
    
    private static final Random RANDOM = new Random();
    
    private static final String CODES = "23456789ABCDEFGHJKLMNPQRSTUVWXYZ";
    
    private static final int WIDTH = 100;
    private static final int HEIGHT = 40;
    
    /**
     * 生成验证码
     */
    public static String generateCode() {
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            code.append(CODES.charAt(RANDOM.nextInt(CODES.length())));
        }
        return code.toString();
    }
    
    /**
     * 生成验证码图片
     */
    public static void generateImage(String code, ByteArrayOutputStream os) {
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = image.createGraphics();
        
        // 设置背景色
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, WIDTH, HEIGHT);
        
        // 绘制干扰线
        g2.setColor(getRandColor(160, 200));
        for (int i = 0; i < 20; i++) {
            int x1 = RANDOM.nextInt(WIDTH);
            int y1 = RANDOM.nextInt(HEIGHT);
            int x2 = RANDOM.nextInt(12);
            int y2 = RANDOM.nextInt(12);
            g2.drawLine(x1, y1, x1 + x2, y1 + y2);
        }
        
        // 绘制验证码
        g2.setFont(new Font("Arial", Font.BOLD, 24));
        for (int i = 0; i < code.length(); i++) {
            g2.setColor(getRandColor(20, 130));
            g2.drawString(String.valueOf(code.charAt(i)), 
                15 + i * 20 + RANDOM.nextInt(8), 
                25 + RANDOM.nextInt(8));
        }
        
        g2.dispose();
        
        try {
            ImageIO.write(image, "JPEG", os);
        } catch (IOException e) {
            throw new RuntimeException("验证码图片生成失败", e);
        }
    }
    
    /**
     * 获取随机颜色
     */
    private static Color getRandColor(int fc, int bc) {
        int r = fc + RANDOM.nextInt(bc - fc);
        int g = fc + RANDOM.nextInt(bc - fc);
        int b = fc + RANDOM.nextInt(bc - fc);
        return new Color(r, g, b);
    }
} 