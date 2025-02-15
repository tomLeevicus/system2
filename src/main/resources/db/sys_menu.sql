/*
 Navicat Premium Dump SQL

 Source Server         : local
 Source Server Type    : MySQL
 Source Server Version : 80041 (8.0.41-0ubuntu0.24.04.1)
 Source Host           : 192.168.80.128:3306
 Source Schema         : system2

 Target Server Type    : MySQL
 Target Server Version : 80041 (8.0.41-0ubuntu0.24.04.1)
 File Encoding         : 65001

 Date: 12/02/2025 18:45:06
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `menu_id` bigint NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `menu_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '菜单名称',
  `parent_id` bigint NULL DEFAULT 0 COMMENT '父菜单ID',
  `order_num` int NULL DEFAULT 0 COMMENT '显示顺序',
  `path` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '路由地址',
  `component` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '组件路径',
  `query` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '路由参数',
  `is_frame` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '是否为外链（0否 1是）',
  `is_cache` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '是否缓存（0不缓存 1缓存）',
  `menu_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '菜单类型（M目录 C菜单 F按钮）',
  `visible` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '菜单状态（0显示 1隐藏）',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '菜单状态（0正常 1停用）',
  `perms` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '权限标识',
  `icon` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '#' COMMENT '菜单图标',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '备注',
  `create_by` bigint NULL DEFAULT NULL,
  `update_by` bigint NULL DEFAULT NULL,
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`menu_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3045 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '菜单权限表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (1, '系统管理', 0, 1, '/system', NULL, NULL, '0', '0', 'M', '0', '0', NULL, 'Setting', '2024-12-12 02:27:49', NULL, '系统管理目录', 1, NULL, NULL);
INSERT INTO `sys_menu` VALUES (2, '工作流程', 0, 2, '/workflow', NULL, NULL, '0', '0', 'M', '0', '0', NULL, 'Guide', '2024-12-12 02:27:49', NULL, '工作流程目录', 1, NULL, NULL);
INSERT INTO `sys_menu` VALUES (3, '资产管理', 0, 3, '/asset', NULL, NULL, '0', '0', 'M', '0', '0', NULL, 'House', '2024-12-12 02:27:49', NULL, '资产管理目录', 1, NULL, NULL);
INSERT INTO `sys_menu` VALUES (100, '用户管理', 1, 1, '/system/user', 'system/user/index', NULL, '0', '0', 'C', '0', '0', 'system:user:list', 'User', '2024-12-12 02:27:49', NULL, '用户管理菜单', 1, NULL, NULL);
INSERT INTO `sys_menu` VALUES (101, '角色管理', 1, 2, '/system/role', 'system/role/index', NULL, '0', '0', 'C', '0', '0', 'system:role:list', 'UserFilled', '2024-12-12 02:27:49', NULL, '角色管理菜单', 1, NULL, NULL);
INSERT INTO `sys_menu` VALUES (102, '菜单管理', 1, 3, '/system/menu', 'system/menu/index', NULL, '0', '0', 'C', '0', '0', 'system:menu:list', 'Menu', '2024-12-12 02:27:49', NULL, '菜单管理菜单', 1, NULL, NULL);
INSERT INTO `sys_menu` VALUES (103, '部门管理', 1, 4, '/system/dept', 'system/dept/index', NULL, '0', '0', 'C', '0', '0', 'system:dept:list', 'OfficeBuilding', '2024-12-12 02:27:49', NULL, '部门管理菜单', 1, NULL, NULL);
INSERT INTO `sys_menu` VALUES (200, '流程管理', 2, 1, '/workflow/process', 'workflow/process/index', NULL, '0', '0', 'C', '0', '0', 'workflow:process:list', 'List', '2024-12-12 02:27:49', NULL, '流程管理菜单', 1, NULL, NULL);
INSERT INTO `sys_menu` VALUES (201, '流程实例', 2, 2, '/workflow/instance', 'workflow/instance/index', NULL, '0', '0', 'C', '0', '0', 'workflow:instance:list', 'Connection', '2024-12-12 02:27:49', NULL, '流程实例菜单', 1, NULL, NULL);
INSERT INTO `sys_menu` VALUES (202, '任务管理', 2, 3, '/workflow/task', 'workflow/task/index', NULL, '0', '0', 'C', '0', '0', 'workflow:task:list', 'Tickets', '2024-12-12 02:27:49', NULL, '任务管理菜单', 1, NULL, NULL);
INSERT INTO `sys_menu` VALUES (300, '资产信息', 3, 1, '/asset/assets', 'asset/assets/index', NULL, '0', '0', 'C', '0', '0', 'asset:assets:list', 'Goods', '2024-12-12 02:27:49', NULL, '资产信息菜单', 1, NULL, NULL);
INSERT INTO `sys_menu` VALUES (301, '资产分类', 3, 2, '/asset/classification', 'asset/classification/index', NULL, '0', '0', 'C', '0', '0', 'asset:classification:list', 'Files', '2024-12-12 02:27:49', NULL, '资产分类菜单', 1, NULL, NULL);
INSERT INTO `sys_menu` VALUES (302, '资产入库', 3, 3, '/asset/storage', 'asset/storage/index', NULL, '0', '0', 'C', '0', '0', 'asset:storage:list', 'Box', '2024-12-12 02:27:49', NULL, '资产入库菜单', 1, NULL, NULL);
INSERT INTO `sys_menu` VALUES (303, '资产领用', 3, 4, '/asset/receipt', 'asset/receipt/index', NULL, '0', '0', 'C', '0', '0', 'asset:receipt:list', 'Stamp', '2024-12-12 02:27:49', NULL, '资产领用菜单', 1, NULL, NULL);
INSERT INTO `sys_menu` VALUES (304, '资产报修', 3, 5, '/asset/repair', 'asset/repair/index', NULL, '0', '0', 'C', '0', '0', 'asset:repair:list', 'Tools', '2024-12-12 02:27:49', NULL, '资产报修菜单', 1, NULL, NULL);
INSERT INTO `sys_menu` VALUES (1001, '用户查询', 100, 1, '', '', NULL, '0', '0', 'F', '0', '0', 'system:user:query', '#', '2024-12-12 02:27:49', NULL, '', 1, NULL, NULL);
INSERT INTO `sys_menu` VALUES (1002, '用户新增', 100, 2, '', '', NULL, '0', '0', 'F', '0', '0', 'system:user:add', '#', '2024-12-12 02:27:49', NULL, '', 1, NULL, NULL);
INSERT INTO `sys_menu` VALUES (1003, '用户修改', 100, 3, '', '', NULL, '0', '0', 'F', '0', '0', 'system:user:edit', '#', '2024-12-12 02:27:49', NULL, '', 1, NULL, NULL);
INSERT INTO `sys_menu` VALUES (1004, '用户删除', 100, 4, '', '', NULL, '0', '0', 'F', '0', '0', 'system:user:remove', '#', '2024-12-12 02:27:49', NULL, '', 1, NULL, NULL);
INSERT INTO `sys_menu` VALUES (1005, '用户导出', 100, 5, '', '', NULL, '0', '0', 'F', '0', '0', 'system:user:export', '#', '2024-12-12 02:27:49', NULL, '', 1, NULL, NULL);
INSERT INTO `sys_menu` VALUES (1006, '用户导入', 100, 6, '', '', NULL, '0', '0', 'F', '0', '0', 'system:user:import', '#', '2024-12-12 02:27:49', NULL, '', 1, NULL, NULL);
INSERT INTO `sys_menu` VALUES (1007, '重置密码', 100, 7, '', '', NULL, '0', '0', 'F', '0', '0', 'system:user:resetPwd', '#', '2024-12-12 02:27:49', NULL, '', 1, NULL, NULL);
INSERT INTO `sys_menu` VALUES (1008, '角色查询', 101, 1, '', '', NULL, '0', '0', 'F', '0', '0', 'system:role:query', '#', '2024-12-12 02:27:49', NULL, '', 1, NULL, NULL);
INSERT INTO `sys_menu` VALUES (1009, '角色新增', 101, 2, '', '', NULL, '0', '0', 'F', '0', '0', 'system:role:add', '#', '2024-12-12 02:27:49', NULL, '', 1, NULL, NULL);
INSERT INTO `sys_menu` VALUES (1010, '角色修改', 101, 3, '', '', NULL, '0', '0', 'F', '0', '0', 'system:role:edit', '#', '2024-12-12 02:27:49', NULL, '', 1, NULL, NULL);
INSERT INTO `sys_menu` VALUES (1011, '角色删除', 101, 4, '', '', NULL, '0', '0', 'F', '0', '0', 'system:role:remove', '#', '2024-12-12 02:27:49', NULL, '', 1, NULL, NULL);
INSERT INTO `sys_menu` VALUES (1012, '角色导出', 101, 5, '', '', NULL, '0', '0', 'F', '0', '0', 'system:role:export', '#', '2024-12-12 02:27:49', NULL, '', 1, NULL, NULL);
INSERT INTO `sys_menu` VALUES (1013, '菜单查询', 102, 1, '', '', NULL, '0', '0', 'F', '0', '0', 'system:menu:query', '#', '2024-12-12 02:27:49', NULL, '', 1, NULL, NULL);
INSERT INTO `sys_menu` VALUES (1014, '菜单新增', 102, 2, '', '', NULL, '0', '0', 'F', '0', '0', 'system:menu:add', '#', '2024-12-12 02:27:49', NULL, '', 1, NULL, NULL);
INSERT INTO `sys_menu` VALUES (1015, '菜单修改', 102, 3, '', '', NULL, '0', '0', 'F', '0', '0', 'system:menu:edit', '#', '2024-12-12 02:27:49', NULL, '', 1, NULL, NULL);
INSERT INTO `sys_menu` VALUES (1016, '菜单删除', 102, 4, '', '', NULL, '0', '0', 'F', '0', '0', 'system:menu:remove', '#', '2024-12-12 02:27:49', NULL, '', 1, NULL, NULL);
INSERT INTO `sys_menu` VALUES (2001, '流程查询', 200, 1, '', '', NULL, '0', '0', 'F', '0', '0', 'workflow:process:query', '#', '2024-12-12 02:27:49', NULL, '', 1, NULL, NULL);
INSERT INTO `sys_menu` VALUES (2002, '流程新增', 200, 2, '', '', NULL, '0', '0', 'F', '0', '0', 'workflow:process:create', '#', '2024-12-12 02:27:49', NULL, '', 1, NULL, NULL);
INSERT INTO `sys_menu` VALUES (2003, '流程修改', 200, 3, '', '', NULL, '0', '0', 'F', '0', '0', 'workflow:process:edit', '#', '2024-12-12 02:27:49', NULL, '', 1, NULL, NULL);
INSERT INTO `sys_menu` VALUES (2004, '流程删除', 200, 4, '', '', NULL, '0', '0', 'F', '0', '0', 'workflow:process:remove', '#', '2024-12-12 02:27:49', NULL, '', 1, NULL, NULL);
INSERT INTO `sys_menu` VALUES (2005, '流程部署', 200, 5, '', '', NULL, '0', '0', 'F', '0', '0', 'workflow:process:deploy', '#', '2024-12-12 02:27:49', NULL, '', 1, NULL, NULL);
INSERT INTO `sys_menu` VALUES (2006, '流程导出', 200, 6, '', '', NULL, '0', '0', 'F', '0', '0', 'workflow:process:export', '#', '2024-12-12 02:27:49', NULL, '', 1, NULL, NULL);
INSERT INTO `sys_menu` VALUES (2007, '实例查询', 201, 1, '', '', NULL, '0', '0', 'F', '0', '0', 'workflow:instance:query', '#', '2024-12-12 02:27:49', NULL, '', 1, NULL, NULL);
INSERT INTO `sys_menu` VALUES (2008, '实例终止', 201, 2, '', '', NULL, '0', '0', 'F', '0', '0', 'workflow:instance:terminate', '#', '2024-12-12 02:27:49', NULL, '', 1, NULL, NULL);
INSERT INTO `sys_menu` VALUES (2009, '实例删除', 201, 3, '', '', NULL, '0', '0', 'F', '0', '0', 'workflow:instance:remove', '#', '2024-12-12 02:27:49', NULL, '', 1, NULL, NULL);
INSERT INTO `sys_menu` VALUES (2010, '任务查询', 202, 1, '', '', NULL, '0', '0', 'F', '0', '0', 'workflow:task:query', '#', '2024-12-12 02:27:49', NULL, '', 1, NULL, NULL);
INSERT INTO `sys_menu` VALUES (2011, '任务处理', 202, 2, '', '', NULL, '0', '0', 'F', '0', '0', 'workflow:task:complete', '#', '2024-12-12 02:27:49', NULL, '', 1, NULL, NULL);
INSERT INTO `sys_menu` VALUES (2012, '任务转办', 202, 3, '', '', NULL, '0', '0', 'F', '0', '0', 'workflow:task:transfer', '#', '2024-12-12 02:27:49', NULL, '', 1, NULL, NULL);
INSERT INTO `sys_menu` VALUES (2013, '任务委派', 202, 4, '', '', NULL, '0', '0', 'F', '0', '0', 'workflow:task:delegate', '#', '2024-12-12 02:27:49', NULL, '', 1, NULL, NULL);
INSERT INTO `sys_menu` VALUES (2014, '任务退回', 202, 5, '', '', NULL, '0', '0', 'F', '0', '0', 'workflow:task:reject', '#', '2024-12-12 02:27:49', NULL, '', 1, NULL, NULL);
INSERT INTO `sys_menu` VALUES (3001, '资产信息查询', 300, 1, '', '', NULL, '0', '0', 'F', '0', '0', 'asset:assets:query', '#', '2024-12-12 02:27:49', NULL, '', 1, NULL, NULL);
INSERT INTO `sys_menu` VALUES (3002, '资产信息新增', 300, 2, '', '', NULL, '0', '0', 'F', '0', '0', 'asset:assets:add', '#', '2024-12-12 02:27:49', NULL, '', 1, NULL, NULL);
INSERT INTO `sys_menu` VALUES (3003, '资产信息修改', 300, 3, '', '', NULL, '0', '0', 'F', '0', '0', 'asset:assets:edit', '#', '2024-12-12 02:27:49', NULL, '', 1, NULL, NULL);
INSERT INTO `sys_menu` VALUES (3004, '资产信息删除', 300, 4, '', '', NULL, '0', '0', 'F', '0', '0', 'asset:assets:remove', '#', '2024-12-12 02:27:49', NULL, '', 1, NULL, NULL);
INSERT INTO `sys_menu` VALUES (3011, '资产分类查询', 301, 1, '', '', NULL, '0', '0', 'F', '0', '0', 'asset:classification:query', '#', '2024-12-12 02:27:49', NULL, '', 1, NULL, NULL);
INSERT INTO `sys_menu` VALUES (3012, '资产分类新增', 301, 2, '', '', NULL, '0', '0', 'F', '0', '0', 'asset:classification:add', '#', '2024-12-12 02:27:49', NULL, '', 1, NULL, NULL);
INSERT INTO `sys_menu` VALUES (3013, '资产分类修改', 301, 3, '', '', NULL, '0', '0', 'F', '0', '0', 'asset:classification:edit', '#', '2024-12-12 02:27:49', NULL, '', 1, NULL, NULL);
INSERT INTO `sys_menu` VALUES (3014, '资产分类删除', 301, 4, '', '', NULL, '0', '0', 'F', '0', '0', 'asset:classification:remove', '#', '2024-12-12 02:27:49', NULL, '', 1, NULL, NULL);
INSERT INTO `sys_menu` VALUES (3021, '资产入库查询', 302, 1, '', '', NULL, '0', '0', 'F', '0', '0', 'asset:storage:query', '#', '2024-12-12 02:27:49', NULL, '', 1, NULL, NULL);
INSERT INTO `sys_menu` VALUES (3022, '资产入库新增', 302, 2, '', '', NULL, '0', '0', 'F', '0', '0', 'asset:storage:add', '#', '2024-12-12 02:27:49', NULL, '', 1, NULL, NULL);
INSERT INTO `sys_menu` VALUES (3023, '资产入库修改', 302, 3, '', '', NULL, '0', '0', 'F', '0', '0', 'asset:storage:edit', '#', '2024-12-12 02:27:49', NULL, '', 1, NULL, NULL);
INSERT INTO `sys_menu` VALUES (3024, '资产入库删除', 302, 4, '', '', NULL, '0', '0', 'F', '0', '0', 'asset:storage:remove', '#', '2024-12-12 02:27:49', NULL, '', 1, NULL, NULL);
INSERT INTO `sys_menu` VALUES (3031, '资产领用查询', 303, 1, '', '', NULL, '0', '0', 'F', '0', '0', 'asset:receipt:query', '#', '2024-12-12 02:27:49', NULL, '', 1, NULL, NULL);
INSERT INTO `sys_menu` VALUES (3032, '资产领用新增', 303, 2, '', '', NULL, '0', '0', 'F', '0', '0', 'asset:receipt:add', '#', '2024-12-12 02:27:49', NULL, '', 1, NULL, NULL);
INSERT INTO `sys_menu` VALUES (3033, '资产领用修改', 303, 3, '', '', NULL, '0', '0', 'F', '0', '0', 'asset:receipt:edit', '#', '2024-12-12 02:27:49', NULL, '', 1, NULL, NULL);
INSERT INTO `sys_menu` VALUES (3034, '资产领用删除', 303, 4, '', '', NULL, '0', '0', 'F', '0', '0', 'asset:receipt:remove', '#', '2024-12-12 02:27:49', NULL, '', 1, NULL, NULL);
INSERT INTO `sys_menu` VALUES (3041, '资产报修查询', 304, 1, '', '', NULL, '0', '0', 'F', '0', '0', 'asset:repair:query', '#', '2024-12-12 02:27:49', NULL, '', 1, NULL, NULL);
INSERT INTO `sys_menu` VALUES (3042, '资产报修新增', 304, 2, '', '', NULL, '0', '0', 'F', '0', '0', 'asset:repair:add', '#', '2024-12-12 02:27:49', NULL, '', 1, NULL, NULL);
INSERT INTO `sys_menu` VALUES (3043, '资产报修修改', 304, 3, '', '', NULL, '0', '0', 'F', '0', '0', 'asset:repair:edit', '#', '2024-12-12 02:27:49', NULL, '', 1, NULL, NULL);
INSERT INTO `sys_menu` VALUES (3044, '资产报修删除', 304, 4, '', '', NULL, '0', '0', 'F', '0', '0', 'asset:repair:remove', '#', '2024-12-12 02:27:49', NULL, '', 1, NULL, NULL);
INSERT INTO `sys_menu` VALUES (104, '流程配置', 1, 5, '/system/process-config', 'system/processConfig/index', NULL, '0', '0', 'C', '0', '0', 'system:processConfig:list', 'List', NOW(), NULL, '流程配置菜单', 1, NULL, NULL);
INSERT INTO `sys_menu` VALUES (1017, '流程配置查询', 104, 1, '', '', NULL, '0', '0', 'F', '0', '0', 'system:processConfig:query', '#', NOW(), NULL, '', 1, NULL, NULL);
INSERT INTO `sys_menu` VALUES (1018, '流程配置新增', 104, 2, '', '', NULL, '0', '0', 'F', '0', '0', 'system:processConfig:add', '#', NOW(), NULL, '', 1, NULL, NULL);
INSERT INTO `sys_menu` VALUES (1019, '流程配置修改', 104, 3, '', '', NULL, '0', '0', 'F', '0', '0', 'system:processConfig:edit', '#', NOW(), NULL, '', 1, NULL, NULL);
INSERT INTO `sys_menu` VALUES (1020, '流程配置删除', 104, 4, '', '', NULL, '0', '0', 'F', '0', '0', 'system:processConfig:remove', '#', NOW(), NULL, '', 1, NULL, NULL);
INSERT INTO `sys_menu` VALUES (2015, '流程启动', 201, 4, '', '', NULL, '0', '0', 'F', '0', '0', 'workflow:instance:start', '#', NOW(), NULL, '', 1, NULL, NULL);
INSERT INTO `sys_menu` VALUES (4, '字典管理', 1, 6, '/system/dict', NULL, NULL, '0', '0', 'M', '0', '0', NULL, 'Notebook', NOW(), NULL, '字典管理目录', 1, NULL, NULL);
INSERT INTO `sys_menu` VALUES (105, '字典类型', 4, 1, '/system/dict/type', 'system/dictType/index', NULL, '0', '0', 'C', '0', '0', 'system:dict:list', 'List', NOW(), NULL, '字典类型菜单', 1, NULL, NULL);
INSERT INTO `sys_menu` VALUES (106, '字典数据', 4, 2, '/system/dict/data', 'system/dictData/index', NULL, '0', '0', 'C', '0', '0', 'system:dict:list', 'List', NOW(), NULL, '字典数据菜单', 1, NULL, NULL);
INSERT INTO `sys_menu` VALUES (1021, '字典查询', 105, 1, '', '', NULL, '0', '0', 'F', '0', '0', 'system:dict:query', '#', NOW(), NULL, '', 1, NULL, NULL);
INSERT INTO `sys_menu` VALUES (1022, '字典新增', 105, 2, '', '', NULL, '0', '0', 'F', '0', '0', 'system:dict:add', '#', NOW(), NULL, '', 1, NULL, NULL);
INSERT INTO `sys_menu` VALUES (1023, '字典修改', 105, 3, '', '', NULL, '0', '0', 'F', '0', '0', 'system:dict:edit', '#', NOW(), NULL, '', 1, NULL, NULL);
INSERT INTO `sys_menu` VALUES (1024, '字典删除', 105, 4, '', '', NULL, '0', '0', 'F', '0', '0', 'system:dict:remove', '#', NOW(), NULL, '', 1, NULL, NULL);
INSERT INTO `sys_menu` VALUES (1025, '字典导出', 105, 5, '', '', NULL, '0', '0', 'F', '0', '0', 'system:dict:export', '#', NOW(), NULL, '', 1, NULL, NULL);

-- 修正流程定义相关权限
UPDATE `sys_menu` SET 
  perms = CASE 
    WHEN menu_id = 200 THEN 'workflow:process:list'
    WHEN menu_id = 2001 THEN 'workflow:process:query'
    WHEN menu_id = 2002 THEN 'workflow:process:create'
    WHEN menu_id = 2003 THEN 'workflow:process:edit'
    WHEN menu_id = 2004 THEN 'workflow:process:remove'
    WHEN menu_id = 2005 THEN 'workflow:process:deploy'
    WHEN menu_id = 2006 THEN 'workflow:process:export'
    ELSE perms END
WHERE menu_id BETWEEN 200 AND 2006;

-- 修正流程实例相关权限
UPDATE `sys_menu` SET 
  perms = 'workflow:instance:list',
  menu_name = '流程实例',
  remark = '流程实例菜单'
WHERE menu_id = 201;

-- 修正任务管理权限
UPDATE `sys_menu` SET 
  perms = 'workflow:task:list',
  menu_name = '任务管理',
  remark = '任务管理菜单'
WHERE menu_id = 202;

-- 修正流程实例子菜单顺序
UPDATE `sys_menu` SET 
  order_num = CASE menu_id
    WHEN 2007 THEN 1  -- 实例查询
    WHEN 2008 THEN 2  -- 实例终止
    WHEN 2009 THEN 3  -- 实例删除
    WHEN 2015 THEN 4  -- 流程启动
    END
WHERE menu_id IN (2007,2008,2009,2015);

-- 修正任务管理子菜单权限标识
UPDATE `sys_menu` SET 
  perms = CASE menu_id
    WHEN 2011 THEN 'workflow:task:complete'  -- 保持与控制器@PreAuthorize一致
    WHEN 2012 THEN 'workflow:task:transfer'
    WHEN 2013 THEN 'workflow:task:delegate' 
    WHEN 2014 THEN 'workflow:task:reject'
    END
WHERE menu_id BETWEEN 2011 AND 2014;

-- 补充流程配置菜单的父级关系
UPDATE `sys_menu` SET 
  parent_id = 1,  -- 归属系统管理
  order_num = 5   -- 在部门管理之后
WHERE menu_id = 104;

-- 修正字典管理菜单层级
UPDATE `sys_menu` SET 
  parent_id = 1,  -- 归属系统管理
  order_num = 6   -- 在流程配置之后
WHERE menu_id = 4;

-- 流程管理菜单
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES 
('流程管理', 0, 5, 'process', null, 1, 'M', '0', '0', null, 'tree', 'admin', sysdate(), '', null, '流程管理目录'),
('流程定义', 1, 1, 'management', 'process/index', 1, 'C', '0', '0', 'process:list', 'tree-table', 'admin', sysdate(), '', null, '流程定义菜单'),
('流程实例', 0, 6, 'instance', 'process/Instance', 1, 'C', '0', '0', 'process:instance:list', 'example', 'admin', sysdate(), '', null, '流程实例菜单'),
('流程部署', 1, 2, 'deploy', 'process/Deploy', 1, 'C', '0', '0', 'process:deploy', 'upload', 'admin', sysdate(), '', null, '流程部署菜单');

-- 新增流程设计菜单
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, menu_type, visible, status, perms, icon, create_by, create_time)
VALUES 
('流程设计', 200, 3, 'design', 'process/Designer', 1, 'C', '0', '0', 'process:design:edit', 'edit', 'admin', sysdate());

-- 新增流程配置权限
INSERT INTO sys_menu (menu_name, parent_id, order_num, perms, menu_type, create_by, create_time)
VALUES
('流程配置查询', 104, 1, 'system:processConfig:query', 'F', 'admin', sysdate()),
('流程配置新增', 104, 2, 'system:processConfig:add', 'F', 'admin', sysdate()),
('流程配置修改', 104, 3, 'system:processConfig:edit', 'F', 'admin', sysdate()),
('流程配置删除', 104, 4, 'system:processConfig:remove', 'F', 'admin', sysdate());

-- 表单设计菜单
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, perms, icon)
VALUES 
('表单设计', 200, 4, 'form', 'process/FormDesigner', 'process:form:design', 'document', NOW());

-- 统计看板菜单
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, perms, icon)
VALUES 
('流程统计', 0, 7, 'dashboard', 'process/Dashboard', 'process:dashboard:view', 'data-analysis', NOW());

-- 催办权限
INSERT INTO sys_menu (menu_name, parent_id, order_num, perms, menu_type)
VALUES 
('任务催办', 202, 5, 'process:task:urge', 'F');

-- 流程版本管理
INSERT INTO sys_menu (menu_name, parent_id, order_num, perms, menu_type)
VALUES 
('流程版本管理', 200, 5, 'process:version:manage', 'C'),
('版本回退', 205, 1, 'process:version:rollback', 'F');

-- 流程监控权限
INSERT INTO sys_menu (menu_name, parent_id, order_num, perms, menu_type)
VALUES 
('流程监控', 0, 8, 'process:monitor:view', 'M'),
('运行中流程', 206, 1, 'process:instance:monitor', 'C');

-- 催办记录查看权限
INSERT INTO sys_menu (menu_name, parent_id, order_num, perms, menu_type)
VALUES 
('催办记录', 202, 6, 'process:urge:history', 'F');

-- 统计看板权限
UPDATE sys_menu SET 
  perms = 'process:dashboard:view'
WHERE menu_name = '流程统计';