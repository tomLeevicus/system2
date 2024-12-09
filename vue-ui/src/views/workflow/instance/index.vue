<template>
  <div class="app-container">
    <!-- 搜索区域 -->
    <el-form :model="queryParams" ref="queryRef" :inline="true">
      <el-form-item label="流程名称" prop="processName">
        <el-input v-model="queryParams.processName" placeholder="请输入流程名称" clearable />
      </el-form-item>
      <el-form-item label="发起人" prop="startUser">
        <el-input v-model="queryParams.startUser" placeholder="请输入发起人" clearable />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="流程状态" clearable>
          <el-option label="运行中" value="running" />
          <el-option label="已挂起" value="suspended" />
          <el-option label="已完成" value="finished" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleQuery">搜索</el-button>
        <el-button @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 流程实例列表 -->
    <el-table v-loading="loading" :data="instanceList">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="流程实例ID" align="center" prop="processInstanceId" width="180" />
      <el-table-column label="流程名称" align="center" prop="processName" :show-overflow-tooltip="true" />
      <el-table-column label="流程版本" align="center" prop="processVersion" width="80" />
      <el-table-column label="发起人" align="center" prop="startUser" />
      <el-table-column label="开始时间" align="center" prop="startTime" width="180" />
      <el-table-column label="结束时间" align="center" prop="endTime" width="180" />
      <el-table-column label="状态" align="center" width="100">
        <template #default="scope">
          <el-tag :type="getStatusType(scope.row.status)">
            {{ getStatusLabel(scope.row.status) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="280">
        <template #default="scope">
          <el-button type="primary" link @click="handleDetail(scope.row)">详情</el-button>
          <el-button 
            type="primary" 
            link 
            @click="handleSuspend(scope.row)"
            v-if="scope.row.status === 'running'"
          >挂起</el-button>
          <el-button 
            type="primary" 
            link 
            @click="handleActivate(scope.row)"
            v-if="scope.row.status === 'suspended'"
          >激活</el-button>
          <el-button 
            type="danger" 
            link 
            @click="handleDelete(scope.row)"
            v-if="scope.row.status !== 'running'"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页组件 -->
    <el-pagination
      v-model:current-page="queryParams.pageNum"
      v-model:page-size="queryParams.pageSize"
      :total="total"
      layout="total, sizes, prev, pager, next, jumper"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
    />

    <!-- 流程实例详情对话框 -->
    <el-dialog title="流程实例详情" v-model="detailOpen" width="1000px" append-to-body>
      <el-tabs v-model="activeTab">
        <el-tab-pane label="流程信息" name="info">
          <el-descriptions :column="2" border>
            <el-descriptions-item label="流程实例ID">{{ instanceDetail.processInstanceId }}</el-descriptions-item>
            <el-descriptions-item label="流程定义ID">{{ instanceDetail.processDefinitionId }}</el-descriptions-item>
            <el-descriptions-item label="流程名称">{{ instanceDetail.processName }}</el-descriptions-item>
            <el-descriptions-item label="流程版本">{{ instanceDetail.processVersion }}</el-descriptions-item>
            <el-descriptions-item label="发起人">{{ instanceDetail.startUser }}</el-descriptions-item>
            <el-descriptions-item label="当前节点">{{ instanceDetail.currentTask }}</el-descriptions-item>
            <el-descriptions-item label="开始时间">{{ instanceDetail.startTime }}</el-descriptions-item>
            <el-descriptions-item label="结束时间">{{ instanceDetail.endTime }}</el-descriptions-item>
            <el-descriptions-item label="状态">
              <el-tag :type="getStatusType(instanceDetail.status)">
                {{ getStatusLabel(instanceDetail.status) }}
              </el-tag>
            </el-descriptions-item>
          </el-descriptions>
        </el-tab-pane>
        <el-tab-pane label="流程图" name="diagram">
          <div class="process-image">
            <img :src="processImage" alt="流程图" style="max-width: 100%;" />
          </div>
        </el-tab-pane>
        <el-tab-pane label="审批历史" name="history">
          <el-timeline>
            <el-timeline-item
              v-for="(activity, index) in instanceDetail.activities"
              :key="index"
              :timestamp="activity.time"
            >
              <h4>{{ activity.taskName }}</h4>
              <p>处理人：{{ activity.assignee }}</p>
              <p>处理结果：{{ activity.result }}</p>
              <p>处理意见：{{ activity.comment }}</p>
            </el-timeline-item>
          </el-timeline>
        </el-tab-pane>
      </el-tabs>
    </el-dialog>
  </div>
</template>

<script lang="ts" setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  listInstance,
  getInstance,
  deleteInstance,
  suspendInstance,
  activateInstance,
  getInstanceDiagram
} from '@/api/workflow/instance'

const loading = ref(false)
const instanceList = ref([])
const total = ref(0)
const detailOpen = ref(false)
const activeTab = ref('info')
const processImage = ref('')

// 查询参数
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  processName: '',
  startUser: '',
  status: undefined
})

