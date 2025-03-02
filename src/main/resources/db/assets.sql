/*
 Navicat Premium Data Transfer

 Source Server         : localo
 Source Server Type    : MySQL
 Source Server Version : 80400 (8.4.0)
 Source Host           : localhost:3306
 Source Schema         : system2

 Target Server Type    : MySQL
 Target Server Version : 80400 (8.4.0)
 File Encoding         : 65001

 Date: 26/02/2025 23:08:24
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for assets
-- ----------------------------
DROP TABLE IF EXISTS `assets`;
CREATE TABLE `assets` (
  `id` bigint NOT NULL COMMENT 'uuid',
  `asset_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '资产编号',
  `asset_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '资产名称',
  `asset_classification_id` bigint DEFAULT NULL COMMENT '资产分类id',
  `asset_model` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '资产型号',
  `asset_purchase_time` datetime DEFAULT NULL COMMENT '资产购买时间',
  `asset_use_time` datetime DEFAULT NULL COMMENT '资产启用时间',
  `asset_user_id` bigint DEFAULT NULL COMMENT '资产负责人id',
  `asset_storage_location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '资产存放地',
  `asset_use_department_id` bigint DEFAULT NULL COMMENT '资产使用部门id',
  `asset_price_num` int DEFAULT NULL COMMENT '资产价格-数字',
  `asset_price_unit` bigint DEFAULT NULL COMMENT '资产价格-单位id',
  `asset_use_status` int DEFAULT NULL COMMENT '资产使用状态 0:未启用 1:启用中 2:弃用 3:销毁',
  `asset_status` int DEFAULT NULL COMMENT '状态 0：正常 1：删除',
  `create_by` bigint DEFAULT NULL COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint DEFAULT NULL COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `del_flag` int DEFAULT NULL COMMENT '软删除标识',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='资产信息表';

-- ----------------------------
-- Records of assets
-- ----------------------------
BEGIN;
INSERT INTO `assets` (`id`, `asset_number`, `asset_name`, `asset_classification_id`, `asset_model`, `asset_purchase_time`, `asset_use_time`, `asset_user_id`, `asset_storage_location`, `asset_use_department_id`, `asset_price_num`, `asset_price_unit`, `asset_use_status`, `asset_status`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `remark`) VALUES (1654789234567890961, 'ZC202401001', '联想ThinkPad笔记本', 1654789234567890945, 'T490', '2024-01-10 00:00:00', NULL, NULL, 'A区-01-01', 101, 6999, 1, 0, 0, 1, '2025-01-24 21:03:01', 1, '2025-01-24 21:03:01', 0, NULL);
INSERT INTO `assets` (`id`, `asset_number`, `asset_name`, `asset_classification_id`, `asset_model`, `asset_purchase_time`, `asset_use_time`, `asset_user_id`, `asset_storage_location`, `asset_use_department_id`, `asset_price_num`, `asset_price_unit`, `asset_use_status`, `asset_status`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `remark`) VALUES (1654789234567890962, 'ZC202401002', '惠普打印机', 1654789234567890945, 'M154A', '2024-01-11 00:00:00', NULL, NULL, 'A区-01-02', 102, 1299, 1, 0, 0, 1, '2025-01-24 21:03:01', 1, '2025-01-24 21:03:01', 0, NULL);
INSERT INTO `assets` (`id`, `asset_number`, `asset_name`, `asset_classification_id`, `asset_model`, `asset_purchase_time`, `asset_use_time`, `asset_user_id`, `asset_storage_location`, `asset_use_department_id`, `asset_price_num`, `asset_price_unit`, `asset_use_status`, `asset_status`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `remark`) VALUES (1654789234567890963, 'ZC202401003', '会议室投影仪', 1654789234567890945, 'CB-X05', '2024-01-12 00:00:00', NULL, NULL, 'A区-01-03', 103, 3999, 1, 0, 0, 1, '2025-01-24 21:03:01', 1, '2025-01-24 21:03:01', 0, NULL);
INSERT INTO `assets` (`id`, `asset_number`, `asset_name`, `asset_classification_id`, `asset_model`, `asset_purchase_time`, `asset_use_time`, `asset_user_id`, `asset_storage_location`, `asset_use_department_id`, `asset_price_num`, `asset_price_unit`, `asset_use_status`, `asset_status`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `remark`) VALUES (1654789234567890964, 'ZC202401004', '员工办公桌', 1654789234567890946, 'BGZ-120', '2024-01-13 00:00:00', NULL, NULL, 'B区-01-01', 101, 899, 1, 0, 0, 1, '2025-01-24 21:03:01', 1, '2025-01-24 21:03:01', 0, NULL);
INSERT INTO `assets` (`id`, `asset_number`, `asset_name`, `asset_classification_id`, `asset_model`, `asset_purchase_time`, `asset_use_time`, `asset_user_id`, `asset_storage_location`, `asset_use_department_id`, `asset_price_num`, `asset_price_unit`, `asset_use_status`, `asset_status`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `remark`) VALUES (1654789234567890965, 'ZC202401005', '人体工学椅', 1654789234567890946, 'BGY-001', '2024-01-14 00:00:00', NULL, NULL, 'B区-01-02', 102, 599, 1, 0, 0, 1, '2025-01-24 21:03:01', 1, '2025-01-24 21:03:01', 0, NULL);
INSERT INTO `assets` (`id`, `asset_number`, `asset_name`, `asset_classification_id`, `asset_model`, `asset_purchase_time`, `asset_use_time`, `asset_user_id`, `asset_storage_location`, `asset_use_department_id`, `asset_price_num`, `asset_price_unit`, `asset_use_status`, `asset_status`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `remark`) VALUES (1654789234567890966, 'ZC202401006', '文件柜', 1654789234567890946, 'WJG-180', '2024-01-15 00:00:00', NULL, NULL, 'B区-01-03', 103, 799, 1, 0, 0, 1, '2025-01-24 21:03:01', 1, '2025-01-24 21:03:01', 0, NULL);
INSERT INTO `assets` (`id`, `asset_number`, `asset_name`, `asset_classification_id`, `asset_model`, `asset_purchase_time`, `asset_use_time`, `asset_user_id`, `asset_storage_location`, `asset_use_department_id`, `asset_price_num`, `asset_price_unit`, `asset_use_status`, `asset_status`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `remark`) VALUES (1654789234567890967, 'ZC202401007', '华为交换机', 1654789234567890947, 'S5735-L24P4X', '2024-01-16 00:00:00', NULL, NULL, 'C区-01-01', 104, 4999, 1, 0, 0, 1, '2025-01-24 21:03:01', 1, '2025-01-24 21:03:01', 0, NULL);
INSERT INTO `assets` (`id`, `asset_number`, `asset_name`, `asset_classification_id`, `asset_model`, `asset_purchase_time`, `asset_use_time`, `asset_user_id`, `asset_storage_location`, `asset_use_department_id`, `asset_price_num`, `asset_price_unit`, `asset_use_status`, `asset_status`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `remark`) VALUES (1654789234567890968, 'ZC202401008', '无线AP', 1654789234567890947, 'AirEngine 5761-11', '2024-01-17 00:00:00', NULL, NULL, 'C区-01-02', 104, 899, 1, 0, 0, 1, '2025-01-24 21:03:01', 1, '2025-01-24 21:03:01', 0, NULL);
INSERT INTO `assets` (`id`, `asset_number`, `asset_name`, `asset_classification_id`, `asset_model`, `asset_purchase_time`, `asset_use_time`, `asset_user_id`, `asset_storage_location`, `asset_use_department_id`, `asset_price_num`, `asset_price_unit`, `asset_use_status`, `asset_status`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `remark`) VALUES (1654789234567890969, 'ZC202401009', '路由器', 1654789234567890947, 'AR6120', '2024-01-18 00:00:00', NULL, NULL, 'C区-01-03', 104, 2999, 1, 0, 0, 1, '2025-01-24 21:03:01', 1, '2025-01-24 21:03:01', 0, NULL);
INSERT INTO `assets` (`id`, `asset_number`, `asset_name`, `asset_classification_id`, `asset_model`, `asset_purchase_time`, `asset_use_time`, `asset_user_id`, `asset_storage_location`, `asset_use_department_id`, `asset_price_num`, `asset_price_unit`, `asset_use_status`, `asset_status`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `remark`) VALUES (1654789234567890970, 'ZC202401010', 'Windows操作系统', 1654789234567890948, 'Win11 Pro', '2024-01-19 00:00:00', NULL, NULL, 'D区-01-01', 105, 1599, 1, 0, 0, 1, '2025-01-24 21:03:01', 1, '2025-01-24 21:03:01', 0, NULL);
INSERT INTO `assets` (`id`, `asset_number`, `asset_name`, `asset_classification_id`, `asset_model`, `asset_purchase_time`, `asset_use_time`, `asset_user_id`, `asset_storage_location`, `asset_use_department_id`, `asset_price_num`, `asset_price_unit`, `asset_use_status`, `asset_status`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `remark`) VALUES (1654789234567890971, 'ZC202401011', 'Office软件', 1654789234567890948, 'Office 365', '2024-01-20 00:00:00', NULL, NULL, 'D区-01-02', 105, 999, 1, 0, 0, 1, '2025-01-24 21:03:01', 1, '2025-01-24 21:03:01', 0, NULL);
INSERT INTO `assets` (`id`, `asset_number`, `asset_name`, `asset_classification_id`, `asset_model`, `asset_purchase_time`, `asset_use_time`, `asset_user_id`, `asset_storage_location`, `asset_use_department_id`, `asset_price_num`, `asset_price_unit`, `asset_use_status`, `asset_status`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `remark`) VALUES (1654789234567890972, 'ZC202401012', '防病毒软件', 1654789234567890948, 'NOD32', '2024-01-21 00:00:00', NULL, NULL, 'D区-01-03', 105, 299, 1, 0, 0, 1, '2025-01-24 21:03:01', 1, '2025-01-24 21:03:01', 0, NULL);
INSERT INTO `assets` (`id`, `asset_number`, `asset_name`, `asset_classification_id`, `asset_model`, `asset_purchase_time`, `asset_use_time`, `asset_user_id`, `asset_storage_location`, `asset_use_department_id`, `asset_price_num`, `asset_price_unit`, `asset_use_status`, `asset_status`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `remark`) VALUES (1654789234567890973, 'ZC202401013', '饮水机', 1654789234567890949, 'YSJ-20L', '2024-01-22 00:00:00', NULL, NULL, 'E区-01-01', 106, 799, 1, 0, 0, 1, '2025-01-24 21:03:01', 1, '2025-01-24 21:03:01', 0, NULL);
INSERT INTO `assets` (`id`, `asset_number`, `asset_name`, `asset_classification_id`, `asset_model`, `asset_purchase_time`, `asset_use_time`, `asset_user_id`, `asset_storage_location`, `asset_use_department_id`, `asset_price_num`, `asset_price_unit`, `asset_use_status`, `asset_status`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `remark`) VALUES (1654789234567890974, 'ZC202401014', '空气净化器', 1654789234567890949, 'KJ-350F', '2024-01-23 00:00:00', NULL, NULL, 'E区-01-02', 106, 1299, 1, 0, 0, 1, '2025-01-24 21:03:01', 1, '2025-01-24 21:03:01', 0, NULL);
INSERT INTO `assets` (`id`, `asset_number`, `asset_name`, `asset_classification_id`, `asset_model`, `asset_purchase_time`, `asset_use_time`, `asset_user_id`, `asset_storage_location`, `asset_use_department_id`, `asset_price_num`, `asset_price_unit`, `asset_use_status`, `asset_status`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `remark`) VALUES (1654789234567890975, 'ZC202401015', '碎纸机', 1654789234567890949, 'SZJ-100', '2024-01-24 00:00:00', NULL, NULL, 'E区-01-03', 106, 499, 1, 0, 0, 1, '2025-01-24 21:03:01', 1, '2025-01-24 21:03:01', 0, NULL);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
