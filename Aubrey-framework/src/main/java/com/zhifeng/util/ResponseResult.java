package com.zhifeng.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.zhifeng.enums.AppHttpCodeEnum;

import java.io.Serializable;

//该类为项目的响应类封装:所有的响应信息都可封装到该类中进行返回
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseResult<T> implements Serializable {
    private Integer code;
    private String msg;
    private T data;//泛型,用于指定一般的返回对象格式(比如Article,User等实体类)

    public ResponseResult() {
        // AppHttpCodeEnum响应枚举类,封装响应成功或失败返回的响应状态码和响应信息
        this.code = AppHttpCodeEnum.SUCCESS.getCode();
        this.msg = AppHttpCodeEnum.SUCCESS.getMsg();
    }
    //构造方法
    public ResponseResult(Integer code, T data) {
        this.code = code;
        this.data = data;
    }
    //构造方法
    public ResponseResult(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
    //构造方法
    public ResponseResult(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
    //静态方法:表示请求失败,返回错误提示信息
    public static ResponseResult errorResult(int code, String msg) {
        ResponseResult result = new ResponseResult();
        return result.error(code, msg);
    }
    //静态方法:表示请求成功,返回成功提示即可
    public static ResponseResult okResult() {
        ResponseResult result = new ResponseResult();
        return result;
    }
    //静态方法:表示请求成功,返回成功提示信息(响应状态码和响应信息)
    public static ResponseResult okResult(int code, String msg) {
        ResponseResult result = new ResponseResult();
        return result.ok(code, null, msg);
    }
    //静态方法:请求处理成功,返回指定对象类型Object data
    public static ResponseResult okResult(Object data) {
        ResponseResult result = setAppHttpCodeEnum(AppHttpCodeEnum.SUCCESS, AppHttpCodeEnum.SUCCESS.getMsg());
        if(data!=null) {
            result.setData(data);
        }
        return result;
    }

    public static ResponseResult errorResult(AppHttpCodeEnum enums){
        return setAppHttpCodeEnum(enums,enums.getMsg());
    }

    public static ResponseResult errorResult(AppHttpCodeEnum enums, String msg){
        return setAppHttpCodeEnum(enums,msg);
    }

    public static ResponseResult setAppHttpCodeEnum(AppHttpCodeEnum enums){
        return okResult(enums.getCode(),enums.getMsg());
    }

    private static ResponseResult setAppHttpCodeEnum(AppHttpCodeEnum enums, String msg){
        return okResult(enums.getCode(),msg);
    }
    //错误提示的方法:指定响应状态码和提示信息
    public ResponseResult<?> error(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
        return this;
    }
    //相应成功的提示
    public ResponseResult<?> ok(Integer code, T data) {
        this.code = code;
        this.data = data;
        return this;
    }

    public ResponseResult<?> ok(Integer code, T data, String msg) {
        this.code = code;
        this.data = data;
        this.msg = msg;
        return this;
    }

    public ResponseResult<?> ok(T data) {
        this.data = data;
        return this;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}