package com.example.blogframework.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.blogframework.dto.AddCategoryDto;
import com.example.blogframework.dto.UpdateCategoryDto;
import com.example.blogframework.entity.Link;
import com.example.blogframework.model.CategoryVo;
import com.example.blogframework.model.PageVo;
import com.example.constants.SystemConstants;
import com.example.blogframework.entity.Article;
import com.example.blogframework.entity.Category;
import com.example.blogframework.mapper.CategoryMapper;
import com.example.blogframework.model.ListCategoryVo;
import com.example.blogframework.service.ArticleService;
import com.example.blogframework.service.CategoryService;
import com.example.enums.AppHttpCodeEnum;
import com.example.utils.BeanCopyUtils;
import com.example.utils.ResponseResult;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * (Category)表服务实现类
 *
 * @author makejava
 * @since 2023-05-25 21:15:07
 */
@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Resource
    ArticleService articleService;

    public ResponseResult getCategoryList() {
        LambdaQueryWrapper<Article> articleWrapper = new LambdaQueryWrapper<>();
        articleWrapper.eq(Article::getStatus, SystemConstants.STATUS_NORMAL);
        List<Article> articleList = articleService.list(articleWrapper);
        Set<Long> CategoryIds = articleList.stream()
                .map(Article::getCategoryId)
                .collect(Collectors.toSet());
        List<Category> categoryList = listByIds(CategoryIds);
        categoryList = categoryList.stream()
                .filter(category -> category.getStatus().equals(SystemConstants.CATEGORY_STATUS_NORMAL))
                .collect(Collectors.toList());
        List<ListCategoryVo> listCategoryVos = BeanCopyUtils.copyBeanList(categoryList, ListCategoryVo.class);
        return ResponseResult.okResult(listCategoryVos);
    }

    @Override
    public ResponseResult listAllCategory() {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Category::getStatus, SystemConstants.STATUS_NORMAL);
        List<Category> categoryList = list(queryWrapper);
        List<ListCategoryVo> listCategoryVoList = BeanCopyUtils.copyBeanList(categoryList, ListCategoryVo.class);
        return ResponseResult.okResult(listCategoryVoList);
    }

    @Override
    public ResponseResult listPageCategory(Integer pageNum, Integer pageSize, String name, String status) {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(name), Category::getName, name);
        queryWrapper.like(StringUtils.hasText(status), Category::getStatus, status);
        Page<Category> categoryPage = new Page<>(pageNum, pageSize);
        page(categoryPage, queryWrapper);
        List<CategoryVo> pageCategories = BeanCopyUtils.copyBeanList(categoryPage.getRecords(), CategoryVo.class);
        return ResponseResult.okResult(new PageVo(pageCategories, categoryPage.getTotal()));
    }

    @Override
    public ResponseResult addCategory(AddCategoryDto addCategoryDto) {
        Category category = BeanCopyUtils.copyBean(addCategoryDto, Category.class);
        save(category);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getCategoryById(Long id) {
        Category category = getById(id);
        CategoryVo categoryVo = BeanCopyUtils.copyBean(category, CategoryVo.class);
        return ResponseResult.okResult(categoryVo);
    }

    @Override
    public ResponseResult updateCategory(UpdateCategoryDto updateCategoryDto) {
        Category category = BeanCopyUtils.copyBean(updateCategoryDto, Category.class);
        updateById(category);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteCategoryById(Long id) {
        LambdaUpdateWrapper<Category> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(Category::getDelFlag,SystemConstants.STATUS_DRAFT);
        updateWrapper.eq(Category::getId,id);
        update(updateWrapper);
        return ResponseResult.okResult();
    }
}

