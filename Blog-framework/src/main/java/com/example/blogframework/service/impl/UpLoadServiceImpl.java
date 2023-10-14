package com.example.blogframework.service.impl;

import com.example.enums.AppHttpCodeEnum;
import com.example.exception.SystemException;
import com.example.blogframework.service.UpLoadService;
import com.example.utils.PathUtils;
import com.example.utils.ResponseResult;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

/**
 * 与对象存储服务器交互的类
 */
@ConfigurationProperties(prefix = "oss")
@Service("upLoadService")
public class UpLoadServiceImpl implements UpLoadService {
    private String secretKey;
    private String accessKey;
    private String bucket;

    @Override
    public ResponseResult upLoad(MultipartFile img) {
        String imgOriginalFilename = img.getOriginalFilename();
        if (!StringUtils.hasText(imgOriginalFilename)) {
            throw new SystemException(AppHttpCodeEnum.NEED_FILE);
        }
        //判断文件格式是否是/png/jpg/jpeg
        if (!imgOriginalFilename.endsWith(".png") && !imgOriginalFilename.endsWith(".jpg") && !imgOriginalFilename.endsWith(".jpeg")) {
            throw new SystemException(AppHttpCodeEnum.FILE_TYPE_ERROR);
        }
        //生成图片的存储位置
        String key = PathUtils.generateFilePath(imgOriginalFilename);
        //上传图片，返回访问图片的URL
        String url = upLoadOss(img, key);
        return ResponseResult.okResult(url);
    }

    public String upLoadOss(MultipartFile img, String key) {
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.autoRegion());
        // 指定分片上传版本
        cfg.resumableUploadAPIVersion = Configuration.ResumableUploadAPIVersion.V2;
        //生成上传凭证，然后准备上传
        UploadManager uploadManager = new UploadManager(cfg);

        try {
            InputStream inputStream = img.getInputStream();
            Auth auth = Auth.create(accessKey, secretKey);
            String upToken = auth.uploadToken(bucket);

            try {
                Response response = uploadManager.put(inputStream, key, upToken, null, null);
                //解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                System.out.println(putRet.key);
                System.out.println(putRet.hash);
                return "http://s0gll645c.bkt.clouddn.com/" + key;
            } catch (QiniuException ex) {
                ex.printStackTrace();
                if (ex.response != null) {
                    System.err.println(ex.response);

                    try {
                        String body = ex.response.toString();
                        System.err.println(body);
                    } catch (Exception ignored) {
                    }
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new RuntimeException("文件上传失败");
        }
        return "error";
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }
}
