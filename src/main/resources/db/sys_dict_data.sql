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

 Date: 14/02/2025 22:36:49
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_dict_data
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_data`;
CREATE TABLE `sys_dict_data`  (
  `dict_code` bigint NOT NULL AUTO_INCREMENT COMMENT '字典编码',
  `dict_sort` int NULL DEFAULT 0 COMMENT '字典排序',
  `dict_label` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '字典标签',
  `dict_value` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '字典键值',
  `dict_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '字典类型',
  `css_class` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '样式属性（其他样式扩展）',
  `list_class` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '表格回显样式',
  `is_default` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'N' COMMENT '是否默认（Y是 N否）',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '删除标志（0代表存在 1代表删除）',
  `create_by` bigint NULL DEFAULT NULL,
  `update_by` bigint NULL DEFAULT NULL,
  PRIMARY KEY (`dict_code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '字典数据表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dict_data
-- ----------------------------
INSERT INTO `sys_dict_data` VALUES (1, 1, '男', '0', 'sys_user_sex', NULL, NULL, 'N', '0', '2024-12-10 15:38:09', NULL, '性别男', '0', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (2, 2, '女', '1', 'sys_user_sex', NULL, NULL, 'N', '0', '2024-12-10 15:38:09', NULL, '性别女', '0', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (3, 3, '未知', '2', 'sys_user_sex', NULL, NULL, 'N', '0', '2024-12-10 15:38:09', NULL, '性别未知', '0', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (4, 1, '显示', '0', 'sys_show_hide', NULL, NULL, 'N', '0', '2024-12-10 15:38:09', NULL, '显示菜单', '0', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (5, 2, '隐藏', '1', 'sys_show_hide', NULL, NULL, 'N', '0', '2024-12-10 15:38:09', NULL, '隐藏菜单', '0', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (6, 1, '正常', '0', 'sys_normal_disable', NULL, NULL, 'N', '0', '2024-12-10 15:38:09', NULL, '正常状态', '0', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (7, 2, '停用', '1', 'sys_normal_disable', NULL, NULL, 'N', '0', '2024-12-10 15:38:09', NULL, '停用状态', '0', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (8, 1, '激活', 'true', 'flowable_process_status', NULL, 'success', 'N', '0', NOW(), NULL, '流程定义已激活（布尔值）', '0', 1, NULL);
INSERT INTO `sys_dict_data` VALUES (9, 2, '挂起', 'false', 'flowable_process_status', NULL, 'danger', 'N', '0', NOW(), NULL, '流程定义已挂起（布尔值）', '0', 1, NULL);
INSERT INTO `sys_dict_data` VALUES (10, 1, '运行中', 'running', 'flowable_instance_status', NULL, 'primary', 'N', '0', NOW(), NULL, '流程实例正在运行', '0', 1, NULL);
INSERT INTO `sys_dict_data` VALUES (11, 2, '已挂起', 'suspended', 'flowable_instance_status', NULL, 'warning', 'N', '0', NOW(), NULL, '流程实例已挂起', '0', 1, NULL);
INSERT INTO `sys_dict_data` VALUES (12, 3, '已完成', 'completed', 'flowable_instance_status', NULL, 'success', 'N', '0', NOW(), NULL, '流程实例正常结束', '0', 1, NULL);
INSERT INTO `sys_dict_data` VALUES (13, 4, '已终止', 'terminated', 'flowable_instance_status', NULL, 'danger', 'N', '0', NOW(), NULL, '流程实例被终止', '0', 1, NULL);

SET FOREIGN_KEY_CHECKS = 1;
