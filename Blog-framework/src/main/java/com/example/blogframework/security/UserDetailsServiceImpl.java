package com.example.blogframework.security;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.blogframework.entity.LoginUser;
import com.example.blogframework.entity.User;
import com.example.blogframework.mapper.UserMapper;
import com.example.blogframework.service.MenuService;
import com.example.constants.SystemConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * 实现了SpringSecurity中的UserDetailService接口，重写了loadUserByUsername方法
 * 让SpringSecurity从数据库中加载相关信息校验
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Resource
    private UserMapper userMapper;
    @Autowired
    private MenuService menuService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LambdaQueryWrapper<User> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserName,username);
        User user=userMapper.selectOne(queryWrapper);
        if(Objects.isNull(user)){
            throw new RuntimeException("用户不存在");
        }
        if(user.getType().equals(SystemConstants.ADMIN)){
            List<String> permissions = menuService.selectPermissionsByUserId(user.getId());
            return new LoginUser(user,permissions);
        }
        return new LoginUser(user,null);
    }
}
