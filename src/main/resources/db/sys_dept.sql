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

 Date: 16/02/2025 01:42:32
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept`  (
  `dept_id` bigint NOT NULL AUTO_INCREMENT COMMENT '部门id',
  `parent_id` bigint NULL DEFAULT 0 COMMENT '父部门id',
  `dept_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '部门名称',
  `order_num` int NULL DEFAULT 0 COMMENT '显示顺序',
  `leader` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '负责人',
  `phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '联系电话',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '邮箱',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '部门状态（0正常 1停用）',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '删除标志（0代表存在 1代表删除）',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `create_by` bigint NULL DEFAULT NULL,
  `update_by` bigint NULL DEFAULT NULL,
  PRIMARY KEY (`dept_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 119 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '部门表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
INSERT INTO `sys_dept` VALUES (100, 0, '湛江科技学院', 1, '张校长', '13800000001', 'president@zjkj.edu.cn', '0', '0', '2025-01-08 17:34:03', NULL, '湛江科技学院总部', NULL, NULL);
INSERT INTO `sys_dept` VALUES (101, 100, '经济与金融学院', 1, '王院长', '13800000002', 'finance@zjkj.edu.cn', '0', '0', '2025-01-08 17:34:03', NULL, '经济与金融学院', NULL, NULL);
INSERT INTO `sys_dept` VALUES (102, 100, '管理学院', 2, '李院长', '13800000003', 'management@zjkj.edu.cn', '0', '0', '2025-01-08 17:34:03', NULL, '管理学院', NULL, NULL);
INSERT INTO `sys_dept` VALUES (103, 100, '会计学院', 3, '赵院长', '13800000004', 'accounting@zjkj.edu.cn', '0', '0', '2025-01-08 17:34:03', NULL, '会计学院', NULL, NULL);
INSERT INTO `sys_dept` VALUES (104, 100, '计算机工程学院', 4, '钱院长', '13800000005', 'computer@zjkj.edu.cn', '0', '0', '2025-01-08 17:34:03', NULL, '计算机工程学院', NULL, NULL);
INSERT INTO `sys_dept` VALUES (105, 100, '智能制造工程学院', 5, '孙院长', '13800000006', 'manufacture@zjkj.edu.cn', '0', '0', '2025-01-08 17:34:03', NULL, '智能制造工程学院', NULL, NULL);
INSERT INTO `sys_dept` VALUES (106, 100, '建筑工程学院', 6, '周院长', '13800000007', 'construction@zjkj.edu.cn', '0', '0', '2025-01-08 17:34:03', NULL, '建筑工程学院', NULL, NULL);
INSERT INTO `sys_dept` VALUES (107, 100, '外国语学院', 7, '吴院长', '13800000008', 'language@zjkj.edu.cn', '0', '0', '2025-01-08 17:34:03', NULL, '外国语学院', NULL, NULL);
INSERT INTO `sys_dept` VALUES (108, 100, '音乐舞蹈学院', 8, '郑院长', '13800000009', 'music@zjkj.edu.cn', '0', '0', '2025-01-08 17:34:03', NULL, '音乐舞蹈学院', NULL, NULL);
INSERT INTO `sys_dept` VALUES (109, 100, '美术与设计学院', 9, '王院长', '13800000010', 'art@zjkj.edu.cn', '0', '0', '2025-01-08 17:34:03', NULL, '美术与设计学院', NULL, NULL);
INSERT INTO `sys_dept` VALUES (110, 100, '文化传媒学院', 10, '冯院长', '13800000011', 'media@zjkj.edu.cn', '0', '0', '2025-01-08 17:34:03', NULL, '文化传媒学院', NULL, NULL);
INSERT INTO `sys_dept` VALUES (111, 100, '教育学院', 11, '陈院长', '13800000012', 'education@zjkj.edu.cn', '0', '0', '2025-01-08 17:34:03', NULL, '教育学院', NULL, NULL);
INSERT INTO `sys_dept` VALUES (112, 100, '中医药学院', 12, '楚院长', '13800000013', 'medicine@zjkj.edu.cn', '0', '0', '2025-01-08 17:34:03', NULL, '中医药学院', NULL, NULL);
INSERT INTO `sys_dept` VALUES (113, 100, '马克思主义学院', 13, '魏院长', '13800000014', 'marx@zjkj.edu.cn', '0', '0', '2025-01-08 17:34:03', NULL, '马克思主义学院', NULL, NULL);
INSERT INTO `sys_dept` VALUES (114, 100, '创新创业学院', 14, '蒋院长', '13800000015', 'innovation@zjkj.edu.cn', '0', '0', '2025-01-08 17:34:03', NULL, '创新创业学院', NULL, NULL);
INSERT INTO `sys_dept` VALUES (115, 100, '国际教育学院', 15, '沈院长', '13800000016', 'international@zjkj.edu.cn', '0', '0', '2025-01-08 17:34:03', NULL, '国际教育学院', NULL, NULL);
INSERT INTO `sys_dept` VALUES (116, 100, '继续教育学院', 16, '韩院长', '13800000017', 'continuing@zjkj.edu.cn', '0', '0', '2025-01-08 17:34:03', NULL, '继续教育学院', NULL, NULL);
INSERT INTO `sys_dept` VALUES (117, 100, '新湖校区', 17, '杨主任', '13800000018', 'xinhu@zjkj.edu.cn', '0', '0', '2025-01-08 17:34:03', NULL, '新湖校区', NULL, NULL);
INSERT INTO `sys_dept` VALUES (118, 100, '麻章校区', 18, '朱主任', '13800000019', 'mazhang@zjkj.edu.cn', '0', '0', '2025-01-08 17:34:03', NULL, '麻章校区', NULL, NULL);

SET FOREIGN_KEY_CHECKS = 1;
