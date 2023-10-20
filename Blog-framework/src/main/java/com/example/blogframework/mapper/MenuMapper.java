package com.example.blogframework.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.blogframework.entity.Menu;
import com.example.blogframework.model.BriefMenuTreeVo;
import com.example.blogframework.model.MenuVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 菜单权限表(Menu)表数据库访问层
 *
 * @author makejava
 * @since 2023-09-17 09:46:57
 */
public interface MenuMapper extends BaseMapper<Menu> {

    List<String> selectPermissionsByUserId(@Param("userId")Long id);

    List<MenuVo> selectAllRouterMenu();

    List<MenuVo> selectRootMenuByUserId(@Param("userId")Long userId);

    List<BriefMenuTreeVo> selectAllBriefMenu();

}


