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

 Date: 01/02/2025 23:57:01
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
  PRIMARY KEY (`menu_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3045 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '菜单权限表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (1, '系统管理', 0, 1, '/system', NULL, NULL, '0', '0', 'M', '0', '0', NULL, 'Setting', '2024-12-12 02:27:49', NULL, '系统管理目录', 1, NULL);
INSERT INTO `sys_menu` VALUES (2, '工作流程', 0, 2, '/workflow', NULL, NULL, '0', '0', 'M', '0', '0', NULL, 'Guide', '2024-12-12 02:27:49', NULL, '工作流程目录', 1, NULL);
INSERT INTO `sys_menu` VALUES (3, '资产管理', 0, 3, '/asset', NULL, NULL, '0', '0', 'M', '0', '0', NULL, 'House', '2024-12-12 02:27:49', NULL, '资产管理目录', 1, NULL);
INSERT INTO `sys_menu` VALUES (100, '用户管理', 1, 1, '/system/user', 'system/user/index', NULL, '0', '0', 'C', '0', '0', 'system:user:list', 'User', '2024-12-12 02:27:49', NULL, '用户管理菜单', 1, NULL);
INSERT INTO `sys_menu` VALUES (101, '角色管理', 1, 2, '/system/role', 'system/role/index', NULL, '0', '0', 'C', '0', '0', 'system:role:list', 'UserFilled', '2024-12-12 02:27:49', NULL, '角色管理菜单', 1, NULL);
INSERT INTO `sys_menu` VALUES (102, '菜单管理', 1, 3, '/system/menu', 'system/menu/index', NULL, '0', '0', 'C', '0', '0', 'system:menu:list', 'Menu', '2024-12-12 02:27:49', NULL, '菜单管理菜单', 1, NULL);
INSERT INTO `sys_menu` VALUES (103, '部门管理', 1, 4, '/system/dept', 'system/dept/index', NULL, '0', '0', 'C', '0', '0', 'system:dept:list', 'OfficeBuilding', '2024-12-12 02:27:49', NULL, '部门管理菜单', 1, NULL);
INSERT INTO `sys_menu` VALUES (200, '流程管理', 2, 1, '/workflow/process', 'workflow/process/index', NULL, '0', '0', 'C', '0', '0', 'workflow:process:list', 'List', '2024-12-12 02:27:49', NULL, '流程管理菜单', 1, NULL);
INSERT INTO `sys_menu` VALUES (201, '流程实例', 2, 2, '/workflow/instance', 'workflow/instance/index', NULL, '0', '0', 'C', '0', '0', 'workflow:instance:list', 'Connection', '2024-12-12 02:27:49', NULL, '流程实例菜单', 1, NULL);
INSERT INTO `sys_menu` VALUES (202, '任务管理', 2, 3, '/workflow/task', 'workflow/task/index', NULL, '0', '0', 'C', '0', '0', 'workflow:task:list', 'Tickets', '2024-12-12 02:27:49', NULL, '任务管理菜单', 1, NULL);
INSERT INTO `sys_menu` VALUES (300, '资产信息', 3, 1, '/asset/assets', 'asset/assets/index', NULL, '0', '0', 'C', '0', '0', 'asset:assets:list', 'Goods', '2024-12-12 02:27:49', NULL, '资产信息菜单', 1, NULL);
INSERT INTO `sys_menu` VALUES (301, '资产分类', 3, 2, '/asset/classification', 'asset/classification/index', NULL, '0', '0', 'C', '0', '0', 'asset:classification:list', 'Files', '2024-12-12 02:27:49', NULL, '资产分类菜单', 1, NULL);
INSERT INTO `sys_menu` VALUES (302, '资产入库', 3, 3, '/asset/storage', 'asset/storage/index', NULL, '0', '0', 'C', '0', '0', 'asset:storage:list', 'Box', '2024-12-12 02:27:49', NULL, '资产入库菜单', 1, NULL);
INSERT INTO `sys_menu` VALUES (303, '资产领用', 3, 4, '/asset/receipt', 'asset/receipt/index', NULL, '0', '0', 'C', '0', '0', 'asset:receipt:list', 'Stamp', '2024-12-12 02:27:49', NULL, '资产领用菜单', 1, NULL);
INSERT INTO `sys_menu` VALUES (304, '资产报修', 3, 5, '/asset/repair', 'asset/repair/index', NULL, '0', '0', 'C', '0', '0', 'asset:repair:list', 'Tools', '2024-12-12 02:27:49', NULL, '资产报修菜单', 1, NULL);
INSERT INTO `sys_menu` VALUES (1001, '用户查询', 100, 1, '', '', NULL, '0', '0', 'F', '0', '0', 'system:user:query', '#', '2024-12-12 02:27:49', NULL, '', 1, NULL);
INSERT INTO `sys_menu` VALUES (1002, '用户新增', 100, 2, '', '', NULL, '0', '0', 'F', '0', '0', 'system:user:add', '#', '2024-12-12 02:27:49', NULL, '', 1, NULL);
INSERT INTO `sys_menu` VALUES (1003, '用户修改', 100, 3, '', '', NULL, '0', '0', 'F', '0', '0', 'system:user:edit', '#', '2024-12-12 02:27:49', NULL, '', 1, NULL);
INSERT INTO `sys_menu` VALUES (1004, '用户删除', 100, 4, '', '', NULL, '0', '0', 'F', '0', '0', 'system:user:remove', '#', '2024-12-12 02:27:49', NULL, '', 1, NULL);
INSERT INTO `sys_menu` VALUES (1005, '用户导出', 100, 5, '', '', NULL, '0', '0', 'F', '0', '0', 'system:user:export', '#', '2024-12-12 02:27:49', NULL, '', 1, NULL);
INSERT INTO `sys_menu` VALUES (1006, '用户导入', 100, 6, '', '', NULL, '0', '0', 'F', '0', '0', 'system:user:import', '#', '2024-12-12 02:27:49', NULL, '', 1, NULL);
INSERT INTO `sys_menu` VALUES (1007, '重置密码', 100, 7, '', '', NULL, '0', '0', 'F', '0', '0', 'system:user:resetPwd', '#', '2024-12-12 02:27:49', NULL, '', 1, NULL);
INSERT INTO `sys_menu` VALUES (1008, '角色查询', 101, 1, '', '', NULL, '0', '0', 'F', '0', '0', 'system:role:query', '#', '2024-12-12 02:27:49', NULL, '', 1, NULL);
INSERT INTO `sys_menu` VALUES (1009, '角色新增', 101, 2, '', '', NULL, '0', '0', 'F', '0', '0', 'system:role:add', '#', '2024-12-12 02:27:49', NULL, '', 1, NULL);
INSERT INTO `sys_menu` VALUES (1010, '角色修改', 101, 3, '', '', NULL, '0', '0', 'F', '0', '0', 'system:role:edit', '#', '2024-12-12 02:27:49', NULL, '', 1, NULL);
INSERT INTO `sys_menu` VALUES (1011, '角色删除', 101, 4, '', '', NULL, '0', '0', 'F', '0', '0', 'system:role:remove', '#', '2024-12-12 02:27:49', NULL, '', 1, NULL);
INSERT INTO `sys_menu` VALUES (1012, '角色导出', 101, 5, '', '', NULL, '0', '0', 'F', '0', '0', 'system:role:export', '#', '2024-12-12 02:27:49', NULL, '', 1, NULL);
INSERT INTO `sys_menu` VALUES (1013, '菜单查询', 102, 1, '', '', NULL, '0', '0', 'F', '0', '0', 'system:menu:query', '#', '2024-12-12 02:27:49', NULL, '', 1, NULL);
INSERT INTO `sys_menu` VALUES (1014, '菜单新增', 102, 2, '', '', NULL, '0', '0', 'F', '0', '0', 'system:menu:add', '#', '2024-12-12 02:27:49', NULL, '', 1, NULL);
INSERT INTO `sys_menu` VALUES (1015, '菜单修改', 102, 3, '', '', NULL, '0', '0', 'F', '0', '0', 'system:menu:edit', '#', '2024-12-12 02:27:49', NULL, '', 1, NULL);
INSERT INTO `sys_menu` VALUES (1016, '菜单删除', 102, 4, '', '', NULL, '0', '0', 'F', '0', '0', 'system:menu:remove', '#', '2024-12-12 02:27:49', NULL, '', 1, NULL);
INSERT INTO `sys_menu` VALUES (2001, '流程查询', 200, 1, '', '', NULL, '0', '0', 'F', '0', '0', 'workflow:process:query', '#', '2024-12-12 02:27:49', NULL, '', 1, NULL);
INSERT INTO `sys_menu` VALUES (2002, '流程新增', 200, 2, '', '', NULL, '0', '0', 'F', '0', '0', 'workflow:process:create', '#', '2024-12-12 02:27:49', NULL, '', 1, NULL);
INSERT INTO `sys_menu` VALUES (2003, '流程修改', 200, 3, '', '', NULL, '0', '0', 'F', '0', '0', 'workflow:process:edit', '#', '2024-12-12 02:27:49', NULL, '', 1, NULL);
INSERT INTO `sys_menu` VALUES (2004, '流程删除', 200, 4, '', '', NULL, '0', '0', 'F', '0', '0', 'workflow:process:remove', '#', '2024-12-12 02:27:49', NULL, '', 1, NULL);
INSERT INTO `sys_menu` VALUES (2005, '流程部署', 200, 5, '', '', NULL, '0', '0', 'F', '0', '0', 'workflow:process:deploy', '#', '2024-12-12 02:27:49', NULL, '', 1, NULL);
INSERT INTO `sys_menu` VALUES (2006, '流程挂起', 200, 6, '', NULL, NULL, '0', '0', 'F', '0', '0', 'workflow:process:suspend', '#', NOW(), NULL, '', 1, NULL);
INSERT INTO `sys_menu` VALUES (2007, '流程激活', 200, 7, '', NULL, NULL, '0', '0', 'F', '0', '0', 'workflow:process:activate', '#', NOW(), NULL, '', 1, NULL);
INSERT INTO `sys_menu` VALUES (2008, '流程导出', 200, 8, '', NULL, NULL, '0', '0', 'F', '0', '0', 'workflow:process:export', '#', NOW(), NULL, '', 1, NULL);
INSERT INTO `sys_menu` VALUES (2009, '实例查询', 201, 1, '', '', NULL, '0', '0', 'F', '0', '0', 'workflow:instance:query', '#', '2024-12-12 02:27:49', NULL, '', 1, NULL);
INSERT INTO `sys_menu` VALUES (2010, '实例终止', 201, 2, '', '', NULL, '0', '0', 'F', '0', '0', 'workflow:instance:terminate', '#', '2024-12-12 02:27:49', NULL, '', 1, NULL);
INSERT INTO `sys_menu` VALUES (2011, '实例启动', 201, 2, '', NULL, NULL, '0', '0', 'F', '0', '0', 'workflow:instance:start', '#', NOW(), NULL, '', 1, NULL);
INSERT INTO `sys_menu` VALUES (2012, '实例终止', 201, 3, '', NULL, NULL, '0', '0', 'F', '0', '0', 'workflow:instance:stop', '#', NOW(), NULL, '', 1, NULL);
INSERT INTO `sys_menu` VALUES (2013, '实例删除', 201, 4, '', NULL, NULL, '0', '0', 'F', '0', '0', 'workflow:instance:delete', '#', NOW(), NULL, '', 1, NULL);
INSERT INTO `sys_menu` VALUES (2014, '变量管理', 201, 5, '', NULL, NULL, '0', '0', 'F', '0', '0', 'workflow:instance:variable', '#', NOW(), NULL, '', 1, NULL);
INSERT INTO `sys_menu` VALUES (2015, '流程图查看', 201, 6, '', NULL, NULL, '0', '0', 'F', '0', '0', 'workflow:instance:diagram', '#', NOW(), NULL, '', 1, NULL);
INSERT INTO `sys_menu` VALUES (2016, '任务查询', 202, 1, '', '', NULL, '0', '0', 'F', '0', '0', 'workflow:task:query', '#', '2024-12-12 02:27:49', NULL, '', 1, NULL);
INSERT INTO `sys_menu` VALUES (2017, '任务处理', 202, 2, '', '', NULL, '0', '0', 'F', '0', '0', 'workflow:task:complete', '#', '2024-12-12 02:27:49', NULL, '', 1, NULL);
INSERT INTO `sys_menu` VALUES (2018, '任务转办', 202, 3, '', '', NULL, '0', '0', 'F', '0', '0', 'workflow:task:transfer', '#', '2024-12-12 02:27:49', NULL, '', 1, NULL);
INSERT INTO `sys_menu` VALUES (2019, '任务委派', 202, 4, '', NULL, NULL, '0', '0', 'F', '0', '0', 'workflow:task:delegate', '#', NOW(), NULL, '', 1, NULL);
INSERT INTO `sys_menu` VALUES (2020, '任务退回', 202, 5, '', NULL, NULL, '0', '0', 'F', '0', '0', 'workflow:task:reject', '#', NOW(), NULL, '', 1, NULL);
INSERT INTO `sys_menu` VALUES (2021, '任务查询', 202, 1, '', NULL, NULL, '0', '0', 'F', '0', '0', 'workflow:task:query', '#', NOW(), NULL, '', 1, NULL);
INSERT INTO `sys_menu` VALUES (2022, '任务处理', 202, 2, '', NULL, NULL, '0', '0', 'F', '0', '0', 'workflow:task:complete', '#', NOW(), NULL, '', 1, NULL);
INSERT INTO `sys_menu` VALUES (2023, '任务转办', 202, 3, '', NULL, NULL, '0', '0', 'F', '0', '0', 'workflow:task:transfer', '#', NOW(), NULL, '', 1, NULL);
INSERT INTO `sys_menu` VALUES (2024, '待办任务', 202, 4, '', NULL, NULL, '0', '0', 'F', '0', '0', 'workflow:task:todo', '#', NOW(), NULL, '查看个人待办任务', 1, NULL);
INSERT INTO `sys_menu` VALUES (3001, '资产信息查询', 300, 1, '', '', NULL, '0', '0', 'F', '0', '0', 'asset:assets:query', '#', '2024-12-12 02:27:49', NULL, '', 1, NULL);
INSERT INTO `sys_menu` VALUES (3002, '资产信息新增', 300, 2, '', '', NULL, '0', '0', 'F', '0', '0', 'asset:assets:add', '#', '2024-12-12 02:27:49', NULL, '', 1, NULL);
INSERT INTO `sys_menu` VALUES (3003, '资产信息修改', 300, 3, '', '', NULL, '0', '0', 'F', '0', '0', 'asset:assets:edit', '#', '2024-12-12 02:27:49', NULL, '', 1, NULL);
INSERT INTO `sys_menu` VALUES (3004, '资产信息删除', 300, 4, '', '', NULL, '0', '0', 'F', '0', '0', 'asset:assets:remove', '#', '2024-12-12 02:27:49', NULL, '', 1, NULL);
INSERT INTO `sys_menu` VALUES (3011, '资产分类查询', 301, 1, '', '', NULL, '0', '0', 'F', '0', '0', 'asset:classification:query', '#', '2024-12-12 02:27:49', NULL, '', 1, NULL);
INSERT INTO `sys_menu` VALUES (3012, '资产分类新增', 301, 2, '', '', NULL, '0', '0', 'F', '0', '0', 'asset:classification:add', '#', '2024-12-12 02:27:49', NULL, '', 1, NULL);
INSERT INTO `sys_menu` VALUES (3013, '资产分类修改', 301, 3, '', '', NULL, '0', '0', 'F', '0', '0', 'asset:classification:edit', '#', '2024-12-12 02:27:49', NULL, '', 1, NULL);
INSERT INTO `sys_menu` VALUES (3014, '资产分类删除', 301, 4, '', '', NULL, '0', '0', 'F', '0', '0', 'asset:classification:remove', '#', '2024-12-12 02:27:49', NULL, '', 1, NULL);
INSERT INTO `sys_menu` VALUES (3021, '资产入库查询', 302, 1, '', '', NULL, '0', '0', 'F', '0', '0', 'asset:storage:query', '#', '2024-12-12 02:27:49', NULL, '', 1, NULL);
INSERT INTO `sys_menu` VALUES (3022, '资产入库新增', 302, 2, '', '', NULL, '0', '0', 'F', '0', '0', 'asset:storage:add', '#', '2024-12-12 02:27:49', NULL, '', 1, NULL);
INSERT INTO `sys_menu` VALUES (3023, '资产入库修改', 302, 3, '', '', NULL, '0', '0', 'F', '0', '0', 'asset:storage:edit', '#', '2024-12-12 02:27:49', NULL, '', 1, NULL);
INSERT INTO `sys_menu` VALUES (3024, '资产入库删除', 302, 4, '', '', NULL, '0', '0', 'F', '0', '0', 'asset:storage:remove', '#', '2024-12-12 02:27:49', NULL, '', 1, NULL);
INSERT INTO `sys_menu` VALUES (3031, '资产领用查询', 303, 1, '', '', NULL, '0', '0', 'F', '0', '0', 'asset:receipt:query', '#', '2024-12-12 02:27:49', NULL, '', 1, NULL);
INSERT INTO `sys_menu` VALUES (3032, '资产领用新增', 303, 2, '', '', NULL, '0', '0', 'F', '0', '0', 'asset:receipt:add', '#', '2024-12-12 02:27:49', NULL, '', 1, NULL);
INSERT INTO `sys_menu` VALUES (3033, '资产领用修改', 303, 3, '', '', NULL, '0', '0', 'F', '0', '0', 'asset:receipt:edit', '#', '2024-12-12 02:27:49', NULL, '', 1, NULL);
INSERT INTO `sys_menu` VALUES (3034, '资产领用删除', 303, 4, '', '', NULL, '0', '0', 'F', '0', '0', 'asset:receipt:remove', '#', '2024-12-12 02:27:49', NULL, '', 1, NULL);
INSERT INTO `sys_menu` VALUES (3041, '资产报修查询', 304, 1, '', '', NULL, '0', '0', 'F', '0', '0', 'asset:repair:query', '#', '2024-12-12 02:27:49', NULL, '', 1, NULL);
INSERT INTO `sys_menu` VALUES (3042, '资产报修新增', 304, 2, '', '', NULL, '0', '0', 'F', '0', '0', 'asset:repair:add', '#', '2024-12-12 02:27:49', NULL, '', 1, NULL);
INSERT INTO `sys_menu` VALUES (3043, '资产报修修改', 304, 3, '', '', NULL, '0', '0', 'F', '0', '0', 'asset:repair:edit', '#', '2024-12-12 02:27:49', NULL, '', 1, NULL);
INSERT INTO `sys_menu` VALUES (3044, '资产报修删除', 304, 4, '', '', NULL, '0', '0', 'F', '0', '0', 'asset:repair:remove', '#', '2024-12-12 02:27:49', NULL, '', 1, NULL);

-- 删除旧流程相关菜单
DELETE FROM sys_menu WHERE menu_id BETWEEN 200 AND 202;
DELETE FROM sys_menu WHERE menu_id BETWEEN 2001 AND 2014;

-- 新增流程管理菜单
INSERT INTO `sys_menu` VALUES 
-- 流程定义管理 --
(200, '流程定义', 2, 1, '/workflow/process', 'workflow/process/index', NULL, '0', '0', 'C', '0', '0', 'workflow:process:list', 'List', NOW(), NULL, '流程定义管理菜单', 1, NULL),
(2001, '流程定义查询', 200, 1, '', NULL, NULL, '0', '0', 'F', '0', '0', 'workflow:process:query', '#', NOW(), NULL, '', 1, NULL),
(2002, '流程定义新增', 200, 2, '', NULL, NULL, '0', '0', 'F', '0', '0', 'workflow:process:add', '#', NOW(), NULL, '', 1, NULL),
(2003, '流程定义修改', 200, 3, '', NULL, NULL, '0', '0', 'F', '0', '0', 'workflow:process:edit', '#', NOW(), NULL, '', 1, NULL),
(2004, '流程定义删除', 200, 4, '', NULL, NULL, '0', '0', 'F', '0', '0', 'workflow:process:remove', '#', NOW(), NULL, '', 1, NULL),
(2005, '流程部署', 200, 5, '', NULL, NULL, '0', '0', 'F', '0', '0', 'workflow:process:deploy', '#', NOW(), NULL, '', 1, NULL),

-- 流程实例管理 --
(201, '流程实例', 2, 2, '/workflow/instance', 'workflow/instance/index', NULL, '0', '0', 'C', '0', '0', 'workflow:instance:list', 'Connection', NOW(), NULL, '流程实例管理菜单', 1, NULL),
(2011, '实例查询', 201, 1, '', NULL, NULL, '0', '0', 'F', '0', '0', 'workflow:instance:query', '#', NOW(), NULL, '', 1, NULL),
(2012, '实例启动', 201, 2, '', NULL, NULL, '0', '0', 'F', '0', '0', 'workflow:instance:start', '#', NOW(), NULL, '', 1, NULL),
(2013, '实例终止', 201, 3, '', NULL, NULL, '0', '0', 'F', '0', '0', 'workflow:instance:stop', '#', NOW(), NULL, '', 1, NULL),
(2014, '实例删除', 201, 4, '', NULL, NULL, '0', '0', 'F', '0', '0', 'workflow:instance:delete', '#', NOW(), NULL, '', 1, NULL),

-- 任务管理 --
(202, '我的任务', 2, 3, '/workflow/task', 'workflow/task/index', NULL, '0', '0', 'C', '0', '0', 'workflow:task:list', 'Tickets', NOW(), NULL, '任务管理菜单', 1, NULL),
(2021, '任务查询', 202, 1, '', NULL, NULL, '0', '0', 'F', '0', '0', 'workflow:task:query', '#', NOW(), NULL, '', 1, NULL),
(2022, '任务处理', 202, 2, '', NULL, NULL, '0', '0', 'F', '0', '0', 'workflow:task:complete', '#', NOW(), NULL, '', 1, NULL),
(2023, '任务转办', 202, 3, '', NULL, NULL, '0', '0', 'F', '0', '0', 'workflow:task:transfer', '#', NOW(), NULL, '', 1, NULL),
(2017, '待办任务', 201, 7, '', NULL, NULL, '0', '0', 'F', '0', '0', 'workflow:instance:todo', '#', NOW(), NULL, '查看待办流程实例', 1, NULL);

SET FOREIGN_KEY_CHECKS = 1;
