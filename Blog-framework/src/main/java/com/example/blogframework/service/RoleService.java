package com.example.blogframework.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.blogframework.dto.AddRoleDto;
import com.example.blogframework.dto.UpdateRoleDto;
import com.example.blogframework.dto.UpdateRoleStatusDto;
import com.example.blogframework.entity.Role;
import com.example.utils.ResponseResult;

import java.util.List;


/**
 * 角色信息表(Role)表服务接口
 *
 * @author makejava
 * @since 2023-09-17 18:19:17
 */
public interface RoleService extends IService<Role> {

    List<String> selectRoleKeyByUserId(Long id);

    ResponseResult listAllRole(Integer pageNum, Integer pageSize, String roleName, String status);

    ResponseResult updateRoleStatus(UpdateRoleStatusDto updateRoleStatusDto);

    ResponseResult addRole(AddRoleDto addRoleDto);

    ResponseResult deleteRoleById(Long id);

    ResponseResult getRoleById(Long id);

    ResponseResult updateRoleById(UpdateRoleDto updateRoleDto);

    ResponseResult listAllUsefulRole();
}

