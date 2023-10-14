package com.example.blogframework.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.blogframework.dto.MenuDto;
import com.example.blogframework.entity.Menu;
import com.example.blogframework.model.MenuVo;
import com.example.utils.ResponseResult;

import java.util.List;


/**
 * 菜单权限表(Menu)表服务接口
 *
 * @author makejava
 * @since 2023-09-17 09:46:57
 */
public interface MenuService extends IService<Menu> {

    List<String> selectPermissionsByUserId(Long id);

    List<MenuVo> selectRouterMenuTreeByUserId(Long userId);

    ResponseResult listMenu(String status, String menuName);

    ResponseResult addMenu(MenuDto menuDto);

    ResponseResult getMenuById(Long id);

    ResponseResult updateMenu(MenuDto menuDto);

    ResponseResult deleteMenuById(Long menuId);

    ResponseResult menuTree();

    ResponseResult getMenuTreeById(Long id);
}

