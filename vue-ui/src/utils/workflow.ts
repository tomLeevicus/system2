import type { ProcessInstance } from '@/types/workflow'

/**
 * 获取流程状态标签
 */
export function getProcessStatusLabel(status: string) {
  const statusMap: Record<string, string> = {
    running: '进行中',
    completed: '已完成',
    suspended: '已暂停',
    terminated: '已终止',
    deleted: '已删除'
  }
  return statusMap[status] || status
}

/**
 * 获取当前任务信息
 */
export function getCurrentTaskInfo(instance: ProcessInstance) {
  const status = instance.endTime ? 'completed' : 'running'
  const taskName = status === 'running' ? '进行中' : '已完成'
  
  return {
    taskName,
    assignee: instance.startUserId,
    createTime: instance.startTime
  }
}

/**
 * 检查是否可以取消流程
 */
export function canCancelProcess(
  instance: ProcessInstance,
  currentUser: string
): boolean {
  // 只有流程发起人可以在流程开始时取消
  return (
    !instance.endTime && // 未结束
    instance.startUserId === currentUser // 是发起人
  )
} 