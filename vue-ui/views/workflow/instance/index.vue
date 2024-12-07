<template>
  <div class="app-container">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>流程实例</span>
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
          <el-select v-model="queryParams.status" placeholder="实例状态" clearable>
            <el-option label="运行中" value="running" />
            <el-option label="已结束" value="finished" />
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
        :data="instanceList"
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
              type="text"
              @click="handleView(scope.row)"
            >查看</el-button>
            <el-button
              v-if="scope.row.status === 'running'"
              type="text"
              @click="handleCancel(scope.row)"
            >取消</el-button>
            <el-button
              type="text"
              @click="handleDelete(scope.row)"
            >删除</el-button>
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
      v-model="dialogVisible"
      width="80%"
      append-to-body
    >
      <div class="process-viewer">
        <img :src="processImage" alt="流程图" style="max-width: 100%;">
      </div>
    </el-dialog>
  </div>
</template>

<script lang="ts" setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { listInstance, cancelInstance, deleteInstance, getInstanceImage } from '@/api/workflow/instance'

// 遮罩层
const loading = ref(false)
// 总条数
const total = ref(0)
// 流程实例列表
const instanceList = ref([])
// 是否显示流程图对话框
const dialogVisible = ref(false)
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

/** 查询流程实例列表 */
function getList() {
  loading.value = true
  listInstance(queryParams).then(response => {
    instanceList.value = response.rows
    total.value = response.total
    loading.value = false
  })
}

/** 搜索按钮操作 */
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
    'running': '运行中',
    'finished': '已结束',
    'canceled': '已取消'
  }
  return statusMap[status] || status
}

/** 获取状态类型 */
function getStatusType(status: string) {
  const typeMap: { [key: string]: string } = {
    'running': 'primary',
    'finished': 'success',
    'canceled': 'info'
  }
  return typeMap[status] || ''
}

/** 查看流程图 */
function handleView(row: any) {
  getInstanceImage(row.processInstanceId).then(response => {
    processImage.value = 'data:image/png;base64,' + response.data
    dialogVisible.value = true
  })
}

/** 取消流程实例 */
function handleCancel(row: any) {
  ElMessageBox.confirm('确认要取消该流程实例吗?', '警告', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    return cancelInstance(row.processInstanceId)
  }).then(() => {
    getList()
    ElMessage.success('取消成功')
  })
}

/** 删除按钮操作 */
function handleDelete(row: any) {
  ElMessageBox.confirm('是否确认删除该流程实例?', '警告', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    return deleteInstance(row.processInstanceId)
  }).then(() => {
    getList()
    ElMessage.success('删除成功')
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