package com.project.system2.common.constant;

public class Constants {
    /** 成功标记 */
    public static final Integer SUCCESS = 200;
    /** 失败标记 */
    public static final Integer ERROR = 500;

    /** 登录成功 */
    public static final String LOGIN_SUCCESS = "Success";
    /** 注销 */
    public static final String LOGOUT = "Logout";
    /** 注册 */
    public static final String REGISTER = "Register";
    /** 登录失败 */
    public static final String LOGIN_FAIL = "Error";

    /** 自动识别json对象白名单配置（仅允许解析的包名，范围越小越安全） */
    public static final String[] JSON_WHITELIST_STR = { "org.springframework", "com.project" };

    /** 令牌自定义标识 */
    public static final String TOKEN = "token";
    /** 令牌前缀 */
    public static final String TOKEN_PREFIX = "Bearer ";
    /** 令牌前缀 */
    public static final String LOGIN_USER_KEY = "login_user_key";

    /** 参数管理 cache key */
    public static final String SYS_CONFIG_KEY = "sys_config:";
    /** 字典管理 cache key */
    public static final String SYS_DICT_KEY = "sys_dict:";

    /** 资源映射路径 前缀 */
    public static final String RESOURCE_PREFIX = "/profile";
} 