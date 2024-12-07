CREATE DATABASE IF NOT EXISTS system2 DEFAULT CHARSET utf8mb4 COLLATE utf8mb4_general_ci;

USE system2;

-- 用户表
CREATE TABLE sys_user (
    user_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '用户ID',
    dept_id BIGINT COMMENT '部门ID',
    username VARCHAR(30) NOT NULL COMMENT '用户账号',
    nickname VARCHAR(30) NOT NULL COMMENT '用户昵称',
    email VARCHAR(50) COMMENT '用户邮箱',
    mobile VARCHAR(11) COMMENT '手机号码',
    gender CHAR(1) COMMENT '用户性别（0男 1女 2未知）',
    avatar VARCHAR(100) COMMENT '头像地址',
    password VARCHAR(100) COMMENT '密码',
    status CHAR(1) DEFAULT '0' COMMENT '帐号状态（0正常 1停用）',
    del_flag CHAR(1) DEFAULT '0' COMMENT '删除标志（0代表存在 1代表删除）',
    last_login_time DATETIME COMMENT '最后登录时间',
    create_by VARCHAR(64) COMMENT '创建者',
    create_time DATETIME COMMENT '创建时间',
    update_by VARCHAR(64) COMMENT '更新者',
    update_time DATETIME COMMENT '更新时间',
    remark VARCHAR(500) COMMENT '备注',
    UNIQUE KEY idx_username (username)
) ENGINE=InnoDB COMMENT='用户信息表';

-- 部门表
CREATE TABLE sys_dept (
    dept_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '部门id',
    parent_id BIGINT DEFAULT 0 COMMENT '父部门id',
    dept_name VARCHAR(30) COMMENT '部门名称',
    order_num INT DEFAULT 0 COMMENT '显示顺序',
    leader VARCHAR(20) COMMENT '负责人',
    phone VARCHAR(11) COMMENT '联系电话',
    email VARCHAR(50) COMMENT '邮箱',
    status CHAR(1) DEFAULT '0' COMMENT '部门状态（0正常 1停用）',
    del_flag CHAR(1) DEFAULT '0' COMMENT '删除标志（0代表存在 1代表删除）',
    create_by VARCHAR(64) COMMENT '创建者',
    create_time DATETIME COMMENT '创建时间',
    update_by VARCHAR(64) COMMENT '更新者',
    update_time DATETIME COMMENT '更新时间',
    remark VARCHAR(500) COMMENT '备注'
) ENGINE=InnoDB COMMENT='部门表';

-- 角色表
CREATE TABLE sys_role (
    role_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '角色ID',
    role_name VARCHAR(30) NOT NULL COMMENT '角色名称',
    role_key VARCHAR(100) NOT NULL COMMENT '角色权限字符串',
    role_sort INT NOT NULL COMMENT '显示顺序',
    data_scope CHAR(1) DEFAULT '1' COMMENT '数据范围（1：全部数据权限 2：自定数据权限）',
    status CHAR(1) NOT NULL COMMENT '角色状态（0正常 1停用）',
    del_flag CHAR(1) DEFAULT '0' COMMENT '删除标志��0代表存在 1代表删除）',
    create_by VARCHAR(64) COMMENT '创建者',
    create_time DATETIME COMMENT '创建时间',
    update_by VARCHAR(64) COMMENT '更新者',
    update_time DATETIME COMMENT '更新时间',
    remark VARCHAR(500) COMMENT '备注'
) ENGINE=InnoDB COMMENT='角色信息表';

-- 菜单表
CREATE TABLE sys_menu (
    menu_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '菜单ID',
    parent_id BIGINT COMMENT '父菜单ID',
    menu_name VARCHAR(50) NOT NULL COMMENT '菜单名称',
    path VARCHAR(200) COMMENT '路由地址',
    component VARCHAR(255) COMMENT '组件路径',
    perms VARCHAR(100) COMMENT '权限标识',
    icon VARCHAR(100) COMMENT '菜单图标',
    menu_type CHAR(1) COMMENT '菜单类型（M目录 C菜单 F按钮）',
    order_num INT DEFAULT 0 COMMENT '显示顺序',
    status CHAR(1) DEFAULT '0' COMMENT '菜单状态（0正常 1停用）',
    visible CHAR(1) DEFAULT '0' COMMENT '显示状态（0显示 1隐藏）',
    del_flag CHAR(1) DEFAULT '0' COMMENT '删除标志（0代表存在 1代表删除）',
    create_by VARCHAR(64) COMMENT '创建者',
    create_time DATETIME COMMENT '创建时间',
    update_by VARCHAR(64) COMMENT '更新者',
    update_time DATETIME COMMENT '更新时间',
    remark VARCHAR(500) COMMENT '备注'
) ENGINE=InnoDB COMMENT='菜单权限表';

-- 用户和角色关联表
CREATE TABLE sys_user_role (
    user_id BIGINT NOT NULL COMMENT '用户ID',
    role_id BIGINT NOT NULL COMMENT '角色ID',
    PRIMARY KEY(user_id, role_id)
) ENGINE=InnoDB COMMENT='用户和角色关联表';

-- 角色和菜单关联表
CREATE TABLE sys_role_menu (
    role_id BIGINT NOT NULL COMMENT '角色ID',
    menu_id BIGINT NOT NULL COMMENT '菜单ID',
    PRIMARY KEY(role_id, menu_id)
) ENGINE=InnoDB COMMENT='角色和菜单关联表'; 