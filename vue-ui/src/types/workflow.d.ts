// 流程定义
export interface ProcessDefinition {
  id: string
  key: string
  name: string
  version: number
  description: string
  suspended: boolean
  deploymentId: string
  resourceName: string
  diagramResourceName: string
}

// 流程实例
export interface ProcessInstance {
  id: string
  name: string
  processDefinitionId: string
  processDefinitionName: string
  processDefinitionKey: string
  businessKey: string
  startUserId: string
  startTime: string
  endTime: string
  duration: number
  status: 'running' | 'suspended' | 'completed'
  currentTask?: Task
  activities?: ProcessActivity[]
}

// 任务
export interface Task {
  id: string
  name: string
  description: string
  processInstanceId: string
  processDefinitionId: string
  processDefinitionName: string
  assignee: string
  assigneeName: string
  owner: string
  ownerName: string
  startTime: string
  endTime: string
  dueDate: string
  priority: number
  suspended: boolean
  taskDefinitionKey: string
  formKey: string
  formData: Record<string, any>
}

// 流程活动
export interface ProcessActivity {
  id: string
  name: string
  type: string
  assignee: string
  startTime: string
  endTime: string
  taskName?: string
  comment?: string
  approved?: boolean
}

// 流程表单
export interface ProcessForm {
  formKey: string
  formName: string
  formData: Record<string, any>
}

// 流程变量
export interface ProcessVariable {
  name: string
  type: string
  value: any
  scope: string
  local: boolean
}

// 流程评论
export interface ProcessComment {
  id: string
  taskId: string
  userId: string
  userName: string
  message: string
  time: string
  type: string
}

// 任务查询参数
export interface TaskQuery {
  processDefinitionName?: string
  taskName?: string
  assignee?: string
  owner?: string
  candidateUser?: string
  candidateGroup?: string
  involved?: string
  finished?: boolean
  unfinished?: boolean
  processFinished?: boolean
  processUnfinished?: boolean
  pageNum?: number
  pageSize?: number
}

// 流程实例查询参数
export interface ProcessInstanceQuery {
  processDefinitionKey?: string
  processDefinitionName?: string
  businessKey?: string
  startedBy?: string
  finished?: boolean
  unfinished?: boolean
  pageNum?: number
  pageSize?: number
} 