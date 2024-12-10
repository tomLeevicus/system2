import { defineStore } from 'pinia'
import { ref } from 'vue'
import type { ProcessDefinition, ProcessInstance, Task } from '@/types/workflow'
import {
  listProcess,
  getProcessDefinition,
  getProcessInstance,
  startProcess,
  deployProcess,
  deleteProcess,
  updateProcessState,
  getProcessImage
} from '@/api/workflow/process'
import { listTask, completeTask, delegateTask, transferTask } from '@/api/workflow/task'
import { getFormData, saveFormData, submitForm } from '@/api/workflow/form'

export const useWorkflowStore = defineStore('workflow', () => {
  // 状态
  const processList = ref<ProcessDefinition[]>([])
  const processDetail = ref<ProcessInstance | null>(null)
  const todoTasks = ref<Task[]>([])
  const doneTasks = ref<Task[]>([])
  const loading = ref(false)

  // 获取流程定义列表
  async function getProcessList(query?: any) {
    loading.value = true
    try {
      const res = await listProcess(query)
      processList.value = res.data
      return res.data
    } finally {
      loading.value = false
    }
  }

  // 获取流程定义详情
  async function getProcessInfo(processDefinitionId: string) {
    loading.value = true
    try {
      const res = await getProcessDefinition(processDefinitionId)
      return res.data
    } finally {
      loading.value = false
    }
  }

  // 部署流程
  async function deployNewProcess(data: FormData) {
    loading.value = true
    try {
      const res = await deployProcess(data)
      return res.data
    } finally {
      loading.value = false
    }
  }

  // 删除流程定义
  async function deleteProcessDef(deploymentId: string) {
    loading.value = true
    try {
      await deleteProcess(deploymentId)
      await getProcessList()
    } finally {
      loading.value = false
    }
  }

  // 更新流程状态
  async function updateProcessStatus(processDefinitionId: string, suspended: boolean) {
    loading.value = true
    try {
      const action = suspended ? 'suspend' : 'activate'
      await updateProcessState(processDefinitionId, action)
      await getProcessList()
    } finally {
      loading.value = false
    }
  }

  // 发起流程
  async function startNewProcess(processDefinitionId: string, data: any) {
    loading.value = true
    try {
      const res = await startProcess(processDefinitionId, data)
      return res.data
    } finally {
      loading.value = false
    }
  }

  // 获取流程实例详情
  async function getInstanceInfo(processInstanceId: string) {
    loading.value = true
    try {
      const res = await getProcessInstance(processInstanceId)
      processDetail.value = res.data
      return res.data
    } finally {
      loading.value = false
    }
  }

  // 获取任务列表
  async function getTaskList(query?: any) {
    loading.value = true
    try {
      const res = await listTask({
        ...query,
        taskType: query?.taskType || 'todo'
      })
      if (query?.taskType === 'done') {
        doneTasks.value = res.data
      } else {
        todoTasks.value = res.data
      }
      return res.data
    } finally {
      loading.value = false
    }
  }

  // 完成任务
  async function completeTaskAction(taskId: string, data: any) {
    loading.value = true
    try {
      await completeTask(taskId, data)
      // 更新待办任务列表
      todoTasks.value = todoTasks.value.filter(task => task.id !== taskId)
    } finally {
      loading.value = false
    }
  }

  // 委派任务
  async function delegateTaskAction(taskId: string, userId: string) {
    loading.value = true
    try {
      await delegateTask(taskId, userId)
      await getTaskList()
    } finally {
      loading.value = false
    }
  }

  // 转办任务
  async function transferTaskAction(taskId: string, userId: string) {
    loading.value = true
    try {
      await transferTask(taskId, userId)
      await getTaskList()
    } finally {
      loading.value = false
    }
  }

  // 获取表单数据
  async function getFormDataAction(processInstanceId: string) {
    loading.value = true
    try {
      const res = await getFormData(processInstanceId)
      return res.data
    } finally {
      loading.value = false
    }
  }

  // 保存表单数据
  async function saveFormDataAction(processInstanceId: string, formData: any) {
    loading.value = true
    try {
      await saveFormData({ processInstanceId, formData })
    } finally {
      loading.value = false
    }
  }

  // 提交表单
  async function submitFormAction(data: {
    processDefinitionId: string
    businessKey: string
    formData: any
  }) {
    loading.value = true
    try {
      await submitForm(data)
    } finally {
      loading.value = false
    }
  }

  // 清除状态
  function resetState() {
    processList.value = []
    processDetail.value = null
    todoTasks.value = []
    doneTasks.value = []
  }

  return {
    // 状态
    processList,
    processDetail,
    todoTasks,
    doneTasks,
    loading,

    // 流程操作
    getProcessList,
    getProcessInfo,
    deployNewProcess,
    deleteProcessDef,
    updateProcessStatus,
    startNewProcess,
    getInstanceInfo,

    // 任务操作
    getTaskList,
    completeTaskAction,
    delegateTaskAction,
    transferTaskAction,

    // 表单操作
    getFormDataAction,
    saveFormDataAction,
    submitFormAction,

    // 状态操作
    resetState
  }
}) 