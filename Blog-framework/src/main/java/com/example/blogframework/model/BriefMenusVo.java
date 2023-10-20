package com.example.blogframework.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BriefMenusVo {
    private List<BriefMenuTreeVo> menus;
}
