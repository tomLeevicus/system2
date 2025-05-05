CREATE TABLE sys_process_definition_config (
  id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
  process_key VARCHAR(100) NOT NULL COMMENT '流程标识',
  process_name VARCHAR(100) NOT NULL COMMENT '流程名称',
  role_key VARCHAR(100) NOT NULL COMMENT '审批角色标识',
  form_path VARCHAR(255) COMMENT '表单路径',
  status CHAR(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
  remark VARCHAR(500) COMMENT '备注',
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  UNIQUE KEY (process_key)
) COMMENT='流程定义配置表';

-- 初始数据
INSERT INTO sys_process_definition_config (process_key, process_name, role_key, form_path, status)
VALUES 
('ASSET_APPROVAL', '资产审批流程', 'asset_approver_role', '/workflow/asset', '0'),
('LEAVE_APPROVAL', '请假审批流程', 'leave_approver_role', '/workflow/leave', '0'),
('EXPENSE_APPROVAL', '报销审批流程', 'finance_approver_role', '/workflow/expense', '0'); 