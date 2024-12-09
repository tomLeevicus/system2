import { ElMessage } from 'element-plus'

// 工作流错误码
export const WORKFLOW_ERROR = {
  PROCESS_NOT_FOUND: 'W0001',
  TASK_NOT_FOUND: 'W0002',
  NO_PERMISSION: 'W0003',
  INVALID_STATE: 'W0004',
  DEPLOY_FAILED: 'W0005',
  START_FAILED: 'W0006',
  COMPLETE_FAILED: 'W0007'
} as const

// 错误消息
const errorMessages: Record<string, string> = {
  [WORKFLOW_ERROR.PROCESS_NOT_FOUND]: '流程不存在',
  [WORKFLOW_ERROR.TASK_NOT_FOUND]: '任务不存在',
  [WORKFLOW_ERROR.NO_PERMISSION]: '没有操作权限',
  [WORKFLOW_ERROR.INVALID_STATE]: '流程状态不正确',
  [WORKFLOW_ERROR.DEPLOY_FAILED]: '流程部署失败',
  [WORKFLOW_ERROR.START_FAILED]: '流程启动失败',
  [WORKFLOW_ERROR.COMPLETE_FAILED]: '任务完成失败'
}

// 处理工作流错误
export function handleWorkflowError(error: any): void {
  const code = error.code || 'UNKNOWN'
  const message = errorMessages[code] || error.message || '操作失败'
  ElMessage.error(message)
} 