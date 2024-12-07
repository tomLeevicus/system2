-- 字典类型表
CREATE TABLE sys_dict_type (
  dict_id          bigint(20)      NOT NULL AUTO_INCREMENT    COMMENT '字典主键',
  dict_name        varchar(100)    DEFAULT ''                 COMMENT '字典名称',
  dict_type        varchar(100)    DEFAULT ''                 COMMENT '字典类型',
  status           char(1)         DEFAULT '0'                COMMENT '状态（0正常 1停用）',
  create_by        varchar(64)     DEFAULT ''                 COMMENT '创建者',
  create_time      datetime                                   COMMENT '创建时间',
  update_by        varchar(64)     DEFAULT ''                 COMMENT '更新者',
  update_time      datetime                                   COMMENT '更新时间',
  remark           varchar(500)    DEFAULT NULL               COMMENT '备注',
  del_flag         char(1)         DEFAULT '0'                COMMENT '删除标志（0代表存在 1代表删除）',
  PRIMARY KEY (dict_id),
  UNIQUE KEY dict_type (dict_type)
) ENGINE=InnoDB AUTO_INCREMENT=100 COMMENT='字典类型表';

-- 字典数据表
CREATE TABLE sys_dict_data (
  dict_code        bigint(20)      NOT NULL AUTO_INCREMENT    COMMENT '字典编码',
  dict_sort        int(4)          DEFAULT 0                  COMMENT '字典排序',
  dict_label       varchar(100)    DEFAULT ''                 COMMENT '字典标签',
  dict_value       varchar(100)    DEFAULT ''                 COMMENT '字典键值',
  dict_type        varchar(100)    DEFAULT ''                 COMMENT '字典类型',
  css_class        varchar(100)    DEFAULT NULL               COMMENT '样式属性（其他样式扩展）',
  list_class       varchar(100)    DEFAULT NULL               COMMENT '表格回显样式',
  is_default       char(1)         DEFAULT 'N'                COMMENT '是否默认（Y是 N否）',
  status           char(1)         DEFAULT '0'                COMMENT '状态（0正常 1停用）',
  create_by        varchar(64)     DEFAULT ''                 COMMENT '创建者',
  create_time      datetime                                   COMMENT '创建时间',
  update_by        varchar(64)     DEFAULT ''                 COMMENT '更新者',
  update_time      datetime                                   COMMENT '更新时间',
  remark           varchar(500)    DEFAULT NULL               COMMENT '备注',
  del_flag         char(1)         DEFAULT '0'                COMMENT '删除标志（0代表存在 1代表删除）',
  PRIMARY KEY (dict_code)
) ENGINE=InnoDB AUTO_INCREMENT=100 COMMENT='字典数据表'; 