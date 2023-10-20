package com.example.blog.controller;

import com.example.exception.SystemException;
import com.example.blogframework.entity.User;
import com.example.blog.loginService.BlogLoginService;
import com.example.utils.ResponseResult;
import com.example.enums.AppHttpCodeEnum;
import com.example.constants.SystemConstants;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class BlogLoginController {
    @Resource
    private BlogLoginService blogLoginService;

    @PostMapping("/login")
    public ResponseResult login(@RequestBody User user) {
        if (!StringUtils.hasText(user.getUserName())) {
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        return blogLoginService.login(user);
    }

    @PostMapping("/user/logout")
    public ResponseResult logout() {
        return blogLoginService.logout();
    }
}
