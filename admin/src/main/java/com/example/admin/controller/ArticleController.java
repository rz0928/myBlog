package com.example.admin.controller;

import com.example.blogframework.dto.AddArticleDto;
import com.example.blogframework.entity.Menu;
import com.example.blogframework.model.updateArticleVo;
import com.example.blogframework.service.ArticleService;
import com.example.blogframework.service.MenuService;
import com.example.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content/article")
public class ArticleController {
    @Autowired
    ArticleService articleService;
    @PostMapping("")
    public ResponseResult addArticle(@RequestBody AddArticleDto articleDto){
        return articleService.addArticle(articleDto);
    }
    @GetMapping("/list")
    public ResponseResult listAllArticle(Integer pageNum, Integer pageSize,String title,String summary){
        return articleService.listAllArticle(pageNum,pageSize,title,summary);
    }
    @GetMapping("{id}")
    public ResponseResult selectArticleById(@PathVariable Long id){
        return articleService.selectArticleById(id);
    }
    @PutMapping("")
    public ResponseResult updateArticle(@RequestBody updateArticleVo updateArticleDto){
        return articleService.updateArticle(updateArticleDto);
    }
    @DeleteMapping("/{id}")
    public ResponseResult deleteArticleById(@PathVariable Long id){
        return articleService.deleteArticleById(id);
    }
}
