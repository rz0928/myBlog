package com.example.blogframework.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.blogframework.dto.AddArticleDto;
import com.example.blogframework.entity.Article;
import com.example.blogframework.model.HotArticleVo;
import com.example.blogframework.model.updateArticleVo;
import com.example.utils.ResponseResult;
import org.springframework.transaction.annotation.Transactional;

public interface ArticleService extends IService<Article> {


    ResponseResult<HotArticleVo> hotArticleList();

    ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId);

    ResponseResult getArticleDetail(Long articleId);

    ResponseResult updateViewCount(Long articleId);

    ResponseResult addArticle(AddArticleDto articleDto);

    ResponseResult listAllArticle(Integer pageNum, Integer pageSize, String title, String summary);

    ResponseResult selectArticleById(Long id);

    ResponseResult updateArticle(updateArticleVo updateArticleDto);

    ResponseResult deleteArticleById(Long id);
}
