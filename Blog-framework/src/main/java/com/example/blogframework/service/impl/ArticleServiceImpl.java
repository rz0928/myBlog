package com.example.blogframework.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.blogframework.dto.AddArticleDto;
import com.example.blogframework.entity.ArticleTag;
import com.example.blogframework.model.*;
import com.example.blogframework.service.ArticleTagService;
import com.example.utils.RedisCache;
import com.example.constants.SystemConstants;
import com.example.blogframework.entity.Article;
import com.example.blogframework.entity.Category;
import com.example.blogframework.mapper.ArticleMapper;
import com.example.blogframework.service.ArticleService;
import com.example.blogframework.service.CategoryService;
import com.example.utils.BeanCopyUtils;
import com.example.utils.ResponseResult;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service("articleService")
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {
    @Resource
    CategoryService categoryService;
    @Resource
    RedisCache redisCache;
    @Autowired
    ArticleTagService articleTagService;
    @Autowired
    TransactionTemplate transactionTemplate;
    @Override
    public ResponseResult hotArticleList() {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Article::getStatus, SystemConstants.STATUS_NORMAL);
        queryWrapper.orderByDesc(Article::getViewCount);
        Page<Article> page = new Page<>(1, 10);
        page(page, queryWrapper);
        List<Article> articles = page.getRecords();
        List<HotArticleVo> result = BeanCopyUtils.copyBeanList(articles, HotArticleVo.class);
        return ResponseResult.okResult(result);
    }

    @Override
    public ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId) {
        //查询类型正常的文章
        LambdaQueryWrapper<Article> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Article::getStatus, SystemConstants.STATUS_NORMAL);
        if (Objects.nonNull(categoryId) && categoryId > 0) {
            lambdaQueryWrapper.eq(Article::getCategoryId, categoryId);
        }
        Page<Article> page = new Page<>(pageNum, pageSize);
        page(page, lambdaQueryWrapper);
        List<Article> articles = page.getRecords();
        //联查表category
        articles=articles.stream()
                .map(article -> article.setCategoryName(categoryService.getById(article.getCategoryId()).getName()))
                .collect(Collectors.toList());
        List<ArticleListVo> ArticleListVo = BeanCopyUtils.copyBeanList(page.getRecords(), ArticleListVo.class);
        PageVo pageVo = new PageVo(ArticleListVo, page.getTotal());
        return ResponseResult.okResult(pageVo);
    }
    @Override
    public ResponseResult getArticleDetail(Long articleId){
        Article article=getById(articleId);
        //从redis中获取浏览量
        Integer viewCount = (Integer)redisCache.getCacheMapValue(SystemConstants.VIEW_COUNT_KEY, articleId.toString());
        article.setViewCount(viewCount.longValue());
        //将Article复制位ArticleDetailVO
        ArticleDetailVo articleDetailVo=BeanCopyUtils.copyBean(article,ArticleDetailVo.class);
        //用categoryId联查表Category获取categoryName
        Long categoryId=articleDetailVo.getCategoryId();
        Category category=categoryService.getById(categoryId);
        if(category!=null){
            articleDetailVo.setCategoryName(category.getName());
        }
        return ResponseResult.okResult(articleDetailVo);
    }

    @Override
    public ResponseResult updateViewCount(Long articleId) {
        redisCache.incrementCacheMapValue(SystemConstants.VIEW_COUNT_KEY,articleId.toString(),1L);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult addArticle(AddArticleDto articleDto) {
        Article article = BeanCopyUtils.copyBean(articleDto, Article.class);
        save(article);
        List<ArticleTag> articleTags = articleDto.getTags().stream()
                .map(tagId -> new ArticleTag(article.getId(),tagId))
                .collect(Collectors.toList());
        articleTagService.saveBatch(articleTags);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult listAllArticle(Integer pageNum, Integer pageSize, String title, String summary) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Article::getStatus,SystemConstants.STATUS_NORMAL);
        queryWrapper.like(StringUtils.hasText(title),Article::getTitle,title);
        queryWrapper.like(StringUtils.hasText(summary),Article::getSummary,summary);
        Page<Article> page = new Page<>(pageNum,pageSize);
        page(page,queryWrapper);
        List<AdminArticlePageVo> articlePageVos = BeanCopyUtils.copyBeanList(page.getRecords(), AdminArticlePageVo.class);
        PageVo pageVo = new PageVo(articlePageVos,page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult selectArticleById(Long id) {
        Article article = getById(id);
        updateArticleVo updateArticleVo = BeanCopyUtils.copyBean(article, updateArticleVo.class);
        LambdaQueryWrapper<ArticleTag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ArticleTag::getArticleId,id);
        queryWrapper.select(ArticleTag::getTagId);
        List<Long> tagIds = getBaseMapper().getTagIdByArticleId(id);
        updateArticleVo.setTags(tagIds);
        return ResponseResult.okResult(updateArticleVo);
    }

    @Override
    @Transactional
    public ResponseResult updateArticle(updateArticleVo updateArticleDto) {
        Article article = BeanCopyUtils.copyBean(updateArticleDto, Article.class);
        List<Long> tagIds = updateArticleDto.getTags();
        HashSet<Long> hashTagId = new HashSet<>(tagIds);
        LambdaQueryWrapper<ArticleTag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ArticleTag::getArticleId,updateArticleDto.getId());
        List<ArticleTag> articleTagList = articleTagService.list(queryWrapper);
        List<Long> needDeleteTagIds =new ArrayList<>();
        for(ArticleTag articleTag : articleTagList){
            if(hashTagId.contains(articleTag.getTagId())){
                hashTagId.remove(articleTag.getTagId());
            }else{
                needDeleteTagIds.add(articleTag.getTagId());
            }
        }
        List<ArticleTag> articleTags = hashTagId.stream()
                .map(tagId->new ArticleTag(updateArticleDto.getId(), tagId))
                .collect(Collectors.toList());
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {

                try {
                    updateById(article);
                    articleTagService.saveBatch(articleTags);
                    if(!needDeleteTagIds.isEmpty()) {
                        queryWrapper.in(ArticleTag::getTagId,needDeleteTagIds);
                        articleTagService.remove(queryWrapper);
                    }
                } catch (Exception e){
                    //回滚
                    transactionStatus.setRollbackOnly();
                }

            }
        });
        return ResponseResult.okResult();
    }

    @Override
    @Transactional
    public ResponseResult deleteArticleById(Long id) {
        LambdaQueryWrapper<ArticleTag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ArticleTag::getArticleId,id);
        articleTagService.remove(queryWrapper);
        removeById(id);
        return ResponseResult.okResult();
    }
}
