package com.example.blogframework.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.example.blogframework.dto.AddCategoryDto;
import com.example.blogframework.dto.UpdateCategoryDto;
import com.example.blogframework.entity.Category;
import com.example.utils.ResponseResult;

public interface CategoryService extends IService<Category> {
    ResponseResult<Category> getCategoryList();

    ResponseResult listAllCategory();

    ResponseResult listPageCategory(Integer pageNum, Integer pageSize, String name, String status);

    ResponseResult addCategory(AddCategoryDto addCategoryDto);

    ResponseResult getCategoryById(Long id);

    ResponseResult updateCategory(UpdateCategoryDto updateCategoryDto);

    ResponseResult deleteCategoryById(Long id);
}

