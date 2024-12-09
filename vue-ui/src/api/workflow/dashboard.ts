import request from '@/utils/request'

// 获取仪表盘统计数据
export function getStatistics() {
  return request({
    url: '/workflow/dashboard/statistics',
    method: 'get'
  })
}

// 获取流程分类统计
export function getCategoryStatistics() {
  return request({
    url: '/workflow/dashboard/category',
    method: 'get'
  })
}

// 获取任务趋势统计
export function getTrendStatistics(params: {
  startDate: string
  endDate: string
}) {
  return request({
    url: '/workflow/dashboard/trend',
    method: 'get',
    params
  })
}

// 获取流程实例统计
export function getInstanceStatistics() {
  return request({
    url: '/workflow/dashboard/instance',
    method: 'get'
  })
}

// 获取任务统计
export function getTaskStatistics() {
  return request({
    url: '/workflow/dashboard/task',
    method: 'get'
  })
}

// 导出类型定义
export interface DashboardStatistics {
  todo: number
  done: number
  process: number
  instance: number
  category: Array<{
    name: string
    value: number
  }>
  trend: Array<{
    date: string
    todo: number
    done: number
  }>
} 