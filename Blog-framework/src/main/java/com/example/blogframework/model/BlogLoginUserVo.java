package com.example.blogframework.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlogLoginUserVo {
    private String token;
    private UserInfoVo userInfo;
}
