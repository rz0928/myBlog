package com.example.blogframework.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.blogframework.entity.Tag;
import com.example.utils.ResponseResult;


/**
 * 标签(Tag)表服务接口
 *
 * @author makejava
 * @since 2023-09-13 17:51:39
 */
public interface TagService extends IService<Tag> {

    ResponseResult pageTagList(Integer pageNum, Integer pageSize, String name, String remark);

    ResponseResult addTag(String name, String remark);


    ResponseResult updateTagById(Long id, String name, String remark);

    ResponseResult listAllTag();

}

