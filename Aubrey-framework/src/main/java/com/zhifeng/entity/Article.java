package com.zhifeng.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 文章表
 * @TableName sg_article
 */
@TableName(value ="sg_article")
@Data
@SuppressWarnings("serial")
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)    //此注解将当前实体类Article的所有set方法返回值设置为实体类对象Article
public class Article implements Serializable {
    //部分参数构造器
    public Article(Long id,long viewCount){
        this.id = id;
        this.viewCount = viewCount;
    }
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 文章内容
     */
    private String content;

    /**
     * 文章摘要
     */
    private String summary;

    /**
     * 所属分类id
     */
    private Long categoryId;

    /**
     * 缩略图
     */
    private String thumbnail;

    /**
     * 是否置顶（0否，1是）
     */
    private String isTop;

    /**
     * 状态（0已发布，1草稿）
     */
    private String status;

    /**
     * 访问量
     */
    private Long viewCount;

    /**
     * 是否允许评论 1是，0否
     */
    private String isComment;

    //自动填充时间和用户
    @TableField(fill = FieldFill.INSERT)
    private Long createBy;
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateBy;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 删除标志（0代表未删除，1代表已删除）
     */
    private Integer delFlag;
    /**
     * 文章分类名(数据库只具有categoryId分类id字段,因此使用@TableField(exist = false)注解,表示不存在此字段)
     */
    @TableField(exist = false)
    private String categoryName;
}