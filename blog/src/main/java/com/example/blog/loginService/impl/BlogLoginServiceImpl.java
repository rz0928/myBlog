package com.example.blog.loginService.impl;

import com.example.blog.loginService.BlogLoginService;
import com.example.blogframework.entity.LoginUser;
import com.example.blogframework.entity.User;
import com.example.blogframework.model.BlogLoginUserVo;
import com.example.blogframework.model.UserInfoVo;
import com.example.constants.SystemConstants;
import com.example.utils.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;

@Service
public class BlogLoginServiceImpl implements BlogLoginService {

    @Resource
    private AuthenticationManager authenticationManager;
    @Resource
    RedisCache redisCache;
    @Override
    public ResponseResult login(User user) {
        //将user相关信息封装成AuthenticationToken类型，调用authenticate传递给AuthenticationProvider进行具体判断
        UsernamePasswordAuthenticationToken authenticationToken=new
                UsernamePasswordAuthenticationToken(user.getUserName(),user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        if(Objects.isNull(authenticate)){
            throw new RuntimeException("用户名或密码错误");
        }
        //判断认证是否通过
        //根据userid生产JWT
        LoginUser loginUser=(LoginUser) authenticate.getPrincipal();
        String userId=loginUser.getUser().getId().toString();
        String jwt= JwtUtil.createJWT(userId);
        //把用户信息存入redis
        redisCache.setCacheObject(SystemConstants.RECEPTION_TOKEN_KEY +userId,loginUser);
        //返回需要用到的user信息
        UserInfoVo userInfoVo= BeanCopyUtils.copyBean(loginUser.getUser(),UserInfoVo.class);
        //加上token返回
        BlogLoginUserVo blogLoginUserVo = new BlogLoginUserVo(jwt, userInfoVo);
        return ResponseResult.okResult(blogLoginUserVo);
    }

    @Override
    public ResponseResult logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId= SecurityUtils.getUserId();
        redisCache.deleteObject(SystemConstants.RECEPTION_TOKEN_KEY +userId);
        return ResponseResult.okResult();
    }
}
