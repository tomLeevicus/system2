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

 Date: 02/03/2025 13:51:47
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for asset_receipt_record
-- ----------------------------
DROP TABLE IF EXISTS `asset_receipt_record`;
CREATE TABLE `asset_receipt_record`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `receipt_id` bigint NOT NULL COMMENT '领用记录编号',
  `asset_id` bigint NOT NULL COMMENT '资产信息ID',
  `recipient_id` bigint NOT NULL COMMENT '领用人ID',
  `approver_id` bigint NULL DEFAULT NULL COMMENT '审批人ID',
  `approval_time` datetime NULL DEFAULT NULL COMMENT '审批时间',
  `apply_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '申请时间',
  `status` tinyint NOT NULL DEFAULT 0 COMMENT '状态（0-待审批 1-已批准 2-已拒绝）',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_receipt_id`(`receipt_id` ASC) USING BTREE,
  INDEX `idx_asset_id`(`asset_id` ASC) USING BTREE,
  INDEX `idx_recipient`(`recipient_id` ASC) USING BTREE,
  INDEX `idx_approver`(`approver_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1895535348254720004 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '资产领用记录表' ROW_FORMAT = DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;
