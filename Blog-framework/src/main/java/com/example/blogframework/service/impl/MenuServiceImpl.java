package com.example.blogframework.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.blogframework.dto.MenuDto;
import com.example.blogframework.entity.Menu;
import com.example.blogframework.mapper.MenuMapper;
import com.example.blogframework.model.BriefMenuTreeVo;
import com.example.blogframework.model.BriefMenusVo;
import com.example.blogframework.model.MenuVo;
import com.example.blogframework.service.MenuService;
import com.example.constants.SystemConstants;
import com.example.utils.BeanCopyUtils;
import com.example.utils.ResponseResult;
import com.example.utils.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 菜单权限表(Menu)表服务实现类
 *
 * @author makejava
 * @since 2023-09-17 09:52:42
 */
@Service("menuService")
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Override
    public List<String> selectPermissionsByUserId(Long id) {
        if (Objects.equals(id,1L)) {
            LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.in(Menu::getMenuType, SystemConstants.MENU, SystemConstants.BUTTON);
            queryWrapper.eq(Menu::getStatus, SystemConstants.STATUS_NORMAL);
            List<Menu> menus = list(queryWrapper);
            List<String> permissions = menus.stream()
                    .map(Menu::toString)
                    .collect(Collectors.toList());
            return permissions;
        }
        return getBaseMapper().selectPermissionsByUserId(id);
    }

    @Override
    public List<MenuVo> selectRouterMenuTreeByUserId(Long userId) {
        MenuMapper menuMapper = getBaseMapper();
        List<MenuVo> menuVos = null;
        if (Objects.equals(userId,1L)) {
            menuVos = menuMapper.selectAllRouterMenu();
        } else {
            menuVos = menuMapper.selectRootMenuByUserId(userId);
        }
        List<MenuVo> menuTree = MenuTreeUtils.generateMenuTree(menuVos, 0L);
        return menuTree;
    }

    @Override
    public ResponseResult listMenu(String status, String menuName) {
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(status),Menu::getStatus,status);
        queryWrapper.like(StringUtils.hasText(menuName),Menu::getMenuName,menuName);
        queryWrapper.eq(Menu::getDelFlag,SystemConstants.STATUS_NORMAL);
        queryWrapper.orderByAsc(Menu::getParentId,Menu::getOrderNum);
        List<Menu> menuList = list(queryWrapper);
        return ResponseResult.okResult(menuList);
    }

    @Override
    public ResponseResult addMenu(MenuDto menuDto) {
        Menu menu = BeanCopyUtils.copyBean(menuDto, Menu.class);
        save(menu);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getMenuById(Long id) {
        Menu menu = getById(id);
        return ResponseResult.okResult(menu);
    }

    @Override
    public ResponseResult updateMenu(MenuDto menuDto) {
        if(Objects.equals(menuDto.getId(),menuDto.getParentId())){
            throw new RuntimeException("修改菜单'写博文'失败，上级菜单不能选择自己");
        }
        Long parentId = getById(menuDto.getParentId()).getParentId();
        if(Objects.equals(parentId,menuDto.getId())){
            throw new RuntimeException("修改菜单'写博文'失败，上级菜单不能选择自己的子菜单");
        }
        LambdaQueryWrapper<Menu> queryWrapper =new LambdaQueryWrapper<>();
        queryWrapper.eq(Menu::getId,menuDto.getId());
        Menu menu = BeanCopyUtils.copyBean(menuDto, Menu.class);
        update(menu,queryWrapper);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteMenuById(Long menuId) {
        Menu menu = getById(menuId);
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Menu::getParentId,menu.getId());
        if(!list(queryWrapper).isEmpty()){
            throw new RuntimeException("存在子菜单不允许删除");
        }
        removeById(menuId);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult menuTree() {
        MenuMapper menuMapper = getBaseMapper();
        List<BriefMenuTreeVo> briefMenuTreeVos = menuMapper.selectAllBriefMenu();
        List<BriefMenuTreeVo> menuTree = MenuTreeUtils.generateBriefMenuTree(briefMenuTreeVos, 0L);
        return ResponseResult.okResult(menuTree);
    }

    @Override
    public ResponseResult getMenuTreeById(Long id) {
        MenuMapper menuMapper = getBaseMapper();
        List<BriefMenuTreeVo> briefMenuVos = menuMapper.selectAllBriefMenu();
        List<BriefMenuTreeVo> briefMenuTreeVo = MenuTreeUtils.generateBriefMenuTree(briefMenuVos, 0L);
        BriefMenusVo briefMenusVo = new BriefMenusVo(briefMenuTreeVo);
        return ResponseResult.okResult(briefMenusVo);
    }

    static class MenuTreeUtils{
        private static List<MenuVo> generateMenuTree(List<MenuVo> menuVos, Long parentId) {
            if(menuVos.isEmpty())
                return null;
            return menuVos.stream()
                    .filter(menuVo -> Objects.equals(menuVo.getParentId(), parentId))
                    .peek(menuVo -> menuVo.setChildren(generateMenuTree(menuVos, menuVo.getId())))
                    .collect(Collectors.toList());
        }
        public static List<BriefMenuTreeVo> generateBriefMenuTree(List<BriefMenuTreeVo> briefMenuVos,Long parentId){
            if(briefMenuVos.isEmpty()){
                return null;
            }
            return briefMenuVos.stream()
                    .filter(briefMenuVo -> Objects.equals(briefMenuVo.getParentId(),parentId))
                    .peek(briefMenuVo -> briefMenuVo.setChildren(generateBriefMenuTree(briefMenuVos, briefMenuVo.getId())))
                    .collect(Collectors.toList());
        }
    }
}
