package com.example.admin.loginService.impl;

import com.example.admin.loginService.AdminLoginService;
import com.example.blogframework.entity.LoginUser;
import com.example.blogframework.entity.User;
import com.example.constants.SystemConstants;
import com.example.utils.JwtUtil;
import com.example.utils.RedisCache;
import com.example.utils.ResponseResult;
import com.example.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service("adminLoginService")
public class AdminLoginServiceImpl implements AdminLoginService {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    RedisCache redisCache;

    @Override
    public ResponseResult login(User user) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        if (Objects.isNull(authentication)) {
            throw new RuntimeException("用户名或密码错误");
        }
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        String userId = loginUser.getUser().getId().toString();
        String jwt = JwtUtil.createJWT(userId);
        redisCache.setCacheObject(SystemConstants.BACKSTAGE_TOKEN_KEY + userId, loginUser);
        Map<String,String> map = new HashMap<>();
        map.put("token",jwt);
        return ResponseResult.okResult(map);
    }

    @Override
    public ResponseResult logout() {
        Long userId = SecurityUtils.getUserId();
        redisCache.deleteObject(SystemConstants.BACKSTAGE_TOKEN_KEY + userId);
        return ResponseResult.okResult();
    }
}
