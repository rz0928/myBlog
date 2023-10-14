package com.example.blogframework.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.blogframework.dto.AddLinkDto;
import com.example.blogframework.dto.UpdateLinkDto;
import com.example.blogframework.model.ListPageLinkVo;
import com.example.blogframework.model.PageVo;
import com.example.constants.SystemConstants;
import com.example.blogframework.entity.Link;
import com.example.blogframework.mapper.LinkMapper;
import com.example.blogframework.model.LinkVo;
import com.example.blogframework.service.LinkService;
import com.example.utils.BeanCopyUtils;
import com.example.utils.ResponseResult;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import sun.awt.image.ImageWatched;

import java.util.List;

/**
 * 友链(Link)表服务实现类
 *
 * @author makejava
 * @since 2023-06-04 15:53:18
 */
@Service("linkService")
public class LinkServiceImpl extends ServiceImpl<LinkMapper, Link> implements LinkService {

    @Override
    public ResponseResult getAllLink() {
        LambdaQueryWrapper<Link> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(Link::getStatus, SystemConstants.LINK_STATUS_NORMAL);
        List<Link> links=list(wrapper);
        List<LinkVo> linkVos = BeanCopyUtils.copyBeanList(links,LinkVo.class);
        return ResponseResult.okResult(linkVos);
    }

    @Override
    public ResponseResult listPageLink(Integer pageNum, Integer pageSize, String name, String status) {
        LambdaQueryWrapper<Link> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(name),Link::getName,name);
        queryWrapper.eq(StringUtils.hasText(status),Link::getStatus,status);
        Page<Link> page = new Page<>(pageNum,pageSize);
        page(page,queryWrapper);
        List<ListPageLinkVo> pageLinkVos = BeanCopyUtils.copyBeanList(page.getRecords(), ListPageLinkVo.class);
        return ResponseResult.okResult(new PageVo(pageLinkVos,page.getTotal()));
    }

    @Override
    public ResponseResult addLink(AddLinkDto addLinkDto) {
        Link link = BeanCopyUtils.copyBean(addLinkDto, Link.class);
        save(link);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getLinkById(Long id) {
        Link link = getById(id);
        ListPageLinkVo LinkVo = BeanCopyUtils.copyBean(link, ListPageLinkVo.class);
        return ResponseResult.okResult(LinkVo);
    }

    @Override
    public ResponseResult deleteLinkById(Long id) {
        LambdaUpdateWrapper<Link> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(Link::getDelFlag,SystemConstants.STATUS_DRAFT);
        updateWrapper.eq(Link::getId,id);
        update(updateWrapper);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult updateLink(UpdateLinkDto updateLinkDto) {
        Link link = BeanCopyUtils.copyBean(updateLinkDto, Link.class);
        updateById(link);
        return ResponseResult.okResult();
    }
}
