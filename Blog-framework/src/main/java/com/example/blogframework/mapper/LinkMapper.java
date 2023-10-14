package com.example.blogframework.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.blogframework.entity.Link;
import org.apache.ibatis.annotations.Mapper;


/**
 * 友链(Link)表数据库访问层
 *
 * @author makejava
 * @since 2023-06-04 15:53:18
 */
@Mapper
public interface LinkMapper extends BaseMapper<Link> {

}


