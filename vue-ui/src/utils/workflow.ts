import type { ProcessInstance, ProcessActivity } from '@/types/workflow'

/**
 * 获取流程状态标签
 * @param status 流程状态
 */
export function getProcessStatusLabel(status: string): string {
  const statusMap: { [key: string]: string } = {
    running: '运行中',
    suspended: '已挂起',
    finished: '已完成',
    canceled: '已取消',
    terminated: '已终止',
    error: '错误'
  }
  return statusMap[status] || status
}

/**
 * 获取流程状态类型（用于显示不同颜色）
 * @param status 流程状态
 */
export function getProcessStatusType(status: string): string {
  const typeMap: { [key: string]: string } = {
    running: 'primary',
    suspended: 'warning',
    finished: 'success',
    canceled: 'info',
    terminated: 'danger',
    error: 'danger'
  }
  return typeMap[status] || 'info'
}

/**
 * 获取请假类型标签
 * @param type 请假类型
 */
export function getLeaveTypeLabel(type: string): string {
  const typeMap: { [key: string]: string } = {
    personal: '事假',
    sick: '病假',
    annual: '年假',
    compensatory: '调休',
    marriage: '婚假',
    maternity: '产假',
    bereavement: '丧假'
  }
  return typeMap[type] || type
}

/**
 * 计算请假天数
 * @param startTime 开始时间
 * @param endTime 结束时间
 */
export function calculateLeaveDuration(startTime: string, endTime: string): number {
  const start = new Date(startTime).getTime()
  const end = new Date(endTime).getTime()
  const days = (end - start) / (1000 * 60 * 60 * 24)
  return Number(days.toFixed(1))
}

/**
 * 格式化流程活动列表（添加序号和状态）
 * @param activities 活动列表
 */
export function formatProcessActivities(activities: ProcessActivity[]): ProcessActivity[] {
  return activities.map((activity, index) => ({
    ...activity,
    index: index + 1,
    statusType: activity.approved ? 'success' : 'danger',
    statusLabel: activity.approved ? '同意' : '拒绝'
  }))
}

/**
 * 获取流程当前节点信息
 * @param processInstance 流程实例
 */
export function getCurrentTaskInfo(processInstance: ProcessInstance) {
  const { currentTask, status } = processInstance
  if (status !== 'running') {
    return {
      taskName: getProcessStatusLabel(status),
      statusType: getProcessStatusType(status)
    }
  }
  return {
    taskName: currentTask,
    statusType: 'primary'
  }
}

/**
 * 格式化审批意见
 * @param approved 是否同���
 * @param comment 审批意见
 */
export function formatApprovalComment(approved: boolean, comment: string): string {
  const prefix = approved ? '同意' : '拒绝'
  return comment ? `${prefix}：${comment}` : prefix
}

/**
 * 检查时间区间是否合法
 * @param startTime 开始时间
 * @param endTime 结束时间
 */
export function validateTimeRange(startTime: string, endTime: string): boolean {
  if (!startTime || !endTime) return true
  return new Date(endTime).getTime() > new Date(startTime).getTime()
}

/**
 * 计算金额（处理精度问题）
 * @param quantity 数量
 * @param unitPrice 单价
 */
export function calculateAmount(quantity: number, unitPrice: number): number {
  return Number((quantity * unitPrice).toFixed(2))
}

/**
 * 格式化流程变量
 * @param variables 流程变量
 */
export function formatProcessVariables(variables: Record<string, any>): Record<string, any> {
  const result: Record<string, any> = {}
  for (const [key, value] of Object.entries(variables)) {
    if (value instanceof Date) {
      result[key] = value.toISOString()
    } else if (typeof value === 'number') {
      result[key] = Number(value.toFixed(2))
    } else {
      result[key] = value
    }
  }
  return result
}

/**
 * 生成流程表单ID
 * @param processDefinitionKey 流程定义Key
 * @param businessKey 业务Key
 */
export function generateFormId(processDefinitionKey: string, businessKey: string): string {
  return `${processDefinitionKey}_${businessKey}_${Date.now()}`
}

/**
 * 解析流程表单ID
 * @param formId 表单ID
 */
export function parseFormId(formId: string): {
  processDefinitionKey: string
  businessKey: string
  timestamp: number
} {
  const [processDefinitionKey, businessKey, timestamp] = formId.split('_')
  return {
    processDefinitionKey,
    businessKey,
    timestamp: Number(timestamp)
  }
}

/**
 * 获取流程历史路径
 * @param activities 活动列表
 */
export function getProcessPath(activities: ProcessActivity[]): string[] {
  return activities
    .filter(activity => activity.type !== 'startEvent' && activity.type !== 'endEvent')
    .map(activity => activity.taskName)
}

/**
 * 检查是否可以撤销流程
 * @param processInstance 流程实例
 * @param currentUser 当前用户
 */
export function canCancelProcess(
  processInstance: ProcessInstance,
  currentUser: string
): boolean {
  return (
    processInstance.status === 'running' &&
    processInstance.startUser === currentUser &&
    processInstance.activities.length <= 2 // 只有开始节点和当前节点
  )
} 