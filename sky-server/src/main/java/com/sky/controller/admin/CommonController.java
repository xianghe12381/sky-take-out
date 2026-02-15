package com.sky.controller.admin;

import com.sky.constant.MessageConstant;
import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * 通用接口 (本地存储版)
 */
@RestController
@RequestMapping("/admin/common")
@Api(tags = "通用接口")
@Slf4j
public class CommonController {

    // 本地存储路径 (最后必须带斜杠)
    private static final String LOCAL_FILE_DIR = "C:/Users/KHYE/Desktop/images/";

    // Nginx 访问地址 (你的 Nginx 端口是 81)
    private static final String NGINX_SERVER_URL = "http://localhost:80/images/";

    /**
     * 文件上传
     */
    @PostMapping("/upload")
    @ApiOperation("文件上传")
    public Result<String> upload(MultipartFile file) {
        log.info("文件上传：{}", file);

        try {
            // 1. 获取原始文件名
            String originalFilename = file.getOriginalFilename();
            // 2. 截取后缀
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            // 3. 构造新文件名
            String newFileName = UUID.randomUUID().toString() + extension;

            // 4. 创建目标文件对象
            File targetFile = new File(LOCAL_FILE_DIR + newFileName);

            // 如果目录不存在，创建它
            if (!targetFile.getParentFile().exists()) {
                targetFile.getParentFile().mkdirs();
            }

            // 5. 核心动作：把上传的临时文件“转移”到 D:/images/ 下
            file.transferTo(targetFile);

            // 6. 返回给前端的访问路径
            // 例如：http://localhost:81/images/abc-123.jpg
            String fileUrl = NGINX_SERVER_URL + newFileName;
            log.info("文件已保存至本地: {}, 访问URL: {}", targetFile.getAbsolutePath(), fileUrl);

            return Result.success(fileUrl);

        } catch (IOException e) {
            log.error("文件上传失败：{}", e);
        }

        return Result.error(MessageConstant.UPLOAD_FAILED);
    }
}