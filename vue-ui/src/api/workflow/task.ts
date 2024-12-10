import request from '@/utils/request'

// 任务列表
export function listTask(query: any) {
  return request({
    url: '/workflow/task/list',
    method: 'get',
    params: query
  })
}

// 获取任务详情
export function getTask(taskId: string) {
  return request({
    url: `/workflow/task/${taskId}`,
    method: 'get'
  })
}

// 获取任务流程图
export function getTaskDiagram(taskId: string) {
  return request({
    url: `/workflow/task/${taskId}/diagram`,
    method: 'get'
  })
}

// 完成任务
export function completeTask(taskId: string, data: any) {
  return request({
    url: `/workflow/task/${taskId}/complete`,
    method: 'post',
    data
  })
}

// 委派任务
export function delegateTask(taskId: string, userId: string) {
  return request({
    url: `/workflow/task/${taskId}/delegate/${userId}`,
    method: 'put'
  })
}

// 转办任务
export function transferTask(taskId: string, userId: string) {
  return request({
    url: `/workflow/task/${taskId}/transfer/${userId}`,
    method: 'put'
  })
} 