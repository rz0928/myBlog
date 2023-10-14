package com.example.admin.controller;

import com.example.admin.loginService.AdminLoginService;
import com.example.blogframework.entity.User;
import com.example.blogframework.model.AdminUserInfoVo;
import com.example.blogframework.model.RoutersVo;
import com.example.blogframework.model.UserInfoVo;
import com.example.blogframework.service.MenuService;
import com.example.blogframework.service.RoleService;
import com.example.enums.AppHttpCodeEnum;
import com.example.exception.SystemException;
import com.example.utils.BeanCopyUtils;
import com.example.utils.ResponseResult;
import com.example.utils.SecurityUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("")
public class LoginController {
    @Resource
    AdminLoginService adminLoginService;
    @Resource
    MenuService menuService;
    @Resource
    RoleService roleService;

    @PostMapping("/user/login")
    public ResponseResult login(@RequestBody User user) {
        if (!StringUtils.hasText(user.getUserName())) {
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        return adminLoginService.login(user);
    }

    @PostMapping("/user/logout")
    public ResponseResult logout() {
        return adminLoginService.logout();
    }

    @GetMapping("/getInfo")
    public ResponseResult getInfo() {
        User user = SecurityUtils.getLoginUser().getUser();
        List<String> permissions = menuService.selectPermissionsByUserId(user.getId());
        List<String> roles = roleService.selectRoleKeyByUserId(user.getId());
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(user, UserInfoVo.class);
        AdminUserInfoVo adminUserInfoVo = new AdminUserInfoVo(permissions, roles, userInfoVo);
        return ResponseResult.okResult(adminUserInfoVo);
    }

    @GetMapping("/getRouters")
    public ResponseResult getRouters() {
        Long userId = SecurityUtils.getUserId();
        RoutersVo routersVo = new RoutersVo(menuService.selectRouterMenuTreeByUserId(userId));
        return ResponseResult.okResult(routersVo);
    }
}
