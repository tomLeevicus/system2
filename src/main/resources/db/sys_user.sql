/*
 Navicat Premium Data Transfer

 Source Server         : local
 Source Server Type    : MySQL
 Source Server Version : 80040
 Source Host           : localhost:3306
 Source Schema         : system2

 Target Server Type    : MySQL
 Target Server Version : 80040
 File Encoding         : 65001

 Date: 24/12/2024 00:32:12
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `user_id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `dept_id` bigint NULL DEFAULT NULL COMMENT '部门ID',
  `username` varchar(30) NOT NULL COMMENT '用户账号',
  `nickname` varchar(30) NOT NULL COMMENT '用户昵称',
  `email` varchar(50) NULL DEFAULT NULL COMMENT '用户邮箱',
  `mobile` varchar(11) NULL DEFAULT NULL COMMENT '手机号码',
  `gender` char(1) NULL DEFAULT '0' COMMENT '用户性别（0男 1女 2未知）',
  `avatar` varchar(100) NULL DEFAULT '' COMMENT '头像地址',
  `password` varchar(100) NULL DEFAULT '' COMMENT '密码',
  `status` char(1) NULL DEFAULT '0' COMMENT '帐号状态（0正常 1停用）',
  `del_flag` char(1) NULL DEFAULT '0' COMMENT '删除标志（0代表存在 1代表删除）',
  `login_ip` varchar(128) NULL DEFAULT '' COMMENT '最后登录IP',
  `login_date` datetime NULL DEFAULT NULL COMMENT '最后登录时间',
  `create_by` varchar(64) NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE INDEX `idx_username`(`username`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 100 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户信息表';

-- ----------------------------
-- Records of sys_user
-- ----------------------------
BEGIN;
INSERT INTO `sys_user` VALUES (
  1, -- user_id
  100, -- dept_id
  'admin', -- username
  '系统管理员', -- nickname
  'admin@example.com', -- email
  '15888888888', -- mobile
  '0', -- gender
  '', -- avatar
  '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', -- password (加密后的 admin123)
  '0', -- status
  '0', -- del_flag
  '127.0.0.1', -- login_ip
  NOW(), -- login_date
  'admin', -- create_by
  NOW(), -- create_time
  '', -- update_by
  NULL, -- update_time
  '管理员' -- remark
);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
