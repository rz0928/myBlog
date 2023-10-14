package com.example.blog.runner;

import com.example.blogframework.entity.Article;
import com.example.blogframework.mapper.ArticleMapper;
import com.example.utils.RedisCache;
import com.example.constants.SystemConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ViewCountRunner implements CommandLineRunner {
    private final ArticleMapper articleMapper;
    private final RedisCache redisCache;
    @Autowired
    public ViewCountRunner(ArticleMapper articleMapper, RedisCache redisCache) {
        this.articleMapper = articleMapper;
        this.redisCache = redisCache;
    }

    @Override
    public void run(String... args) throws Exception {
        List<Article> articles = articleMapper.selectList(null);
        Map<String, Integer> viewCountMap = articles.stream()
                .collect(Collectors.toMap(article -> article.getId().toString(), article -> {
                    return article.getViewCount().intValue();
                }));
        redisCache.setCacheMap(SystemConstants.VIEW_COUNT_KEY,viewCountMap);
    }
}
