package com.example.blogframework.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.blogframework.dto.AddLinkDto;
import com.example.blogframework.dto.UpdateLinkDto;
import com.example.blogframework.dto.UpdateLinkStatusDto;
import com.example.blogframework.entity.Link;
import com.example.utils.ResponseResult;


/**
 * 友链(Link)表服务接口
 *
 * @author makejava
 * @since 2023-06-04 15:53:18
 */
public interface LinkService extends IService<Link> {

    ResponseResult getAllLink();

    ResponseResult listPageLink(Integer pageNum, Integer pageSize, String name, String status);

    ResponseResult addLink(AddLinkDto addLinkDto);

    ResponseResult getLinkById(Long id);

    ResponseResult deleteLinkById(Long id);

    ResponseResult updateLink(UpdateLinkDto updateLinkDto);

    ResponseResult changeLinkStatus(UpdateLinkStatusDto updateLinkStatusDto);
}

