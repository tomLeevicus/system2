import request from '@/utils/request'
import type { ProcessDefinition, ProcessInstance, ProcessInstanceQuery } from '@/types/workflow'

// 查询流程定义列表
export function listProcess(query?: any) {
  return request<ProcessDefinition[]>({
    url: '/workflow/process/list',
    method: 'get',
    params: query
  })
}

// 部署流程
export function deployProcess(data: FormData) {
  return request<void>({
    url: '/workflow/process/deploy',
    method: 'post',
    data,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

// 删除流程
export function deleteProcess(deploymentId: string) {
  return request<void>({
    url: `/workflow/process/${deploymentId}`,
    method: 'delete'
  })
}

// 更新流程状态
export function updateProcessState(processDefinitionId: string, action: 'suspend' | 'activate') {
  return request<void>({
    url: `/workflow/process/${processDefinitionId}/${action}`,
    method: 'put'
  })
}

// 获取流程图片
export function getProcessImage(processDefinitionId: string) {
  return request<Blob>({
    url: `/workflow/process/${processDefinitionId}/image`,
    method: 'get',
    responseType: 'blob'
  })
}

// 获取流程定义
export function getProcessDefinition(processDefinitionId: string) {
  return request<ProcessDefinition>({
    url: `/workflow/process/definition/${processDefinitionId}`,
    method: 'get'
  })
}

// 获取流程图
export function getProcessDiagram(processDefinitionId: string) {
  return request<string>({
    url: `/workflow/process/${processDefinitionId}/diagram`,
    method: 'get'
  })
}

// 获取流程XML
export function getProcessXml(processDefinitionId: string) {
  return request<string>({
    url: `/workflow/process/${processDefinitionId}/xml`,
    method: 'get'
  })
}

// 启动流程
export function startProcess(processDefinitionId: string, data: any) {
  return request<string>({
    url: `/workflow/process/${processDefinitionId}/start`,
    method: 'post',
    data
  })
}

// 获取流程实例
export function getProcessInstance(processInstanceId: string) {
  return request<ProcessInstance>({
    url: `/workflow/process/instance/${processInstanceId}`,
    method: 'get'
  })
}

// 获取实例流程图
export function getInstanceDiagram(processInstanceId: string) {
  return request<string>({
    url: `/workflow/process/instance/${processInstanceId}/diagram`,
    method: 'get'
  })
}

// 取消流程实例
export function cancelProcess(processInstanceId: string, reason: string) {
  return request<void>({
    url: `/workflow/process/instance/${processInstanceId}/cancel`,
    method: 'put',
    params: { reason }
  })
} 