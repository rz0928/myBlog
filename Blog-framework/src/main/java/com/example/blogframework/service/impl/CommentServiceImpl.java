package com.example.blogframework.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.constants.SystemConstants;
import com.example.blogframework.entity.Comment;
import com.example.exception.SystemException;
import com.example.blogframework.mapper.CommentMapper;
import com.example.blogframework.model.CommentVo;
import com.example.blogframework.model.PageVo;
import com.example.blogframework.service.CommentService;
import com.example.blogframework.service.UserService;
import com.example.utils.BeanCopyUtils;
import com.example.utils.ResponseResult;
import com.example.enums.AppHttpCodeEnum;
import com.example.utils.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * 评论表(Comment)表服务实现类
 *
 * @author makejava
 * @since 2023-08-27 20:15:08
 */
@Service("commentService")
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {
    @Resource
    private UserService userService;
    @Override
    public ResponseResult commentList(String commentType, Long articleId, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<Comment> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(SystemConstants.ARTICLE_COMMENT.equals(commentType),Comment::getArticleId,articleId);
        queryWrapper.eq(Comment::getRootId,-1);
        queryWrapper.eq(Comment::getType,commentType);
        Page<Comment> page=new Page<>(pageNum,pageSize);
        page(page,queryWrapper);
        //评论需要展示的是nickName，需要根据id查用户的nickName
        List<CommentVo> commentVoList=toCommentVoList(page.getRecords());
        for (CommentVo commentVo: commentVoList) {
            List<CommentVo> children=getChildren(commentVo.getId());
            commentVo.setChildren(children);
        }
        return ResponseResult.okResult(new PageVo(commentVoList,page.getTotal()));
    }

    /**
     * 将List<Comment>转成List<CommentVo>
     * @param list
     * @return
     */
    private List<CommentVo> toCommentVoList(List<Comment> list){
        List<CommentVo> commentVos = BeanCopyUtils.copyBeanList(list, CommentVo.class);
        for (CommentVo commentVo :commentVos) {
           String nickName = userService.getById(commentVo.getCreateBy()).getNickName();
           commentVo.setUsername(nickName);
           if(commentVo.getToCommentId()!=-1){
               String toCommentUserName = userService.getById(commentVo.getToCommentUserId()).getNickName();
               commentVo.setToCommentUserName(toCommentUserName);
           }
        }
        return commentVos;
    }
    /**
     * 根据根评论id搜索子评论
     * @param id 子评论对应根评论的id
     * @return List<CommentVo> commentVoList
     */
    private List<CommentVo> getChildren(Long id){
        LambdaQueryWrapper<Comment> queryWrapper=new LambdaQueryWrapper<Comment>();
        queryWrapper.eq(Comment::getRootId,id);
        queryWrapper.orderByAsc(Comment::getCreateTime);
        List<Comment> commentList=list(queryWrapper);
        List<CommentVo> commentVoList=toCommentVoList(commentList);
        return commentVoList;
    }
    @Override
    public ResponseResult addComment(Comment comment) {
        if(!StringUtils.hasText(comment.getContent())){
            throw new SystemException(AppHttpCodeEnum.CONTENT_NOT_NULL);
        }
        comment.setCreateBy(SecurityUtils.getUserId());
        save(comment);
        return  ResponseResult.okResult();
    }
}

