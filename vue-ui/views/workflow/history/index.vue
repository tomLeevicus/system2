<template>
  <div class="app-container">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>流程历史</span>
        </div>
      </template>

      <!-- 搜索区域 -->
      <el-form :model="queryParams" ref="queryForm" :inline="true">
        <el-form-item label="流程名称" prop="processName">
          <el-input
            v-model="queryParams.processName"
            placeholder="请输入流程名称"
            clearable
            @keyup.enter="handleQuery"
          />
        </el-form-item>
        <el-form-item label="发起人" prop="startUser">
          <el-input
            v-model="queryParams.startUser"
            placeholder="请输入发起人"
            clearable
            @keyup.enter="handleQuery"
          />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="queryParams.status" placeholder="流程状态" clearable>
            <el-option label="已完成" value="finished" />
            <el-option label="已取消" value="canceled" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery">搜索</el-button>
          <el-button @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- 表格区域 -->
      <el-table
        v-loading="loading"
        :data="historyList"
        border
      >
        <el-table-column label="流程名称" align="center" prop="processName" />
        <el-table-column label="流程实例ID" align="center" prop="processInstanceId" width="300" />
        <el-table-column label="业务标题" align="center" prop="businessKey" />
        <el-table-column label="发起人" align="center" prop="startUser" />
        <el-table-column label="开始时间" align="center" prop="startTime" width="160" />
        <el-table-column label="结束时间" align="center" prop="endTime" width="160" />
        <el-table-column label="状态" align="center">
          <template #default="scope">
            <el-tag :type="getStatusType(scope.row.status)">
              {{ getStatusLabel(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" width="200">
          <template #default="scope">
            <el-button
              type="primary"
              link
              @click="handleView(scope.row)"
            >查看</el-button>
            <el-button
              type="primary"
              link
              @click="handleDetail(scope.row)"
            >详情</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页区域 -->
      <pagination
        v-show="total > 0"
        :total="total"
        v-model:page="queryParams.pageNum"
        v-model:limit="queryParams.pageSize"
        @pagination="getList"
      />
    </el-card>

    <!-- 流程图查看对话框 -->
    <el-dialog
      title="流程图"
      v-model="processDialog"
      width="80%"
      append-to-body
    >
      <div class="process-viewer">
        <img :src="processImage" alt="流程图" style="max-width: 100%;">
      </div>
    </el-dialog>

    <!-- 流程详情对话框 -->
    <el-dialog
      title="流程详情"
      v-model="detailDialog"
      width="800px"
      append-to-body
    >
      <el-timeline>
        <el-timeline-item
          v-for="(activity, index) in activityList"
          :key="index"
          :timestamp="activity.startTime"
          :type="getTimelineType(activity)"
        >
          <h4>{{ activity.activityName }}</h4>
          <p>处理人: {{ activity.assignee }}</p>
          <p v-if="activity.endTime">完成时间: {{ activity.endTime }}</p>
          <p v-if="activity.comment">审批意见: {{ activity.comment }}</p>
          <p v-if="activity.durationInMillis">耗时: {{ formatDuration(activity.durationInMillis) }}</p>
        </el-timeline-item>
      </el-timeline>
    </el-dialog>
  </div>
</template>

<script lang="ts" setup>
import { ref, reactive, onMounted } from 'vue'
import { listHistory, getHistoryImage, getHistoryDetail } from '@/api/workflow/history'

// 遮罩层
const loading = ref(false)
// 总条数
const total = ref(0)
// 历史列表
const historyList = ref([])
// 活动列表
const activityList = ref([])
// 是否显示流程图对话框
const processDialog = ref(false)
// 是否显示详情对话框
const detailDialog = ref(false)
// 流程图URL
const processImage = ref('')

// 查询参数
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  processName: undefined,
  startUser: undefined,
  status: undefined
})

/** 查询历史列表 */
function getList() {
  loading.value = true
  listHistory(queryParams).then(response => {
    historyList.value = response.rows
    total.value = response.total
    loading.value = false
  })
}

/** ��索按钮操作 */
function handleQuery() {
  queryParams.pageNum = 1
  getList()
}

/** 重置按钮操作 */
function resetQuery() {
  queryParams.processName = undefined
  queryParams.startUser = undefined
  queryParams.status = undefined
  handleQuery()
}

/** 获取状态标签 */
function getStatusLabel(status: string) {
  const statusMap: { [key: string]: string } = {
    'finished': '已完成',
    'canceled': '已取消'
  }
  return statusMap[status] || status
}

/** 获取状态类型 */
function getStatusType(status: string) {
  const typeMap: { [key: string]: string } = {
    'finished': 'success',
    'canceled': 'info'
  }
  return typeMap[status] || ''
}

/** 获取时间线类型 */
function getTimelineType(activity: any) {
  if (!activity.endTime) {
    return 'primary'
  }
  return activity.approved ? 'success' : 'danger'
}

/** 格式化持续时间 */
function formatDuration(duration: number) {
  const seconds = Math.floor(duration / 1000)
  const minutes = Math.floor(seconds / 60)
  const hours = Math.floor(minutes / 60)
  const days = Math.floor(hours / 24)

  if (days > 0) {
    return `${days}天${hours % 24}小时`
  }
  if (hours > 0) {
    return `${hours}小时${minutes % 60}分钟`
  }
  if (minutes > 0) {
    return `${minutes}分钟${seconds % 60}秒`
  }
  return `${seconds}秒`
}

/** 查看流程图 */
function handleView(row: any) {
  getHistoryImage(row.processInstanceId).then(response => {
    processImage.value = 'data:image/png;base64,' + response.data
    processDialog.value = true
  })
}

/** 查看流程详情 */
function handleDetail(row: any) {
  getHistoryDetail(row.processInstanceId).then(response => {
    activityList.value = response.data
    detailDialog.value = true
  })
}

onMounted(() => {
  getList()
})
</script>

<style lang="scss" scoped>
.app-container {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.process-viewer {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 400px;
  background: #fafafa;
  padding: 20px;
}
</style> 