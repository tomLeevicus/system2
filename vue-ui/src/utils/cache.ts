import { CACHE_KEY } from '@/constants/cache-keys'

// 缓存工作流表单数据
export function setFormCache(key: string, data: any): void {
  localStorage.setItem(`${CACHE_KEY.WORKFLOW_FORM}_${key}`, JSON.stringify(data))
}

export function getFormCache(key: string): any {
  const data = localStorage.getItem(`${CACHE_KEY.WORKFLOW_FORM}_${key}`)
  return data ? JSON.parse(data) : null
}

export function removeFormCache(key: string): void {
  localStorage.removeItem(`${CACHE_KEY.WORKFLOW_FORM}_${key}`)
}

// 缓存流程定义列表
export function setProcessListCache(data: any[]): void {
  sessionStorage.setItem(CACHE_KEY.PROCESS_LIST, JSON.stringify(data))
}

export function getProcessListCache(): any[] {
  const data = sessionStorage.getItem(CACHE_KEY.PROCESS_LIST)
  return data ? JSON.parse(data) : []
}

// 缓存任务列表
export function setTaskListCache(type: 'todo' | 'done', data: any[]): void {
  sessionStorage.setItem(`${CACHE_KEY.TASK_LIST}_${type}`, JSON.stringify(data))
}

export function getTaskListCache(type: 'todo' | 'done'): any[] {
  const data = sessionStorage.getItem(`${CACHE_KEY.TASK_LIST}_${type}`)
  return data ? JSON.parse(data) : []
} 