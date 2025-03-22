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

 Date: 22/03/2025 21:28:04
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for asset_scrap
-- ----------------------------
DROP TABLE IF EXISTS `asset_scrap`;
CREATE TABLE `asset_scrap`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `asset_id` bigint NOT NULL COMMENT '资产ID',
  `asset_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '资产名称',
  `start_time` datetime NOT NULL COMMENT '启用时间',
  `scrap_time` datetime NOT NULL COMMENT '报废时间',
  `scrap_reason` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '报废理由',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` bigint NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `del_flag` int NULL DEFAULT 0 COMMENT '删除标志（0-存在，1-删除）',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '状态 0待审核 1审核通过 2审核不通过',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_asset_id`(`asset_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1903294642400194562 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '资产报废记录表' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
