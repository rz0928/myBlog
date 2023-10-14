package com.example.blogframework.security;

import com.example.blogframework.service.MenuService;
import com.example.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("ps")
public class PermissionService {
    @Autowired
    private MenuService menuService;

    public Boolean hasPermission(String permission){
        if(SecurityUtils.isAdmin()){
            return true;
        }
        List<String> permissions = menuService.selectPermissionsByUserId(SecurityUtils.getUserId());
        return permissions.contains(permission);
    }
}
