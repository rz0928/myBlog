package com.example.utils;

import com.example.blogframework.entity.LoginUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 从SecurityContextHolder获取用户相关信息
 */
public class SecurityUtils {
    public static Authentication getAuthentication(){
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * 获取登录用户
     * @return
     */
    public static LoginUser getLoginUser(){
        return (LoginUser) getAuthentication().getPrincipal();
    }
    public static Long getUserId(){
        return getLoginUser().getUser().getId();
    }
    public static boolean isAdmin(){
        Long id=getUserId();
        return id!=null&&id==1L;
    }
}
