package com.example.blog;

import com.example.blogframework.entity.Article;
import com.example.blogframework.service.ArticleService;
import com.example.utils.RedisCache;
import com.example.constants.SystemConstants;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SpringBootTest
public class RunnerTest {
    @Resource
    private RedisCache redisCache;
    @Resource
    private ArticleService articleService;
    @Test
    public void updateViewCount(){
        Map<String, Integer> viewCountMap=redisCache.getCacheMap(SystemConstants.VIEW_COUNT_KEY);
        List<Article> articles = viewCountMap.entrySet()
                .stream()
                .map(entry -> new Article(Long.valueOf(entry.getKey()), entry.getValue().longValue()))
                .collect(Collectors.toList());
    }
    @Test
    public void increment(){
        redisCache.incrementCacheMapValue(SystemConstants.VIEW_COUNT_KEY,"1",1L);
    }
}
