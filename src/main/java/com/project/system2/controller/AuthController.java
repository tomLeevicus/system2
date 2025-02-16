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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/auth")
@Tag(name = "认证管理", description = "用户登录认证相关接口")
public class AuthController {
    
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    
    @Autowired
    private AuthService authService;
    
    @Autowired
    private RedisCache redisCache;

    @PostMapping("/login")
    @Operation(summary = "用户登录", description = "使用用户名密码获取访问令牌")
    @Parameter(name = "username", description = "用户名", example = "admin", required = true)
    @Parameter(name = "password", description = "密码", example = "admin123", required = true)
    @ApiResponse(responseCode = "200", description = "登录成功返回JWT令牌")
    public R<Map<String, Object>> login(@RequestBody LoginBody loginBody) {
        String token = authService.login(loginBody.getUsername(), loginBody.getPassword(),loginBody.getCode(),loginBody.getUuid());
        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        return R.ok(data);
    }

    @GetMapping("/info")
    @Operation(summary = "获取用户信息", description = "获取当前登录用户的基本信息和权限")
    @ApiResponse(responseCode = "200", description = "成功获取用户信息")
    public R<Map<String, Object>> getInfo() {
        Map<String, Object> data = authService.getInfo();
        return R.ok(data);
    }

    @PostMapping("/logout")
    @Operation(summary = "用户登出", description = "使当前用户的令牌失效")
    @ApiResponse(responseCode = "200", description = "成功登出")
    public R<Void> logout() {
        authService.logout();
        return R.ok();
    }

    @GetMapping("/captcha")
    @Operation(summary = "获取验证码", description = "生成图形验证码用于登录")
    @ApiResponse(responseCode = "200", description = "返回Base64编码的验证码图片和UUID")
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