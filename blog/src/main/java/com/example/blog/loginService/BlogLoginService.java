package com.example.blog.loginService;

import com.example.blogframework.entity.User;
import com.example.utils.ResponseResult;

public interface BlogLoginService {
    ResponseResult login(User user);

    ResponseResult logout();
}
