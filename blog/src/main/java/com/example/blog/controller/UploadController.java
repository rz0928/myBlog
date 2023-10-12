package com.example.blog.controller;

import com.example.blogframework.service.UpLoadService;
import com.example.utils.ResponseResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

@RestController
public class UploadController {
    @Resource
    private UpLoadService upLoadService;
    @PostMapping("/upload")
    public ResponseResult upLoadImg(MultipartFile img){
        return upLoadService.upLoad(img);
    }
}
