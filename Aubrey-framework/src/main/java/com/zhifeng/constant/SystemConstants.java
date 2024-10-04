package com.zhifeng.constant;

/**
 * 封装常量
 */
public interface SystemConstants
{
    /**
     *  文章是草稿
     */
    public static final int ARTICLE_STATUS_DRAFT = 1;
    /**
     *  文章是正常发布状态
     */
    public static final int ARTICLE_STATUS_NORMAL = 0;
    /**
     * 友链状态为审核通过:即status=0
     */
    public static final String  LINK_STATUS_NORMAL = "0";
    /**
     * 表示为文章评论类型
     */
    public static final String  ARTICLE_COMMENT = "0";
    /**
     *  表示为友链评论类型
     */
    public static final String  LINK_COMMENT = "1";
    /**
     *  表示Menu菜单功能表中的menu_type字段值为C(菜单)
     */
    public static final String  MENU = "C";
    /**
     *  表示Menu菜单功能表中的menu_type字段值为F(按钮)
     */
    public static final String  BUTTON = "F";
    /**
     *  表示Menu菜单功能表中的status字段值为0(功能状态正常)
     */
    public static final String  STATUS_NORMAL = "0";
    /**
     *  表示当前用户身份为管理员ROLE_ADMIN
     */
    public static final String  ROLE_ADMIN = "1";
}