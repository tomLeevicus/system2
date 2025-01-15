/*
 Navicat Premium Dump SQL

 Source Server         : local
 Source Server Type    : MySQL
 Source Server Version : 80040 (8.0.40-0ubuntu0.24.04.1)
 Source Host           : 192.168.80.128:3306
 Source Schema         : system2

 Target Server Type    : MySQL
 Target Server Version : 80040 (8.0.40-0ubuntu0.24.04.1)
 File Encoding         : 65001

 Date: 14/01/2025 16:45:49
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for assets
-- ----------------------------
DROP TABLE IF EXISTS `assets`;
CREATE TABLE `assets`  (
  `id` bigint NOT NULL COMMENT 'uuid',
  `asset_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '资产编号',
  `asset_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '资产名称',
  `asset_classification_id` bigint NULL DEFAULT NULL COMMENT '资产分类id',
  `asset_num` int NULL DEFAULT NULL COMMENT '资产数量',
  `asset_model` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '资产型号',
  `asset_purchase_time` datetime NULL DEFAULT NULL COMMENT '资产购买时间',
  `asset_use_time` datetime NULL DEFAULT NULL COMMENT '资产启用时间',
  `asset_user_id` bigint NULL DEFAULT NULL COMMENT '资产负责人id',
  `asset_storage_location_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '资产存放地id',
  `asset_use_department_id` bigint NULL DEFAULT NULL COMMENT '资产使用部门id',
  `asset_price_num` int NULL DEFAULT NULL COMMENT '资产价格-数字',
  `asset_price_unit` bigint NULL DEFAULT NULL COMMENT '资产价格-单位id',
  `asset_status` int NULL DEFAULT NULL COMMENT '资产使用状态 0:未启用 1:启用中 2:弃用 3:销毁',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint NULL DEFAULT NULL COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '资产信息表' ROW_FORMAT = DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;
