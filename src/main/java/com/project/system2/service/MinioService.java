package com.project.system2.service;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

/**
 * MinIO 文件存储服务接口
 */
public interface MinioService {
    
    /**
     * 上传文件
     * @param file 文件
     * @param folder 文件夹
     * @return 文件访问路径
     */
    String uploadFile(MultipartFile file, String folder) throws Exception;
    
    /**
     * 下载文件
     * @param fileName 文件名称
     * @return 文件流
     */
    InputStream downloadFile(String fileName) throws Exception;
}

