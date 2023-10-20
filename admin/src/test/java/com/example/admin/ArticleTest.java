package com.example.admin;

import com.example.blogframework.entity.Article;
import com.example.blogframework.entity.Tag;
import com.example.blogframework.service.ArticleService;
import com.example.blogframework.service.TagService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

@SpringBootTest
public class ArticleTest {
    @Autowired
    ArticleService articleService;
    @Autowired
    TagService tagService;
    @Test
    public void addArticleTest() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Article article1 = (Article)Article.class.getDeclaredConstructor(Long.class, Long.class).newInstance(114L,2L);
        Article article2 = new Article(224L,3L);
        articleService.save(article1);
        articleService.save(article2);
    }
    @Test
    public void addTagTest() throws Exception{
        Tag tag1 = new Tag("test","test");
        tagService.save(tag1);
    }
}
