-- 流程配置表
CREATE TABLE sys_process_config (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    process_key VARCHAR(64) NOT NULL COMMENT '流程标识',
    name VARCHAR(100) NOT NULL COMMENT '流程名称',
    category VARCHAR(50) COMMENT '流程分类',
    role_key VARCHAR(100) COMMENT '审批角色标识',
    status CHAR(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
    del_flag CHAR(1) DEFAULT '0' COMMENT '删除标志（0代表存在 1代表删除）',
    create_by VARCHAR(64) COMMENT '创建者',
    create_time DATETIME COMMENT '创建时间',
    update_by VARCHAR(64) COMMENT '更新者',
    update_time DATETIME COMMENT '更新时间',
    remark VARCHAR(500) COMMENT '备注',
    UNIQUE KEY uk_process_key (process_key)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='流程配置表';

-- 流程变量配置表
CREATE TABLE sys_process_variable_config (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    process_key VARCHAR(64) NOT NULL COMMENT '流程标识',
    name VARCHAR(100) NOT NULL COMMENT '参数名称',
    label VARCHAR(100) NOT NULL COMMENT '参数标签',
    type VARCHAR(50) NOT NULL COMMENT '参数类型',
    required TINYINT(1) DEFAULT 0 COMMENT '是否必填（0否 1是）',
    default_value VARCHAR(255) COMMENT '默认值',
    validator VARCHAR(500) COMMENT '验证规则（JSON格式）',
    sort_order INT DEFAULT 0 COMMENT '排序号',
    status CHAR(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
    del_flag CHAR(1) DEFAULT '0' COMMENT '删除标志（0代表存在 1代表删除）',
    create_by VARCHAR(64) COMMENT '创建者',
    create_time DATETIME COMMENT '创建时间',
    update_by VARCHAR(64) COMMENT '更新者',
    update_time DATETIME COMMENT '更新时间',
    remark VARCHAR(500) COMMENT '备注',
    UNIQUE KEY uk_process_variable (process_key, name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='流程变量配置表';

-- 流程审批人表
CREATE TABLE sys_process_reviewer (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    process_definition_id VARCHAR(64) NOT NULL COMMENT '流程定义ID',
    reviewer_id BIGINT NOT NULL COMMENT '审核员ID',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    KEY idx_process_definition (process_definition_id),
    KEY idx_reviewer (reviewer_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='流程审批人表'; 