// 实例详情
const instanceDetail = reactive({
  processInstanceId: '',
  processDefinitionId: '',
  processName: '',
  processVersion: '',
  startUser: '',
  currentTask: '',
  startTime: '',
  endTime: '',
  status: '',
  activities: []
})

// 获���状态标签
const getStatusLabel = (status: string) => {
  const statusMap: { [key: string]: string } = {
    running: '运行中',
    suspended: '已挂起',
    finished: '已完成'
  }
  return statusMap[status] || status
}

// 获取状态类型
const getStatusType = (status: string) => {
  const typeMap: { [key: string]: string } = {
    running: 'primary',
    suspended: 'warning',
    finished: 'success'
  }
  return typeMap[status] || 'info'
}

// 查询流程实例列表
const getList = async () => {
  loading.value = true
  try {
    const res = await listInstance(queryParams)
    instanceList.value = res.data.records
    total.value = res.data.total
  } finally {
    loading.value = false
  }
}

// 搜索按钮操作
const handleQuery = () => {
  queryParams.pageNum = 1
  getList()
}

// 重置按钮操作
const resetQuery = () => {
  queryParams.processName = ''
  queryParams.startUser = ''
  queryParams.status = undefined
  handleQuery()
}

// 查看详情按钮操作
const handleDetail = async (row: any) => {
  try {
    const [detailRes, imageRes] = await Promise.all([
      getInstance(row.processInstanceId),
      getInstanceDiagram(row.processInstanceId)
    ])
    Object.assign(instanceDetail, detailRes.data)
    processImage.value = 'data:image/png;base64,' + imageRes.data
    detailOpen.value = true
    activeTab.value = 'info'
  } catch (error) {
    console.error(error)
  }
}

// 挂起按钮操作
const handleSuspend = (row: any) => {
  ElMessageBox.confirm('确认要挂起该流程实例吗？', '警告', {
    type: 'warning'
  }).then(async () => {
    await suspendInstance(row.processInstanceId)
    getList()
    ElMessage.success('挂起成功')
  })
}

// 激活按钮操作
const handleActivate = (row: any) => {
  ElMessageBox.confirm('确认要激活该流程实例吗？', '警告', {
    type: 'warning'
  }).then(async () => {
    await activateInstance(row.processInstanceId)
    getList()
    ElMessage.success('激活成功')
  })
}

// 删除按钮操作
const handleDelete = (row: any) => {
  ElMessageBox.confirm('确认要删除该流程实例吗？', '警告', {
    type: 'warning'
  }).then(async () => {
    await deleteInstance(row.processInstanceId)
    getList()
    ElMessage.success('删除成功')
  })
}

// 分页大小改变
const handleSizeChange = (val: number) => {
  queryParams.pageSize = val
  getList()
}

// 分页页码改变
const handleCurrentChange = (val: number) => {
  queryParams.pageNum = val
  getList()
}

onMounted(() => {
  getList()
})
</script>

<style lang="scss" scoped>
.process-image {
  text-align: center;
  margin: 20px 0;
}

.el-timeline {
  margin: 20px;
  max-height: 400px;
  overflow-y: auto;
}
</style> 