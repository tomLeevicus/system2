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

 Date: 08/03/2025 23:06:31
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for asset_scrap_record
-- ----------------------------
DROP TABLE IF EXISTS `asset_scrap_record`;
CREATE TABLE `asset_scrap_record`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `scrap_id` bigint NULL DEFAULT NULL COMMENT '报废记录编号',
  `asset_id` bigint NULL DEFAULT NULL COMMENT '资产ID',
  `scrap_user_id` bigint NULL DEFAULT NULL COMMENT '报废申请人ID',
  `approver_id` bigint NULL DEFAULT NULL COMMENT '审批人ID',
  `approval_comment` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '审批意见',
  `scrap_time` datetime NULL DEFAULT NULL COMMENT '操作时间',
  `is_agreed` int NULL DEFAULT NULL COMMENT '是否同意',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` bigint NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `del_flag` int NULL DEFAULT 0 COMMENT '删除标志（0-存在 1-删除）',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uniq_scrap_id`(`scrap_id` ASC) USING BTREE,
  INDEX `idx_asset_id`(`asset_id` ASC) USING BTREE,
  INDEX `idx_scrap_user`(`scrap_user_id` ASC) USING BTREE,
  INDEX `idx_approver`(`approver_id` ASC) USING BTREE,
  INDEX `idx_scrap_time`(`scrap_time` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '资产报废审批记录表' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
