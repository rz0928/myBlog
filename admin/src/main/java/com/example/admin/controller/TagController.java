package com.example.admin.controller;

import com.example.blogframework.dto.TagDto;
import com.example.blogframework.service.TagService;
import com.example.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content/tag")
public class TagController {
    @Autowired
    private TagService tagService;
    @GetMapping("/list")
    public ResponseResult list(Integer pageNum,Integer pageSize,String name,String remark){
        return tagService.pageTagList(pageNum,pageSize,name,remark);
    }
    @PostMapping()
    public ResponseResult addTag(@RequestBody TagDto tagDto){
        return tagService.addTag(tagDto.getName(), tagDto.getRemark());
    }
    @DeleteMapping("/{id}")
    public ResponseResult deleteTagById(@PathVariable Long id){
        tagService.getBaseMapper().deleteById(id);
        return ResponseResult.okResult();
    }
    @GetMapping("/{id}")
    public ResponseResult selectTagById(@PathVariable Long id){
        return ResponseResult.okResult(tagService.getBaseMapper().selectById(id));
    }
    @PutMapping()
    public ResponseResult updateTagById(@RequestBody TagDto tagDto){
        return tagService.updateTagById(tagDto.getId(), tagDto.getName(), tagDto.getRemark());
    }
    @GetMapping("/listAllTag")
    public  ResponseResult listAllTag(){
        return tagService.listAllTag();
    }
}
