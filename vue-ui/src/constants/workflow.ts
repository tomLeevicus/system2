// 流程状态
export const PROCESS_STATUS = {
  RUNNING: 'running',
  SUSPENDED: 'suspended',
  FINISHED: 'finished',
  CANCELED: 'canceled',
  TERMINATED: 'terminated',
  ERROR: 'error'
} as const

// 流程分类
export const PROCESS_CATEGORY = {
  OA: 'oa',
  FINANCE: 'finance',
  HR: 'hr',
  PURCHASE: 'purchase'
} as const

// 任务类型
export const TASK_TYPE = {
  TODO: 'todo',
  DONE: 'done'
} as const

// 请假类型
export const LEAVE_TYPE = {
  PERSONAL: 'personal',
  SICK: 'sick',
  ANNUAL: 'annual',
  COMPENSATORY: 'compensatory',
  MARRIAGE: 'marriage',
  MATERNITY: 'maternity',
  BEREAVEMENT: 'bereavement'
} as const

// 报销类型
export const EXPENSE_TYPE = {
  TRAVEL: 'travel',
  OFFICE: 'office',
  ENTERTAINMENT: 'entertainment',
  OTHER: 'other'
} as const

// 流程节点类型
export const NODE_TYPE = {
  START_EVENT: 'startEvent',
  END_EVENT: 'endEvent',
  USER_TASK: 'userTask',
  EXCLUSIVE_GATEWAY: 'exclusiveGateway',
  PARALLEL_GATEWAY: 'parallelGateway'
} as const

// 流程权限
export const PROCESS_PERMISSION = {
  LIST: 'workflow:process:list',
  QUERY: 'workflow:process:query',
  CREATE: 'workflow:process:create',
  UPDATE: 'workflow:process:update',
  DELETE: 'workflow:process:delete',
  DEPLOY: 'workflow:process:deploy',
  START: 'workflow:process:start'
} as const

// 任务权限
export const TASK_PERMISSION = {
  LIST: 'workflow:task:list',
  QUERY: 'workflow:task:query',
  COMPLETE: 'workflow:task:complete',
  DELEGATE: 'workflow:task:delegate',
  TRANSFER: 'workflow:task:transfer'
} as const 