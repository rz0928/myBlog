package com.example.admin.controller;

import com.example.blogframework.dto.AddLinkDto;
import com.example.blogframework.dto.UpdateLinkDto;
import com.example.blogframework.dto.UpdateLinkStatusDto;
import com.example.blogframework.service.LinkService;
import com.example.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content/link")
public class LinkController {
    @Autowired
    LinkService linkService;
    @GetMapping("/list")
    public ResponseResult listPageLink(Integer pageNum,Integer pageSize,String name,String status){
        return linkService.listPageLink(pageNum,pageSize,name,status);
    }
    @PostMapping("")
    public ResponseResult addLink(@RequestBody AddLinkDto addLinkDto){
        return linkService.addLink(addLinkDto);
    }
    @GetMapping("/{id}")
    public ResponseResult getLinkById(@PathVariable Long id){
        return linkService.getLinkById(id);
    }
    @PutMapping("")
    public ResponseResult updateLink(@RequestBody UpdateLinkDto updateLinkDto){
        return linkService.updateLink(updateLinkDto);
    }
    @DeleteMapping("/{id}")
    public ResponseResult deleteLinkById(@PathVariable Long id){
        return linkService.deleteLinkById(id);
    }
    @PutMapping("/changeLinkStatus")
    public ResponseResult changeLinkStatus(@RequestBody UpdateLinkStatusDto updateLinkStatusDto){
        return linkService.changeLinkStatus(updateLinkStatusDto);
    }
}
