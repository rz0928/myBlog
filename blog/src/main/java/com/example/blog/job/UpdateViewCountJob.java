package com.example.blog.job;

import com.example.blogframework.entity.Article;
import com.example.blogframework.service.ArticleService;
import com.example.utils.RedisCache;
import com.example.constants.SystemConstants;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class UpdateViewCountJob {
    @Resource
    private RedisCache redisCache;
    @Resource
    private ArticleService articleService;
    @Scheduled(cron = "0 0/10 * * * ?")
    public void updateViewCount(){
        Map<String, Integer> viewCountMap=redisCache.getCacheMap(SystemConstants.VIEW_COUNT_KEY);
        List<Article> articles = viewCountMap.entrySet()
                .stream()
                .map(entry -> new Article(Long.valueOf(entry.getKey()), entry.getValue().longValue()))
                .collect(Collectors.toList());
        articleService.updateBatchById(articles);
    }
}
