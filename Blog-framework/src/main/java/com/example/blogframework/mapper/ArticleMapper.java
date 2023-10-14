package com.example.blogframework.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.blogframework.entity.Article;
import com.example.blogframework.model.updateArticleVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ArticleMapper extends BaseMapper<Article> {
    List<Long> getTagIdByArticleId(Long id);
}
