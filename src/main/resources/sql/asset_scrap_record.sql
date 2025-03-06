CREATE TABLE `asset_scrap_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `scrap_id` varchar(32) NOT NULL COMMENT '报废记录编号',
  `asset_id` bigint(20) NOT NULL COMMENT '资产ID',
  `scrap_user_id` bigint(20) NOT NULL COMMENT '报废申请人ID',
  `approver_id` bigint(20) DEFAULT NULL COMMENT '审批人ID',
  `approval_comment` varchar(500) DEFAULT NULL COMMENT '审批意见',
  `scrap_time` datetime NOT NULL COMMENT '操作时间',
  
  -- 继承BaseEntity的公共字段 --
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `del_flag` int(1) NOT NULL DEFAULT '0' COMMENT '删除标志（0-存在 1-删除）',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_scrap_id` (`scrap_id`),
  KEY `idx_asset_id` (`asset_id`),
  KEY `idx_scrap_user` (`scrap_user_id`),
  KEY `idx_approver` (`approver_id`),
  KEY `idx_scrap_time` (`scrap_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='资产报废审批记录表'; 