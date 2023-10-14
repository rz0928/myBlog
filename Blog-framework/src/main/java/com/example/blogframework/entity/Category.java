package com.example.blogframework.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * 分类表(Category)实体类
 *
 * @author makejava
 * @since 2023-05-29 20:02:32
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("category")
public class Category  {
    /**
     * 分类id
     */
    @TableId
    private Long id;
    /**
     * 分类名
     */
    private String name;
    /**
     * 父分类id，如果没有父分类为-1
     */
    private Long pid;
    /**
     * 描述
     */
    private String description;
    /**
     * 状态0:正常,1禁用
     */
    private String status;
    //创建者
    @TableField(fill = FieldFill.INSERT)
    private Long createBy;
    //创建时间
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    //更新者
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateBy;
    //更新时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
    /**
     * 删除标志（0代表未删除，1代表已删除）
     */
    @TableField(fill = FieldFill.INSERT)
    private Integer delFlag;


}

