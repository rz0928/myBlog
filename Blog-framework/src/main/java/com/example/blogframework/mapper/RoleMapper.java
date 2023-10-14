package com.example.blogframework.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.blogframework.entity.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 角色信息表(Role)表数据库访问层
 *
 * @author makejava
 * @since 2023-09-17 18:19:02
 */
public interface RoleMapper extends BaseMapper<Role> {

    List<String> selectRoleKeyByUserId(@Param("userId")Long id);
}


