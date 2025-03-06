CREATE TABLE `asset_scrap` (
  `id` bigint(20) NOT NULL COMMENT '主键ID',
  `asset_id` bigint(20) NOT NULL COMMENT '资产ID',
  `asset_name` varchar(255) NOT NULL COMMENT '资产名称',
  `start_time` datetime DEFAULT NULL COMMENT '启用时间',
  `scrap_time` datetime NOT NULL COMMENT '报废时间',
  `scrap_reason` varchar(500) DEFAULT NULL COMMENT '报废理由',
  
  -- 继承BaseEntity的字段 --
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `del_flag` int(1) NOT NULL DEFAULT '0' COMMENT '删除标志（0-存在 1-删除）',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  
  PRIMARY KEY (`id`),
  KEY `idx_asset_id` (`asset_id`),
  KEY `idx_create_time` (`create_time`),
  KEY `idx_del_flag` (`del_flag`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='资产报废记录表'; 