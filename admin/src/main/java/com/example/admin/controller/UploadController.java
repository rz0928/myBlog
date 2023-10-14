package com.example.admin.controller;

import com.example.blogframework.service.UpLoadService;
import com.example.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class UploadController {
    @Autowired
    UpLoadService upLoadService;
    @PostMapping("/upload")
    public ResponseResult uploadImg(MultipartFile img) {
            return upLoadService.upLoad(img);
    }
}
