package com.example.admin.controller;

import com.example.blogframework.dto.AddUserDto;
import com.example.blogframework.dto.ChangeUserStatus;
import com.example.blogframework.dto.UpdateUserDto;
import com.example.blogframework.service.UserService;
import com.example.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system/user")
public class UserController {
    @Autowired
    UserService userService;
    @GetMapping("/list")
    public ResponseResult listAllUser(Integer pageNum,Integer pageSize,String userName,String phonenumber,String status){
        return userService.listAllUser(pageNum,pageSize,userName,phonenumber,status);
    }
    @PostMapping()
    public ResponseResult addUser(@RequestBody AddUserDto addUserDto){
        return userService.addUser(addUserDto);
    }
    @DeleteMapping("/{id}")
    public ResponseResult deleteUserById(@PathVariable Long id){
        return userService.deleteUserById(id);
    }
    @GetMapping("/{id}")
    public ResponseResult getUserRoleById(@PathVariable Long id){
        return userService.getUserRoleById(id);
    }
    @PutMapping()
    public ResponseResult updateUser(@RequestBody UpdateUserDto updateUserDto){
        return userService.updateUser(updateUserDto);
    }
    @PutMapping("/changeStatus")
    public ResponseResult changeUserStatus(@RequestBody ChangeUserStatus changeUserStatus){
        return userService.changeStatus(changeUserStatus);
    }

}
