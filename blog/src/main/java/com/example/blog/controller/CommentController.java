package com.example.blog.controller;

import com.example.constants.SystemConstants;
import com.example.blogframework.entity.Comment;
import com.example.blogframework.service.CommentService;
import com.example.utils.ResponseResult;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController()
@RequestMapping("comment")
public class CommentController {
    @Resource
    private CommentService commentService;
    @GetMapping("/commentList")
    public ResponseResult commentList(@Param("articleId") Long articleId,
                                      @Param("pageNum")Integer pageNum,
                                      @Param("pageSize") Integer pageSize){
        return commentService.commentList(SystemConstants.ARTICLE_COMMENT,articleId,pageNum,pageSize);
    }
    @PostMapping("")
    public ResponseResult addComment(@RequestBody Comment comment){
        return commentService.addComment(comment);
    }
    @GetMapping("/linkCommentList")
    public ResponseResult linkCommentList(Integer pageNum,Integer pageSize){
        return commentService.commentList(SystemConstants.LINK_COMMENT,null,pageNum,pageSize);
    }
}
