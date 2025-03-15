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

    /**
     * http请求
     */
    public static final String HTTP = "http://";

    /**
     * https请求
     */
    public static final String HTTPS = "https://";

    /**
     * 启用状态
     */
    public static final Integer STATUS_NORMAL = 0;
    
    /**
     * 停用状态
     */
    public static final Integer STATUS_DISABLE = 1;
    
    /**
     * 删除标志 - 未删除
     */
    public static final Integer NOT_DELETED = 0;
    
    /**
     * 删除标志 - 已删除
     */
    public static final Integer DELETED = 1;
    
    /**
     * 菜单类型 - 目录
     */
    public static final String MENU_TYPE_DIR = "M";
    
    /**
     * 菜单类型 - 菜单
     */
    public static final String MENU_TYPE_MENU = "C";
    
    /**
     * 菜单类型 - 按钮
     */
    public static final String MENU_TYPE_BUTTON = "F";
    
    /**
     * 是否 - 是
     */
    public static final Integer YES = 1;
    
    /**
     * 是否 - 否
     */
    public static final Integer NO = 0;
    
    /**
     * 审批状态 - 待审批
     */
    public static final Integer APPROVAL_PENDING = 0;
    
    /**
     * 审批状态 - 已通过
     */
    public static final Integer APPROVAL_PASSED = 1;
    
    /**
     * 审批状态 - 已拒绝
     */
    public static final Integer APPROVAL_REJECTED = 2;
} 