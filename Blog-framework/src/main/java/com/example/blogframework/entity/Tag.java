package com.example.blogframework.entity;


import java.io.Serializable;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 标签
 * &#064;TableName  tag
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@TableName("tag")
public class Tag implements Serializable {

    /**
     *
     */
    private Long id;
    /**
     * 标签名
     */
    private String name;
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

    @TableField(fill = FieldFill.INSERT)
    private Integer delFlag;
    /**
     * 备注
     */
    private String remark;

    public Tag(String name, String remark) {
        this.name = name;
        this.remark = remark;
    }
}
