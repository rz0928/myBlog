package com.example.blog.controller;

import com.example.blogframework.service.ArticleService;
import com.example.utils.ResponseResult;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;

@RestController
@RequestMapping("/article")
public class ArticleController {
    @Resource
    private ArticleService articleService;
    @GetMapping("/hotArticleList")
    public ResponseResult hotArticleList(){
        return articleService.hotArticleList();
    }
    @GetMapping("/articleList")
    public ResponseResult articleList(@Param("pageNum") Integer pageNum,
                                      @Param("pageSize") Integer pageSize,
                                      @Param("categoryId") Long categoryId){
        return articleService.articleList(pageNum,pageSize,categoryId);
    }
    @GetMapping("/{id}")
    public ResponseResult getArticleDetail(@PathVariable("id") Long articleId){
        return articleService.getArticleDetail(articleId);
    }
    @PutMapping("/updateViewCount/{id}")
    public ResponseResult updateViewCount(@PathVariable("id")Long articleId){
        return articleService.updateViewCount(articleId);
    }
}
