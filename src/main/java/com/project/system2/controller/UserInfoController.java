package com.project.system2.controller;

import com.project.system2.common.core.domain.Result;
import com.project.system2.common.core.utils.SecurityUtils;
import com.project.system2.domain.entity.SysUser;
import com.project.system2.service.ISysUserService;
import com.project.system2.service.MinioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletResponse;
import java.io.InputStream;

@RestController
@RequestMapping("/system/userInfo")
@Tag(name = "用户管理", description = "系统用户管理接口")
public class UserInfoController {

    @Autowired
    private ISysUserService userService;
    
    @Autowired
    private MinioService minioService;
    
    @Value("${minio.bucketName}")
    private String bucketName;

    /**
     * 获取当前登录用户的信息
     */
    @GetMapping(value = "/getInfo")
    @Operation(summary = "获取用户详情", description = "根据用户ID获取详细信息")
    @Parameter(name = "userId", description = "用户ID", example = "1", required = true)
    public Result<SysUser> getInfo() {
        Long userId = SecurityUtils.getUserId();
        if (userId != null) {
            return Result.success(userService.getUserById(userId));
        }
        return Result.error();
    }
    
    /**
     * 上传用户头像
     */
    @PostMapping("/uploadAvatar")
    @Operation(summary = "上传用户头像", description = "上传用户头像到MinIO存储")
    public Result<String> uploadAvatar(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return Result.error("上传文件不能为空");
            }
            
            // 检查文件类型
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                return Result.error("只能上传图片文件");
            }
            
            Long userId = SecurityUtils.getUserId();
            String folder = "avatar";
            String avatarUrl = minioService.uploadFile(file, folder);
            
            // 更新用户头像地址
            SysUser user = userService.getUserById(userId);
            user.setAvatar(avatarUrl);
            userService.updateUser(user);
            
            return Result.success("头像上传成功", avatarUrl);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("头像上传失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取用户头像
     */
    @GetMapping("/avatar")
    @Operation(summary = "获取用户头像", description = "获取当前用户的头像")
    public void getAvatar(HttpServletResponse response) {
        try {
            Long userId = SecurityUtils.getUserId();
            SysUser user = userService.getUserById(userId);
            
            if (user.getAvatar() == null || user.getAvatar().isEmpty()) {
                response.sendRedirect("/static/images/default-avatar.png");
                return;
            }
            
            // 从MinIO获取文件路径
            String filePath = user.getAvatar().replace(bucketName + "/", "");
            
            // 设置响应类型
            response.setContentType(MediaType.IMAGE_JPEG_VALUE);
            
            // 获取并输出文件内容
            try (InputStream inputStream = minioService.downloadFile(filePath)) {
                StreamUtils.copy(inputStream, response.getOutputStream());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 上传文件
     */
    @PostMapping("/upload")
    @Operation(summary = "上传文件", description = "上传文件到MinIO存储")
    public Result<String> uploadFile(@RequestParam("file") MultipartFile file, 
                                     @RequestParam(value = "folder", defaultValue = "files") String folder) {
        try {
            if (file.isEmpty()) {
                return Result.error("上传文件不能为空");
            }
            
            String fileUrl = minioService.uploadFile(file, folder);
            return Result.success("文件上传成功", fileUrl);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("文件上传失败: " + e.getMessage());
        }
    }
}
