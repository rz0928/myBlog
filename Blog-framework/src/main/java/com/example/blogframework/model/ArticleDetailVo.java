package com.example.blogframework.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class  ArticleDetailVo {

    private Date createTime;

    private Long id;
    //标题
    private String title;
    //文章内容
    private String content;
    //访问量
    private Long viewCount;
    //所属分类id
    private Long categoryId;

    private String categoryName;
    //是否允许评论 1是，0否
    private String isComment;
}
