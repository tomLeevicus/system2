-- 清空表数据
DELETE FROM sys_role_menu;
DELETE FROM sys_user_role;
DELETE FROM sys_dict_data;
DELETE FROM sys_dict_type;
DELETE FROM sys_menu;
DELETE FROM sys_role;
DELETE FROM sys_user;

-- 重置自增ID
ALTER TABLE sys_menu AUTO_INCREMENT = 1;
ALTER TABLE sys_role AUTO_INCREMENT = 1;
ALTER TABLE sys_user AUTO_INCREMENT = 1;
ALTER TABLE sys_dict_type AUTO_INCREMENT = 1;
ALTER TABLE sys_dict_data AUTO_INCREMENT = 1;

-- 初始化用户数据
INSERT INTO sys_user (user_id, dept_id, username, nickname, email, mobile, gender, avatar, password, status, del_flag, create_by, create_time, update_by, update_time, remark) VALUES
(1, 100, 'admin', '超级管理员', 'admin@example.com', '15888888888', '0', '', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '0', '0', 'admin', now(), '', null, '超级管理员'),
(2, 100, 'user1', '管理员', 'user1@example.com', '15666666666', '0', '', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '0', '0', 'admin', now(), '', null, '管理员'),
(3, 100, 'test', '测试用户', 'test@example.com', '15555555555', '0', '', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '0', '0', 'admin', now(), '', null, '测试用户');

-- 初始化角色数据
INSERT INTO sys_role (role_id, role_name, role_key, role_sort, status, del_flag, create_by, create_time, update_by, update_time, remark) VALUES
(1, '超级管理员', 'admin', 1, '0', '0', 'admin', now(), '', null, '超级管理员'),
(2, '管理员', 'manager', 2, '0', '0', 'admin', now(), '', null, '管理员'),
(3, '普通用户', 'user', 3, '0', '0', 'admin', now(), '', null, '普通用户');

-- 初始化菜单数据
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES
-- 一级菜单
(1, '系统管理', 0, 1, 'system', null, 'M', '0', '0', '', 'system', 'admin', now(), '', null, '系统管理目录'),
(2, '工作流程', 0, 2, 'workflow', null, 'M', '0', '0', '', 'guide', 'admin', now(), '', null, '工作流程目录'),

-- 系统管理子菜单
(100, '用户管理', 1, 1, 'user', 'system/user/index', 'C', '0', '0', 'system:user:list', 'user', 'admin', now(), '', null, '用户管理菜单'),
(101, '角色管理', 1, 2, 'role', 'system/role/index', 'C', '0', '0', 'system:role:list', 'peoples', 'admin', now(), '', null, '角色管理菜单'),
(102, '菜单管理', 1, 3, 'menu', 'system/menu/index', 'C', '0', '0', 'system:menu:list', 'tree-table', 'admin', now(), '', null, '菜单管理菜单'),
(103, '字典管理', 1, 4, 'dict', 'system/dict/index', 'C', '0', '0', 'system:dict:list', 'dict', 'admin', now(), '', null, '字典管理菜单'),

-- 工作流程子菜单
(200, '流程管理', 2, 1, 'process', 'workflow/process/index', 'C', '0', '0', 'workflow:process:list', 'list', 'admin', now(), '', null, '流程管理菜单'),
(201, '流程实例', 2, 2, 'instance', 'workflow/instance/index', 'C', '0', '0', 'workflow:instance:list', 'component', 'admin', now(), '', null, '流程实例菜单'),
(202, '任务管理', 2, 3, 'task', 'workflow/task/index', 'C', '0', '0', 'workflow:task:list', 'checkbox', 'admin', now(), '', null, '任务管理菜单'),

-- 用户管理按钮
(1001, '用户查询', 100, 1, '', '', 'F', '0', '0', 'system:user:query', '#', 'admin', now(), '', null, ''),
(1002, '用户新增', 100, 2, '', '', 'F', '0', '0', 'system:user:add', '#', 'admin', now(), '', null, ''),
(1003, '用户修改', 100, 3, '', '', 'F', '0', '0', 'system:user:edit', '#', 'admin', now(), '', null, ''),
(1004, '用户删除', 100, 4, '', '', 'F', '0', '0', 'system:user:remove', '#', 'admin', now(), '', null, ''),
(1005, '用户导出', 100, 5, '', '', 'F', '0', '0', 'system:user:export', '#', 'admin', now(), '', null, ''),
(1006, '用户导入', 100, 6, '', '', 'F', '0', '0', 'system:user:import', '#', 'admin', now(), '', null, ''),
(1007, '重置密码', 100, 7, '', '', 'F', '0', '0', 'system:user:resetPwd', '#', 'admin', now(), '', null, ''),

-- 角色管理按钮
(1008, '角色查询', 101, 1, '', '', 'F', '0', '0', 'system:role:query', '#', 'admin', now(), '', null, ''),
(1009, '角色新增', 101, 2, '', '', 'F', '0', '0', 'system:role:add', '#', 'admin', now(), '', null, ''),
(1010, '角色修改', 101, 3, '', '', 'F', '0', '0', 'system:role:edit', '#', 'admin', now(), '', null, ''),
(1011, '角色删除', 101, 4, '', '', 'F', '0', '0', 'system:role:remove', '#', 'admin', now(), '', null, ''),
(1012, '角色导出', 101, 5, '', '', 'F', '0', '0', 'system:role:export', '#', 'admin', now(), '', null, ''),

