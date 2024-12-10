package com.project.system2.common.constant;

public class UserConstants {
    /** 平台内系统用户的唯一标志 */
    public static final String SYS_USER = "SYS_USER";

    /** 正常状态 */
    public static final String NORMAL = "0";
    /** 异常状态 */
    public static final String EXCEPTION = "1";
    /** 用户封禁状态 */
    public static final String USER_DISABLE = "1";
    /** 角色封禁状态 */
    public static final String ROLE_DISABLE = "1";
    /** 部门正常状态 */
    public static final String DEPT_NORMAL = "0";
    /** 部门停用状态 */
    public static final String DEPT_DISABLE = "1";
    /** 字典正常状态 */
    public static final String DICT_NORMAL = "0";

    /** 是否为系统默认（是） */
    public static final String YES = "Y";

    /** 用户名长度限制 */
    public static final int USERNAME_MIN_LENGTH = 2;
    public static final int USERNAME_MAX_LENGTH = 20;

    /** 密码长度限制 */
    public static final int PASSWORD_MIN_LENGTH = 5;
    public static final int PASSWORD_MAX_LENGTH = 20;

    /** 管理员ID */
    public static final Long ADMIN_ID = 1L;
} 