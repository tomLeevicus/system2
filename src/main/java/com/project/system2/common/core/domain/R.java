package com.project.system2.common.core.domain;

import java.io.Serializable;

import com.project.system2.common.core.constant.HttpStatus;

/**
 * 响应信息主体
 */
public class R<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 成功 */
    public static final int SUCCESS = HttpStatus.SUCCESS;

    /** 失败 */
    public static final int ERROR = HttpStatus.ERROR;

    /** 状态码 */
    private int code;

    /** 返回内容 */
    private String msg;

    /** 数据对象 */
    private T data;

    /**
     * 初始化一个新创建的 R 对象
     */
    public R() {
    }

    /**
     * 初始化一个新创建的 R 对象
     * 
     * @param code 状态码
     * @param msg 返回内容
     */
    public R(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * 初始化一个新创建的 R 对象
     * 
     * @param code 状态码
     * @param msg 返回内容
     * @param data 数据对象
     */
    public R(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    /**
     * 返回成功消息
     * 
     * @return 成功消息
     */
    public static <T> R<T> ok() {
        return new R<>(SUCCESS, "操作成功");
    }

    /**
     * 返回成功数据
     * 
     * @return 成功消息
     */
    public static <T> R<T> ok(T data) {
        return new R<>(SUCCESS, "操作成功", data);
    }

    /**
     * 返回成功消息
     * 
     * @param msg 返回内容
     * @return 成功消息
     */
    public static <T> R<T> ok(String msg) {
        return new R<>(SUCCESS, msg);
    }

    /**
     * 返回成功消息
     * 
     * @param msg 返回内容
     * @param data 数据对象
     * @return 成功消息
     */
    public static <T> R<T> ok(String msg, T data) {
        return new R<>(SUCCESS, msg, data);
    }

    /**
     * 返回错误消息
     * 
     * @return 警告消息
     */
    public static <T> R<T> error() {
        return new R<>(ERROR, "操作失败");
    }

    /**
     * 返回错误消息
     * 
     * @param msg 返回内容
     * @return 警告消息
     */
    public static <T> R<T> error(String msg) {
        return new R<>(ERROR, msg);
    }

    /**
     * 返回错误消息
     * 
     * @param code 状态码
     * @param msg 返回内容
     * @return 警告消息
     */
    public static <T> R<T> error(int code, String msg) {
        return new R<>(code, msg);
    }

    /**
     * 返回失败消息
     * 
     * @param msg 返回内容
     * @return 失败消息
     */
    public static <T> R<T> fail(String msg) {
        return new R<>(ERROR, msg);
    }

    /**
     * 返回失败消息
     * 
     * @param code 状态码
     * @param msg 返回内容
     * @return 失败消息
     */
    public static <T> R<T> fail(int code, String msg) {
        return new R<>(code, msg, null);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
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