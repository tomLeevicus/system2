import { defineStore } from 'pinia'
import { ref, reactive } from 'vue'

export const useWorkflowFormStore = defineStore('workflow-form', () => {
  // 请假表单状态
  const leaveForm = reactive({
    leaveType: '',
    startTime: '',
    endTime: '',
    duration: 0.5,
    reason: ''
  })

  // 报销表单状态
  const expenseForm = reactive({
    expenseType: '',
    amount: 0,
    description: '',
    attachments: [] as string[]
  })

  // 采购表单状态
  const purchaseForm = reactive({
    itemName: '',
    quantity: 1,
    unitPrice: 0,
    totalAmount: 0,
    supplier: '',
    purpose: ''
  })

  // 表单验证规则
  const leaveRules = {
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
      { required: true, message: '请输入请假天数', trigger: 'blur' }
    ],
    reason: [
      { required: true, message: '请输入请���原因', trigger: 'blur' }
    ]
  }

  const expenseRules = {
    expenseType: [
      { required: true, message: '请选择报销类型', trigger: 'change' }
    ],
    amount: [
      { required: true, message: '请输入报销金额', trigger: 'blur' }
    ],
    description: [
      { required: true, message: '请输入报销说明', trigger: 'blur' }
    ]
  }

  const purchaseRules = {
    itemName: [
      { required: true, message: '请输入物品名称', trigger: 'blur' }
    ],
    quantity: [
      { required: true, message: '请输入数量', trigger: 'blur' }
    ],
    unitPrice: [
      { required: true, message: '请输入单价', trigger: 'blur' }
    ],
    supplier: [
      { required: true, message: '请输入供应商', trigger: 'blur' }
    ],
    purpose: [
      { required: true, message: '请输入采购用途', trigger: 'blur' }
    ]
  }

  // 重置表单
  function resetLeaveForm() {
    Object.assign(leaveForm, {
      leaveType: '',
      startTime: '',
      endTime: '',
      duration: 0.5,
      reason: ''
    })
  }

  function resetExpenseForm() {
    Object.assign(expenseForm, {
      expenseType: '',
      amount: 0,
      description: '',
      attachments: []
    })
  }

  function resetPurchaseForm() {
    Object.assign(purchaseForm, {
      itemName: '',
      quantity: 1,
      unitPrice: 0,
      totalAmount: 0,
      supplier: '',
      purpose: ''
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