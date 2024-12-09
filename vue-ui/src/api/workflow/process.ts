import request from '@/utils/request'
import type { ProcessDefinition, ProcessInstance, ProcessInstanceQuery } from '@/types/workflow'

// 查询流程定义列表
export function listProcessDefinition(params?: any) {
  return request({
    url: '/workflow/process/definition/list',
    method: 'get',
    params
  })
}

// 获取流程定义XML
export function getProcessXml(processDefinitionId: string) {
  return request({
    url: `/workflow/process/definition/${processDefinitionId}/xml`,
    method: 'get'
  })
}

// 启动流程实例
export function startProcess(data: {
  processDefinitionId: string
  businessKey?: string
  variables?: Record<string, any>
}) {
  return request({
    url: '/workflow/process/instance/start',
    method: 'post',
    data
  })
}

// 查询流程实例列表
export function listProcessInstance(params: ProcessInstanceQuery) {
  return request({
    url: '/workflow/process/instance/list',
    method: 'get',
    params
  })
}

// 获取流程实例详情
export function getProcessInstance(processInstanceId: string) {
  return request({
    url: `/workflow/process/instance/${processInstanceId}`,
    method: 'get'
  })
}

// 删除流程实例
export function deleteProcess(processInstanceId: string) {
  return request({
    url: `/workflow/process/instance/${processInstanceId}`,
    method: 'delete'
  })
}

// 更新流程实例状态
export function updateProcessState(processInstanceId: string, action: 'suspend' | 'activate') {
  return request({
    url: `/workflow/process/instance/${processInstanceId}/${action}`,
    method: 'put'
  })
}

// 导出类型定义
export interface ProcessDefinitionQuery {
  name?: string
  key?: string
  category?: string
  suspended?: boolean
  pageNum?: number
  pageSize?: number
}

export interface ProcessDefinitionVO extends ProcessDefinition {
  deploymentTime: string
  suspended: boolean
}

export interface ProcessInstanceVO extends ProcessInstance {
  processName: string
  businessKey: string
  startUser: string
  startTime: string
  endTime: string
  duration: number
  status: string
} 