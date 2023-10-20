package com.example.admin.controller;

import com.example.blogframework.dto.UpdateRoleDto;
import com.example.blogframework.dto.UpdateRoleStatusDto;
import com.example.blogframework.dto.AddRoleDto;
import com.example.blogframework.service.RoleService;
import com.example.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system/role")
public class RoleController {
    @Autowired
    RoleService roleService;
    @GetMapping("/list")
    public ResponseResult listAllRole(Integer pageNum,Integer pageSize,String roleName,String status){
        return roleService.listAllRole(pageNum,pageSize,roleName,status);
    }
    @PutMapping("/changeStatus")
    public ResponseResult updateRoleStatus(@RequestBody UpdateRoleStatusDto updateRoleStatusDto){
        return roleService.updateRoleStatus(updateRoleStatusDto);
    }
    @PostMapping()
    public ResponseResult addRole(@RequestBody AddRoleDto addRoleDto){
        return roleService.addRole(addRoleDto);
    }
    @DeleteMapping("/{id}")
    public ResponseResult deleteRoleById(@PathVariable Long id){
        return roleService.deleteRoleById(id);
    }
    @GetMapping("/{id}")
    public ResponseResult getRoleById(@PathVariable Long id){
        return roleService.getRoleById(id);
    }
    @PutMapping()
    public ResponseResult updateRoleById(@RequestBody UpdateRoleDto updateRoleDto){
        return roleService.updateRoleById(updateRoleDto);
    }
    @GetMapping("/listAllRole")
    public ResponseResult listAllUsefulRole(){
        return roleService.listAllUsefulRole();
    }
}
