import { defineStore } from 'pinia'
import { ref } from 'vue'
import type { ProcessDefinition, ProcessInstance, Task } from '@/types/workflow'
import {
  listProcess,
  getProcessDefinition,
  getProcessInstance,
  startProcess
} from '@/api/workflow/process'
import { listTask, completeTask } from '@/api/workflow/task'

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
  async function completeTaskAction(data: {
    taskId: string
    approved: boolean
    comment: string
  }) {
    loading.value = true
    try {
      await completeTask(data)
      // 更新待办任务列表
      todoTasks.value = todoTasks.value.filter(task => task.taskId !== data.taskId)
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

    // 操作方法
    getProcessList,
    getProcessInfo,
    startNewProcess,
    getInstanceInfo,
    getTaskList,
    completeTaskAction,
    resetState
  }
}) 