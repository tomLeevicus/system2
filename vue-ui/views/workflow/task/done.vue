<template>
  <div class="app-container">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>我的已办任务</span>
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
        <el-form-item label="任务名称" prop="taskName">
          <el-input
            v-model="queryParams.taskName"
            placeholder="请输入任务名称"
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
        :data="taskList"
        border
      >
        <el-table-column label="任务名称" align="center" prop="taskName" />
        <el-table-column label="流程名称" align="center" prop="processName" />
        <el-table-column label="流程发起人" align="center" prop="startUser" />
        <el-table-column label="完成时间" align="center" prop="endTime" width="160" />
        <el-table-column label="审批结果" align="center">
          <template #default="scope">
            <el-tag :type="scope.row.approved ? 'success' : 'danger'">
              {{ scope.row.approved ? '同意' : '驳回' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="审批意见" align="center" prop="comment" show-overflow-tooltip />
        <el-table-column label="操作" align="center" width="120">
          <template #default="scope">
            <el-button
              type="primary"
              link
              @click="handleView(scope.row)"
            >查看</el-button>
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
import { listDoneTask, getTaskImage } from '@/api/workflow/task'

// 遮罩层
const loading = ref(false)
// 总条数
const total = ref(0)
// 任务列表
const taskList = ref([])
// 是否显示流程图对话框
const dialogVisible = ref(false)
// 流程图URL
const processImage = ref('')

// 查询参数
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  processName: undefined,
  taskName: undefined
})

/** 查询任务列表 */
function getList() {
  loading.value = true
  listDoneTask(queryParams).then(response => {
    taskList.value = response.rows
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
  queryParams.taskName = undefined
  handleQuery()
}

/** 查看流程图 */
function handleView(row: any) {
  getTaskImage(row.taskId).then(response => {
    processImage.value = 'data:image/png;base64,' + response.data
    dialogVisible.value = true
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