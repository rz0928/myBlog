package com.example.blogframework.service;

import com.example.utils.ResponseResult;
import org.springframework.web.multipart.MultipartFile;

public interface UpLoadService {

    ResponseResult upLoad(MultipartFile img);
}
