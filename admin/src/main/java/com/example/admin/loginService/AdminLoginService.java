package com.example.admin.loginService;

import com.example.blogframework.entity.User;
import com.example.utils.ResponseResult;

public interface AdminLoginService {
    ResponseResult login(User user);

    ResponseResult logout();
}