-- 菜单管理按钮
(1013, '菜单查询', 102, 1, '', '', 'F', '0', '0', 'system:menu:query', '#', 'admin', now(), '', null, ''),
(1014, '菜单新增', 102, 2, '', '', 'F', '0', '0', 'system:menu:add', '#', 'admin', now(), '', null, ''),
(1015, '菜单修改', 102, 3, '', '', 'F', '0', '0', 'system:menu:edit', '#', 'admin', now(), '', null, ''),
(1016, '菜单删除', 102, 4, '', '', 'F', '0', '0', 'system:menu:remove', '#', 'admin', now(), '', null, ''),

-- 字典管理按钮
(1017, '字典查询', 103, 1, '', '', 'F', '0', '0', 'system:dict:query', '#', 'admin', now(), '', null, ''),
(1018, '字典新增', 103, 2, '', '', 'F', '0', '0', 'system:dict:add', '#', 'admin', now(), '', null, ''),
(1019, '字典修改', 103, 3, '', '', 'F', '0', '0', 'system:dict:edit', '#', 'admin', now(), '', null, ''),
(1020, '字典删除', 103, 4, '', '', 'F', '0', '0', 'system:dict:remove', '#', 'admin', now(), '', null, ''),
(1021, '字典导出', 103, 5, '', '', 'F', '0', '0', 'system:dict:export', '#', 'admin', now(), '', null, ''),

-- 流程管理按钮
(2001, '流程查询', 200, 1, '', '', 'F', '0', '0', 'workflow:process:query', '#', 'admin', now(), '', null, ''),
(2002, '流程新增', 200, 2, '', '', 'F', '0', '0', 'workflow:process:add', '#', 'admin', now(), '', null, ''),
(2003, '流程修改', 200, 3, '', '', 'F', '0', '0', 'workflow:process:edit', '#', 'admin', now(), '', null, ''),
(2004, '流程删除', 200, 4, '', '', 'F', '0', '0', 'workflow:process:remove', '#', 'admin', now(), '', null, ''),
(2005, '流程部署', 200, 5, '', '', 'F', '0', '0', 'workflow:process:deploy', '#', 'admin', now(), '', null, ''),
(2006, '流程导出', 200, 6, '', '', 'F', '0', '0', 'workflow:process:export', '#', 'admin', now(), '', null, '');

-- 初始化用户和角色关联数据
INSERT INTO sys_user_role (user_id, role_id) VALUES
(1, 1),
(2, 2),
(3, 3);

-- 初始化角色和菜单关联数据
INSERT INTO sys_role_menu (role_id, menu_id) VALUES
-- 超级管理员拥有所有权限
(1, 1), (1, 2), (1, 100), (1, 101), (1, 102), (1, 103), (1, 200), (1, 201), (1, 202),
(1, 1001), (1, 1002), (1, 1003), (1, 1004), (1, 1005), (1, 1006), (1, 1007),
(1, 1008), (1, 1009), (1, 1010), (1, 1011), (1, 1012),
(1, 1013), (1, 1014), (1, 1015), (1, 1016),
(1, 1017), (1, 1018), (1, 1019), (1, 1020), (1, 1021),
(1, 2001), (1, 2002), (1, 2003), (1, 2004), (1, 2005), (1, 2006),

-- 管理员拥有部分权限
(2, 1), (2, 2), (2, 100), (2, 101), (2, 200), (2, 201), (2, 202),
(2, 1001), (2, 1002), (2, 1003),
(2, 1008), (2, 1009), (2, 1010),
(2, 2001), (2, 2002), (2, 2003),

-- 普通用户只有查看权限
(3, 2), (3, 200), (3, 201), (3, 202),
(3, 2001);

-- 初始化字典类型
INSERT INTO sys_dict_type (dict_id, dict_name, dict_type, status, create_by, create_time, update_by, update_time, remark) VALUES 
(1, '用户性别', 'sys_user_sex', '0', 'admin', now(), '', null, '用户性别列表'),
(2, '菜单状态', 'sys_show_hide', '0', 'admin', now(), '', null, '菜单状态列表'),
(3, '系统开关', 'sys_normal_disable', '0', 'admin', now(), '', null, '系统开关列表'),
(4, '任务状态', 'sys_job_status', '0', 'admin', now(), '', null, '任务状态列表'),
(5, '任务分组', 'sys_job_group', '0', 'admin', now(), '', null, '任务分组列表'),
(6, '系统是否', 'sys_yes_no', '0', 'admin', now(), '', null, '系统是否列表'),
(7, '通知类型', 'sys_notice_type', '0', 'admin', now(), '', null, '通知类型列表'),
(8, '通知状态', 'sys_notice_status', '0', 'admin', now(), '', null, '通知状态列表'),
(9, '操作类型', 'sys_oper_type', '0', 'admin', now(), '', null, '操作类型列表'),
(10, '系统状态', 'sys_common_status', '0', 'admin', now(), '', null, '登录状态列表');

-- 初始化字典数据
INSERT INTO sys_dict_data (dict_sort, dict_label, dict_value, dict_type, status, create_by, create_time, update_by, update_time, remark) VALUES
-- 用户性别
(1, '男', '0', 'sys_user_sex', '0', 'admin', now(), '', null, '性别男'),
(2, '女', '1', 'sys_user_sex', '0', 'admin', now(), '', null, '性别女'),
(3, '未知', '2', 'sys_user_sex', '0', 'admin', now(), '', null, '性别未知'),
-- 菜单状态
(1, '显示', '0', 'sys_show_hide', '0', 'admin', now(), '', null, '显示菜单'),
(2, '隐藏', '1', 'sys_show_hide', '0', 'admin', now(), '', null, '隐藏菜单'),
-- 系统开关
(1, '正常', '0', 'sys_normal_disable', '0', 'admin', now(), '', null, '正���状态'),
(2, '停用', '1', 'sys_normal_disable', '0', 'admin', now(), '', null, '停用状态'); 