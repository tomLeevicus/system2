import { defineStore } from 'pinia'
import { reactive } from 'vue'
import type { FormRules } from '@/types/element'

// 请假表单状态
export interface LeaveForm {
  leaveType: string
  startTime: string
  endTime: string
  duration: number
  reason: string
  attachments?: string[]
}

// 报销表单状态
export interface ExpenseForm {
  expenseType: string
  amount: number
  description: string
  attachments: string[]
  invoices: string[]
}

// 采购表单状态
export interface PurchaseForm {
  purchaseType: string
  items: Array<{
    name: string
    quantity: number
    price: number
    total: number
  }>
  totalAmount: number
  supplier: string
  expectedDeliveryDate: string
  remark: string
}

export const useWorkflowFormStore = defineStore('workflow-form', () => {
  // 请假表单
  const leaveForm = reactive<LeaveForm>({
    leaveType: '',
    startTime: '',
    endTime: '',
    duration: 0.5,
    reason: '',
    attachments: []
  })

  // 报销表单
  const expenseForm = reactive<ExpenseForm>({
    expenseType: '',
    amount: 0,
    description: '',
    attachments: [],
    invoices: []
  })

  // 采购表单
  const purchaseForm = reactive<PurchaseForm>({
    purchaseType: '',
    items: [],
    totalAmount: 0,
    supplier: '',
    expectedDeliveryDate: '',
    remark: ''
  })

  // 表单验证规则
  const leaveRules: FormRules = {
    leaveType: [
      { required: true, message: '请选择请假类型', trigger: 'change' }
    ],
    startTime: [
      { required: true, message: '请选择开始时间', trigger: 'change' }
    ],
    endTime: [
      { required: true, message: '请选择结束时间', trigger: 'change' }
    ],
    duration: [
      { required: true, message: '请输入请假时长', trigger: 'blur' },
      { type: 'number', min: 0.5, message: '请假时长不能小于0.5天', trigger: 'blur' }
    ],
    reason: [
      { required: true, message: '请输入请假原因', trigger: 'blur' },
      { min: 10, message: '请假原因不能少于10个字符', trigger: 'blur' }
    ]
  }

  const expenseRules: FormRules = {
    expenseType: [
      { required: true, message: '请选择报销类型', trigger: 'change' }
    ],
    amount: [
      { required: true, message: '请输入报销金额', trigger: 'blur' },
      { type: 'number', min: 0, message: '报销金额必须大于0', trigger: 'blur' }
    ],
    description: [
      { required: true, message: '请输入报销说明', trigger: 'blur' }
    ],
    invoices: [
      { required: true, message: '请上传发票', trigger: 'change' }
    ]
  }

  const purchaseRules: FormRules = {
    purchaseType: [
      { required: true, message: '请选择采购��型', trigger: 'change' }
    ],
    items: [
      { required: true, message: '请添加采购项目', trigger: 'change' },
      { type: 'array', min: 1, message: '至少添加一个采购项目', trigger: 'change' }
    ],
    supplier: [
      { required: true, message: '请输入供应商', trigger: 'blur' }
    ],
    expectedDeliveryDate: [
      { required: true, message: '请选择预计交付日期', trigger: 'change' }
    ]
  }

  // 重置表单
  function resetLeaveForm() {
    Object.assign(leaveForm, {
      leaveType: '',
      startTime: '',
      endTime: '',
      duration: 0.5,
      reason: '',
      attachments: []
    })
  }

  function resetExpenseForm() {
    Object.assign(expenseForm, {
      expenseType: '',
      amount: 0,
      description: '',
      attachments: [],
      invoices: []
    })
  }

  function resetPurchaseForm() {
    Object.assign(purchaseForm, {
      purchaseType: '',
      items: [],
      totalAmount: 0,
      supplier: '',
      expectedDeliveryDate: '',
      remark: ''
    })
  }

  return {
    // 表单状态
    leaveForm,
    expenseForm,
    purchaseForm,

    // 验证规则
    leaveRules,
    expenseRules,
    purchaseRules,

    // 重置方法
    resetLeaveForm,
    resetExpenseForm,
    resetPurchaseForm
  }
}) 