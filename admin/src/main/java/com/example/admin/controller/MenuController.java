package com.example.admin.controller;

import com.example.blogframework.dto.MenuDto;
import com.example.blogframework.service.MenuService;
import com.example.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system/menu")
public class MenuController {
    @Autowired
    MenuService menuService;

    @GetMapping("/list")
    public ResponseResult listMenu(String status,String menuName){
        return menuService.listMenu(status,menuName);
    }

    @PostMapping("")
    public ResponseResult addMenu(@RequestBody MenuDto menuDto){
        return menuService.addMenu(menuDto);
    }
    @GetMapping("/{id}")
    public ResponseResult getMenuById(@PathVariable Long id){
        return menuService.getMenuById(id);
    }
    @PutMapping("")
    public ResponseResult updateMenu(@RequestBody MenuDto menuDto){
        return menuService.updateMenu(menuDto);
    }
    @DeleteMapping("/{menuId}")
    public ResponseResult deleteMenuById(@PathVariable Long menuId){
        return menuService.deleteMenuById(menuId);
    }
    @GetMapping("/treeselect")
    public ResponseResult menuTree(){
        return menuService.menuTree();
    }
    @GetMapping("/roleMenuTreeselect/{id}")
    public ResponseResult getMenuTreeById(@PathVariable Long id){
        return menuService.getMenuTreeById(id);
    }
}
