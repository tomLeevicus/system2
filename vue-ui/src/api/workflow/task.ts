import request from '@/utils/request'
import type { Task, TaskQuery } from '@/types/workflow'

// 查询任务列表
export function listTask(params: TaskQuery) {
  return request({
    url: '/workflow/task/list',
    method: 'get',
    params
  })
}

// 获取任务详情
export function getTask(taskId: string) {
  return request({
    url: `/workflow/task/${taskId}`,
    method: 'get'
  })
}

// 完成任务
export function completeTask(data: {
  taskId: string
  variables?: Record<string, any>
  comment?: string
}) {
  return request({
    url: '/workflow/task/complete',
    method: 'post',
    data
  })
}

// 委派任务
export function delegateTask(data: {
  taskId: string
  userId: string
}) {
  return request({
    url: '/workflow/task/delegate',
    method: 'post',
    data
  })
}

// 转办任务
export function transferTask(data: {
  taskId: string
  userId: string
}) {
  return request({
    url: '/workflow/task/transfer',
    method: 'post',
    data
  })
}

// 导出类型定义
export interface TaskVO extends Task {
  processName: string
  businessKey: string
  startUser: string
  createTime: string
  dueTime: string
  priority: number
  formType: string
  formUrl: string
} 