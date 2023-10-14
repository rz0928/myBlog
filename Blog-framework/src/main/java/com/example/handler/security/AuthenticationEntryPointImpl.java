package com.example.handler.security;

import com.alibaba.fastjson.JSON;
import com.example.utils.ResponseResult;
import com.example.utils.WebUtils;
import com.example.enums.AppHttpCodeEnum;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 认证失败处理类
 */
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException authenticationException) throws IOException, ServletException {
        authenticationException.printStackTrace();
        ResponseResult result=null;
        if(authenticationException instanceof BadCredentialsException) {
             result = ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_ERROR.getCode(),authenticationException.getMessage());
        }else if(authenticationException instanceof InsufficientAuthenticationException){
             result = ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
        }else{
            result =ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR.getCode(),"认证或授权失败");
        }
        WebUtils.renderString(httpServletResponse, JSON.toJSONString(result));
    }
}
