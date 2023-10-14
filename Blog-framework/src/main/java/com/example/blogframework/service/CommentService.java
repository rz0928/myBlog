package com.example.blogframework.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.blogframework.entity.Comment;
import com.example.utils.ResponseResult;

/**
 * 评论表(Comment)表服务接口
 *
 * @author makejava
 * @since 2023-08-27 20:15:08
 */
public interface CommentService extends IService<Comment> {

    ResponseResult commentList(String CommentType, Long articleId, Integer pageNum, Integer pageSize);

    ResponseResult addComment(Comment comment);
}

