package com.zhifeng.enums;
//响应枚举类:用于封装响应信息ResponseResult中需要的响应状态码code和对应的响应信息msg,从而通过SystemException全局异常配置类返回
//封装后,便于修改
public enum AppHttpCodeEnum {
    // 成功
    SUCCESS(200,"操作成功"),//即响应信息中code=200表示操作成功
    // 登录
    NEED_LOGIN(401,"需要登录后操作"),
    NO_OPERATOR_AUTH(403,"无权限操作"),
    SYSTEM_ERROR(500,"出现错误"),
    USERNAME_EXIST(501,"用户名已存在"),
    PHONENUMBER_EXIST(502,"手机号已存在"),
    EMAIL_EXIST(503, "邮箱已存在"),
    REQUIRE_USERNAME(504, "必需填写用户名"),
    LOGIN_ERROR(505,"用户名或密码错误"),
    CONTENT_NOT_NULL(506,"评论不能为空!"),
    FILE_TYPE_ERROR(507,"文件类型错误,请上传png或者jpg格式!"),
    USERNAME_NOT_NULL(508, "用户名不能为空"),
    NICKNAME_NOT_NULL(509, "昵称不能为空"),
    PASSWORD_NOT_NULL(510, "密码不能为空"),
    EMAIL_NOT_NULL(511, "邮箱不能为空"),
    NICKNAME_EXIST(512, "昵称已存在"),
    TAG_NOT_NULL(513,"新增标签不能为空!"),
    ID_NOT_NULL(514,"传入id不能为空!"),
    FILE_UPLOAD_ERROR(515,"文件上传失败!"),
    ARTICLE_NOT_NULL(516,"文章内容不能为空!"),
    MENU_NOT_NULL(517,"新增菜单信息(菜单名/路由地址/菜单顺序)可能为空!"),
    UPDATE_MENU_FAIL(518,"修改菜单失败,上级菜单不能选择自己!");
    int code;//响应状态码
    String msg;//响应信息

    AppHttpCodeEnum(int code, String errorMessage){
        this.code = code;
        this.msg = errorMessage;
    }
    public int getCode() {
        return code;
    }
    public String getMsg() {
        return msg;
    }
}