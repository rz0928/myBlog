package com.example.blogframework.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.blogframework.entity.Tag;
import com.example.blogframework.mapper.TagMapper;
import com.example.blogframework.model.PageVo;
import com.example.blogframework.model.TagVo;
import com.example.blogframework.service.TagService;
import com.example.enums.AppHttpCodeEnum;
import com.example.exception.SystemException;
import com.example.utils.BeanCopyUtils;
import com.example.utils.ResponseResult;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 标签(Tag)表服务实现类
 *
 * @author makejava
 * @since 2023-09-13 17:51:39
 */
@Service("tagService")
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    @Override
    public ResponseResult pageTagList(Integer pageNum, Integer pageSize, String name, String remark) {
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.hasText(name),Tag::getName,name);
        queryWrapper.eq(StringUtils.hasText(remark),Tag::getRemark,remark);
        Page<Tag> page=new Page<>(pageNum,pageSize);
        page(page, queryWrapper);
        List<TagVo> tagVos = BeanCopyUtils.copyBeanList(page.getRecords(),TagVo.class);
        PageVo pageVo = new PageVo(tagVos,page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult addTag(String name, String remark) {
        if(!StringUtils.hasText(name)){
            throw new SystemException(AppHttpCodeEnum.NEED_TAG_NAME);
        }
        Tag tag = new Tag(name,remark);
        save(tag);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult updateTagById(Long id, String name, String remark) {
        Tag tag = new Tag();
        tag.setId(id);
        tag.setName(name);
        tag.setRemark(remark);
        updateById(tag);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult listAllTag() {
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Tag::getId,Tag::getName);
        List<Tag> tagList = list(queryWrapper);
        List<TagVo> tagVoList = BeanCopyUtils.copyBeanList(tagList, TagVo.class);
        return ResponseResult.okResult(tagVoList);
    }
}
