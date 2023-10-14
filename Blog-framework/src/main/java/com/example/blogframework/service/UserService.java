package com.example.blogframework.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.blogframework.dto.AddUserDto;
import com.example.blogframework.dto.UpdateUserDto;
import com.example.blogframework.entity.User;
import com.example.utils.ResponseResult;

/**
 * 用户表(SysUser)表服务接口
 *
 * @author makejava
 * @since 2023-08-29 19:21:37
 */
public interface UserService extends IService<User> {

    ResponseResult userInfo();

    ResponseResult updateUserInfo(User user);

    ResponseResult register(User user);

    ResponseResult listAllUser(Integer pageNum, Integer pageSize, String userName, String phonenumber, String status);

    ResponseResult addUser(AddUserDto addUserDto);

    ResponseResult deleteUserById(Long id);

    ResponseResult getUserRoleById(Long id);

    ResponseResult updateUser(UpdateUserDto updateUserDto);
}

