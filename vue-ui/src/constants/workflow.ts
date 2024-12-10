// 流程状态
export const PROCESS_STATUS = {
  DRAFT: 'draft',
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
  HR: 'hr'
} as const

// 流程表单类型
export const FORM_TYPE = {
  LEAVE: 'leave',
  EXPENSE: 'expense',
  PURCHASE: 'purchase'
} as const

// 流程表单状态
export const FORM_STATUS = {
  DRAFT: 'draft',
  SUBMITTED: 'submitted',
  APPROVED: 'approved',
  REJECTED: 'rejected'
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

// 任务操作类型
export const TASK_ACTION = {
  APPROVE: 'approve',
  REJECT: 'reject',
  DELEGATE: 'delegate',
  TRANSFER: 'transfer'
} as const

// 流程权限
export const PROCESS_PERMISSION = {
  START: 'workflow:process:start',
  CANCEL: 'workflow:process:cancel',
  DELETE: 'workflow:process:delete',
  SUSPEND: 'workflow:process:suspend',
  ACTIVATE: 'workflow:process:activate'
} as const

// 任务权限
export const TASK_PERMISSION = {
  COMPLETE: 'workflow:task:complete',
  DELEGATE: 'workflow:task:delegate',
  TRANSFER: 'workflow:task:transfer'
} as const 