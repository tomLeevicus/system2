-- 字典类型初始化数据
INSERT INTO sys_dict_type VALUES(1, '用户性别', 'sys_user_sex',        '0', 'admin', CURRENT_TIMESTAMP, '', NULL, '用户性别列表', '0');
INSERT INTO sys_dict_type VALUES(2, '菜单状态', 'sys_show_hide',       '0', 'admin', CURRENT_TIMESTAMP, '', NULL, '菜单状态列表', '0');
INSERT INTO sys_dict_type VALUES(3, '系统开关', 'sys_normal_disable',  '0', 'admin', CURRENT_TIMESTAMP, '', NULL, '系统开关列表', '0');
INSERT INTO sys_dict_type VALUES(4, '任务状态', 'sys_job_status',      '0', 'admin', CURRENT_TIMESTAMP, '', NULL, '任务状态列表', '0');
INSERT INTO sys_dict_type VALUES(5, '数据状态', 'sys_common_status',   '0', 'admin', CURRENT_TIMESTAMP, '', NULL, '数据状态列表', '0');

-- 字典数据初始化数据
-- 用户性别
INSERT INTO sys_dict_data VALUES(1,  1, '男',     '0',  'sys_user_sex',        '',   '',        'Y', '0', 'admin', CURRENT_TIMESTAMP, '', NULL, '性别男', '0');
INSERT INTO sys_dict_data VALUES(2,  2, '女',     '1',  'sys_user_sex',        '',   '',        'N', '0', 'admin', CURRENT_TIMESTAMP, '', NULL, '性别女', '0');
INSERT INTO sys_dict_data VALUES(3,  3, '未知',   '2',  'sys_user_sex',        '',   '',        'N', '0', 'admin', CURRENT_TIMESTAMP, '', NULL, '性别未知', '0');

-- 菜单状态
INSERT INTO sys_dict_data VALUES(4,  1, '显示',   '0',  'sys_show_hide',       '',   'primary', 'Y', '0', 'admin', CURRENT_TIMESTAMP, '', NULL, '显示菜单', '0');
INSERT INTO sys_dict_data VALUES(5,  2, '隐藏',   '1',  'sys_show_hide',       '',   'danger',  'N', '0', 'admin', CURRENT_TIMESTAMP, '', NULL, '隐藏菜单', '0');

-- 系统开关
INSERT INTO sys_dict_data VALUES(6,  1, '正常',   '0',  'sys_normal_disable',  '',   'primary', 'Y', '0', 'admin', CURRENT_TIMESTAMP, '', NULL, '正常状态', '0');
INSERT INTO sys_dict_data VALUES(7,  2, '停用',   '1',  'sys_normal_disable',  '',   'danger',  'N', '0', 'admin', CURRENT_TIMESTAMP, '', NULL, '停用状态', '0');

-- 任务状态
INSERT INTO sys_dict_data VALUES(8,  1, '正常',   '0',  'sys_job_status',      '',   'primary', 'Y', '0', 'admin', CURRENT_TIMESTAMP, '', NULL, '正常状态', '0');
INSERT INTO sys_dict_data VALUES(9,  2, '暂停',   '1',  'sys_job_status',      '',   'danger',  'N', '0', 'admin', CURRENT_TIMESTAMP, '', NULL, '停用状态', '0');

-- 数据状态
INSERT INTO sys_dict_data VALUES(10, 1, '成功',   '0',  'sys_common_status',   '',   'primary', 'N', '0', 'admin', CURRENT_TIMESTAMP, '', NULL, '正常状态', '0');
INSERT INTO sys_dict_data VALUES(11, 2, '失败',   '1',  'sys_common_status',   '',   'danger',  'N', '0', 'admin', CURRENT_TIMESTAMP, '', NULL, '停用状态', '0'); 