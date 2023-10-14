package com.example.blogframework.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.blogframework.entity.User;
import org.apache.ibatis.annotations.Mapper;


/**
 * 用户表(SysUser)表数据库访问层
 *
 * @author makejava
 * @since 2023-06-04 19:27:03
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}


