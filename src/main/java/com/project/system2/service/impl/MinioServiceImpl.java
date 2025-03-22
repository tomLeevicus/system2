package com.project.system2.service.impl;

import com.project.system2.service.MinioService;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

/**
 * MinIO 文件存储服务实现类
 */
@Service
public class MinioServiceImpl implements MinioService {

    @Autowired
    private MinioClient minioClient;

    @Value("${minio.bucketName}")
    private String bucketName;

    @Value("${minio.endpoint}")
    private String endpoint;

    @Override
    public String uploadFile(MultipartFile file, String folder) throws Exception {
        // 生成文件名
        String originalFilename = file.getOriginalFilename();
        String fileName = folder + "/" + UUID.randomUUID().toString().replaceAll("-", "") 
                + getFileExtension(originalFilename);
        
        // 上传文件
        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(fileName)
                        .stream(file.getInputStream(), file.getSize(), -1)
                        .contentType(file.getContentType())
                        .build());
        
        // 返回文件访问路径
        return endpoint + "/" + bucketName + "/" + fileName;
    }

    @Override
    public InputStream downloadFile(String fileName) throws Exception {
        return minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(bucketName)
                        .object(fileName)
                        .build());
    }

    /**
     * 获取文件扩展名
     * @param filename 文件名
     * @return 扩展名
     */
    private String getFileExtension(String filename) {
        if (filename == null) return "";
        int lastIndexOf = filename.lastIndexOf(".");
        if (lastIndexOf == -1) return "";
        return filename.substring(lastIndexOf);
    }
} 