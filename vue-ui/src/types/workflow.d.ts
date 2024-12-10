// 流程定义相关类型
export interface ProcessDefinition {
  id: string
  key: string
  name: string
  version: number
  description: string
  category: string
  deploymentId: string
  resourceName: string
  diagramResourceName: string
  suspended: boolean
  startFormKey: string
  tenantId: string
}

// 流程实例相关类型
export interface ProcessInstance {
  id: string
  processDefinitionId: string
  processDefinitionKey: string
  processDefinitionName: string
  processDefinitionVersion: number
  businessKey: string
  startTime: string
  endTime: string
  duration: number
  startUserId: string
  startActivityId: string
  deleteReason: string
  superProcessInstanceId: string
  variables: Record<string, any>
  tenantId: string
  name: string
  description: string
  localizedName: string
  localizedDescription: string
}

// 任务相关类型
export interface Task {
  id: string
  name: string
  description: string
  processInstanceId: string
  processDefinitionId: string
  processDefinitionKey: string
  processDefinitionName: string
  executionId: string
  taskDefinitionKey: string
  assignee: string
  owner: string
  parentTaskId: string
  priority: number
  createTime: string
  dueDate: string
  category: string
  suspensionState: number
  tenantId: string
  formKey: string
  variables: Record<string, any>
}

// 查询参数类型
export interface ProcessDefinitionQuery {
  name?: string
  key?: string
  category?: string
  suspended?: boolean
  latest?: boolean
  startableByUser?: boolean
  pageNum?: number
  pageSize?: number
}

export interface ProcessInstanceQuery {
  processDefinitionKey?: string
  processDefinitionId?: string
  businessKey?: string
  startedBy?: string
  finished?: boolean
  unfinished?: boolean
  pageNum?: number
  pageSize?: number
}

export interface TaskQuery {
  processDefinitionKey?: string
  processInstanceId?: string
  taskName?: string
  assignee?: string
  owner?: string
  unassigned?: boolean
  delegationState?: string
  candidateUser?: string
  candidateGroup?: string
  taskType?: 'todo' | 'done'
  pageNum?: number
  pageSize?: number
}

// 表单相关类型
export interface FormDefinition {
  id: string
  key: string
  name: string
  version: number
  description: string
  formFields: FormField[]
}

export interface FormField {
  id: string
  label: string
  type: string
  defaultValue?: any
  required: boolean
  readable: boolean
  writable: boolean
  enumValues?: { id: string; name: string }[]
  validation?: {
    minLength?: number
    maxLength?: number
    min?: number
    max?: number
    pattern?: string
  }
}

export interface FormData {
  processInstanceId: string
  taskId?: string
  formKey: string
  formFields: Record<string, any>
} 