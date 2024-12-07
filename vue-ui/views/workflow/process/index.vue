<template>
  <div class="app-container">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>流程定义</span>
          <div class="right-menu">
            <el-upload
              class="upload-demo"
              :action="uploadUrl"
              :headers="headers"
              :before-upload="handleBeforeUpload"
              :on-success="handleSuccess"
              :show-file-list="false"
            >
              <el-button type="primary">部署流程</el-button>
            </el-upload>
          </div>
        </div>
      </template>

      <!-- 搜索区域 -->
      <el-form :model="queryParams" ref="queryForm" :inline="true">
        <el-form-item label="流程名称" prop="name">
          <el-input
            v-model="queryParams.name"
            placeholder="请输入流程名称"
            clearable
            @keyup.enter="handleQuery"
          />
        </el-form-item>
        <el-form-item label="流程标识" prop="key">
          <el-input
            v-model="queryParams.key"
            placeholder="请输入流程标识"
            clearable
            @keyup.enter="handleQuery"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery">搜索</el-button>
          <el-button @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- 表格区域 -->
      <el-table
        v-loading="loading"
        :data="processList"
        border
      >
        <el-table-column label="流程标识" align="center" prop="key" />
        <el-table-column label="流程名称" align="center" prop="name" />
        <el-table-column label="流程版本" align="center" prop="version" />
        <el-table-column label="部署时间" align="center" prop="deploymentTime" />
        <el-table-column label="状态" align="center">
          <template #default="scope">
            <el-tag :type="scope.row.suspended ? 'danger' : 'success'">
              {{ scope.row.suspended ? '已挂起' : '已激活' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" width="220">
          <template #default="scope">
            <el-button
              type="text"
              @click="handleView(scope.row)"
            >查看</el-button>
            <el-button
              type="text"
              @click="handleState(scope.row)"
            >{{ scope.row.suspended ? '激活' : '挂起' }}</el-button>
            <el-button
              type="text"
              @click="handleDelete(scope.row)"
            >删除</el-button>
          </template>
        </el-table-column>
      </el-table>
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
import { useUserStore } from '@/stores/user'
import { listProcess, deleteProcess, updateState, getProcessImage } from '@/api/workflow/process'

const userStore = useUserStore()

// 上传地址
const uploadUrl = '/api/workflow/process/deploy'
// 上传请求头
const headers = {
  Authorization: 'Bearer ' + userStore.token
}

// 遮罩层
const loading = ref(false)
// 流程列表数据
const processList = ref([])
// 是否显示流程图对话框
const dialogVisible = ref(false)
// 流程图URL
const processImage = ref('')

// 查询参数
const queryParams = reactive({
  name: undefined,
  key: undefined
})

/** 查询流程列表 */
function getList() {
  loading.value = true
  listProcess(queryParams).then(response => {
    processList.value = response.data
    loading.value = false
  })
}

/** 搜索按钮操作 */
function handleQuery() {
  getList()
}

/** 重置按钮操作 */
function resetQuery() {
  queryParams.name = undefined
  queryParams.key = undefined
  handleQuery()
}

/** 上传前校验 */
function handleBeforeUpload(file: File) {
  const isXML = file.type === 'text/xml' || file.name.endsWith('.bpmn')
  if (!isXML) {
    ElMessage.error('只能上传BPMN文件!')
    return false
  }
  return true
}

/** 上传成功回调 */
function handleSuccess(response: any) {
  if (response.code === 200) {
    ElMessage.success('部署成功')
    getList()
  } else {
    ElMessage.error(response.msg)
  }
}

/** 查看流程图 */
function handleView(row: any) {
  getProcessImage(row.id).then(response => {
    processImage.value = 'data:image/png;base64,' + response.data
    dialogVisible.value = true
  })
}

/** 修改状态 */
function handleState(row: any) {
  const state = row.suspended
  const text = state ? '激活' : '挂起'
  ElMessageBox.confirm('确认要"' + text + '""' + row.name + '"流程吗?', '警告', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    return updateState(row.id, !state)
  }).then(() => {
    ElMessage.success(text + '成功')
    getList()
  })
}

/** 删除按钮操作 */
function handleDelete(row: any) {
  ElMessageBox.confirm('是否确认删除名称为"' + row.name + '"的流程?', '警告', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    return deleteProcess(row.deploymentId)
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