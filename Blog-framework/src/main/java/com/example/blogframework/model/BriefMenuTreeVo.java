package com.example.blogframework.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BriefMenuTreeVo {
    private List<BriefMenuTreeVo> children;
    private Long id;
    private String label;
    private Long parentId;
}
