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

 Date: 02/03/2025 14:45:22
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for asset_receipt
-- ----------------------------
DROP TABLE IF EXISTS `asset_receipt`;
CREATE TABLE `asset_receipt`  (
  `id` bigint NOT NULL COMMENT 'id',
  `asset_id` bigint NULL DEFAULT NULL COMMENT '资产信息id',
  `asset_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '资产名称‘',
  `receiver_id` bigint NULL DEFAULT NULL COMMENT '领用人id',
  `receiver_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '领用人名称',
  `collection_date` datetime NULL DEFAULT NULL COMMENT '领用日期',
  `instructions_for_use` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '领用说明',
  `is_long_term_use` int NULL DEFAULT NULL COMMENT '是否长期领用 1：是  0： 否',
  `return_time` datetime NULL DEFAULT NULL COMMENT '归还日期',
  `return_status` int NULL DEFAULT NULL COMMENT '归还状态 0：违规还 1：已归还',
  `review_status` int NULL DEFAULT NULL COMMENT '审核状态 0：未审核 1：审核通过 2：审核未通过',
  `create_by` bigint NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `update_by` bigint NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  `del_flag` int NULL DEFAULT NULL COMMENT '删除标志',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '资产领用' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
