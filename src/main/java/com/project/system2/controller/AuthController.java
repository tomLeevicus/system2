package com.project.system2.controller;

import com.project.system2.common.core.domain.R;
import com.project.system2.common.core.redis.RedisCache;
import com.project.system2.common.core.utils.Base64;
import com.project.system2.common.core.utils.CaptchaUtil;
import com.project.system2.common.core.utils.CacheConstants;
import com.project.system2.domain.model.LoginBody;
import com.project.system2.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/auth")
public class AuthController {
    
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    
    @Autowired
    private AuthService authService;
    
    @Autowired
    private RedisCache redisCache;

    @PostMapping("/login")
    public R<Map<String, Object>> login(@RequestBody LoginBody loginBody) {
        String token = authService.login(loginBody.getUsername(), loginBody.getPassword());
        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        return R.ok(data);
    }

    @GetMapping("/info")
    public R<Map<String, Object>> getInfo() {
        Map<String, Object> data = authService.getInfo();
        return R.ok(data);
    }

    @PostMapping("/logout")
    public R<Void> logout() {
        authService.logout();
        return R.ok();
    }

    @GetMapping("/captcha")
    public R<Map<String, String>> getCaptcha() {
        log.info("收到验证码请求");
        // 生成验证码
        String uuid = UUID.randomUUID().toString();
        String code = CaptchaUtil.generateCode();
        
        // 生成图片
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        CaptchaUtil.generateImage(code, stream);
        
        // 保存验证码
        redisCache.setCacheObject(CacheConstants.CAPTCHA_CODE_KEY + uuid, code, 2, TimeUnit.MINUTES);
        
        // 转换为Base64
        String base64 = Base64.encode(stream.toByteArray());
        
        Map<String, String> data = new HashMap<>();
        data.put("uuid", uuid);
        data.put("img", base64);
        
        return R.ok(data);
    }
} 