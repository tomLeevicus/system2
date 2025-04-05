/*
 Navicat Premium Dump SQL

 Source Server         : mac
 Source Server Type    : MySQL
 Source Server Version : 80400 (8.4.0)
 Source Host           : 192.168.2.4:3306
 Source Schema         : system2

 Target Server Type    : MySQL
 Target Server Version : 80400 (8.4.0)
 File Encoding         : 65001

 Date: 04/04/2025 12:52:21
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for act_process_definition
-- ----------------------------
DROP TABLE IF EXISTS `act_process_definition`;
CREATE TABLE `act_process_definition`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '流程定义ID',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '流程名称',
  `process_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '流程标识',
  `category` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '流程分类',
  `version` int NULL DEFAULT NULL COMMENT '版本号',
  `deployment_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '部署ID',
  `resource_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '资源名称',
  `diagram_resource_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '流程图资源名称',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '描述',
  `suspended` tinyint(1) NULL DEFAULT 0 COMMENT '是否挂起',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建者',
  `update_by` bigint NULL DEFAULT NULL COMMENT '更新者',
  `del_flag` int NULL DEFAULT NULL,
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `deploy_time` datetime NULL DEFAULT NULL COMMENT '部署时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_process_key`(`process_key` ASC) USING BTREE,
  INDEX `idx_deployment_id`(`deployment_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '流程定义表' ROW_FORMAT = DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;
