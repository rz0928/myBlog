package com.example.admin.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.example.blogframework.dto.AddCategoryDto;
import com.example.blogframework.dto.UpdateCategoryDto;
import com.example.blogframework.dto.UpdateRoleDto;
import com.example.blogframework.entity.Category;
import com.example.blogframework.model.ExcelCategoryVo;
import com.example.blogframework.service.CategoryService;
import com.example.enums.AppHttpCodeEnum;
import com.example.utils.BeanCopyUtils;
import com.example.utils.ResponseResult;
import com.example.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/content/category")
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    @GetMapping("/listAllCategory")
    public ResponseResult listAllCategory(){
        return categoryService.listAllCategory();
    }

    @GetMapping("/export")
    public void exportAllCategory(HttpServletResponse response){
        try {
            //设置下载文件的请求头
            WebUtils.setDownLoadHeader("分类.xlsx",response);
            //获取需要导出的数据
            List<Category> categoryVos = categoryService.list();

            List<ExcelCategoryVo> excelCategoryVos = BeanCopyUtils.copyBeanList(categoryVos, ExcelCategoryVo.class);
            //把数据写入到Excel中
            EasyExcel.write(response.getOutputStream(), ExcelCategoryVo.class).autoCloseStream(Boolean.FALSE).sheet("分类导出")
                    .doWrite(excelCategoryVos);

        } catch (Exception e) {
            //如果出现异常也要响应json
            ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
            WebUtils.renderString(response, JSON.toJSONString(result));
        }
    }
    @GetMapping("/list")
    public ResponseResult listPageCategory(Integer pageNum,Integer pageSize,String name,String status){
        return categoryService.listPageCategory(pageNum,pageSize,name,status);
    }
    @PostMapping()
    public ResponseResult addCategory(@RequestBody AddCategoryDto addCategoryDto){
        return categoryService.addCategory(addCategoryDto);
    }
    @GetMapping("/{id}")
    public ResponseResult getCategoryById(@PathVariable Long id){
        return categoryService.getCategoryById(id);
    }
    @PutMapping("")
    public ResponseResult updateCategory(@RequestBody UpdateCategoryDto updateCategoryDto){
        return categoryService.updateCategory(updateCategoryDto);
    }
    @DeleteMapping("{id}")
    public ResponseResult deleteCategoryById(@PathVariable Long id){
        return categoryService.deleteCategoryById(id);
    }
}
