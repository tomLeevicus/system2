<template>
  <div class="app-container">
    <!-- 搜索区域 -->
    <el-form :model="queryParams" ref="queryRef" :inline="true">
      <el-form-item label="任务名称" prop="taskName">
        <el-input v-model="queryParams.taskName" placeholder="请输入任务名称" clearable />
      </el-form-item>
      <el-form-item label="流程名称" prop="processName">
        <el-input v-model="queryParams.processName" placeholder="请输入流程名称" clearable />
      </el-form-item>
      <el-form-item label="任务类型" prop="taskType">
        <el-select v-model="queryParams.taskType" placeholder="任务类型" clearable>
          <el-option label="待办任务" value="todo" />
          <el-option label="已办任务" value="done" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleQuery">搜索</el-button>
        <el-button @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 任务列表 -->
    <el-table v-loading="loading" :data="taskList">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="任务编号" align="center" prop="taskId" width="180" />
      <el-table-column label="任务名称" align="center" prop="taskName" :show-overflow-tooltip="true" />
      <el-table-column label="流程名称" align="center" prop="processName" :show-overflow-tooltip="true" />
      <el-table-column label="流程发起人" align="center" prop="startUser" />
      <el-table-column label="任务创建时间" align="center" prop="createTime" width="180" />
      <el-table-column label="任务类型" align="center" width="100">
        <template #default="scope">
          <el-tag :type="scope.row.taskType === 'todo' ? 'warning' : 'success'">
            {{ scope.row.taskType === 'todo' ? '待办' : '已办' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="200">
        <template #default="scope">
          <el-button 
            type="primary" 
            link 
            @click="handleApprove(scope.row)"
            v-if="scope.row.taskType === 'todo'"
          >审批</el-button>
          <el-button type="primary" link @click="handleDetail(scope.row)">详情</el-button>
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

    <!-- 审批对话框 -->
    <el-dialog title="任务审批" v-model="approveOpen" width="500px" append-to-body>
      <el-form ref="approveFormRef" :model="approveForm" :rules="approveRules" label-width="80px">
        <el-form-item label="审批结果" prop="approved">
          <el-radio-group v-model="approveForm.approved">
            <el-radio :label="true">同意</el-radio>
            <el-radio :label="false">拒绝</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="审批意见" prop="comment">
          <el-input
            v-model="approveForm.comment"
            type="textarea"
            placeholder="请输入审批意见"
            :rows="3"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitApprove">确 定</el-button>
          <el-button @click="cancelApprove">取 消</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 任务详情对话框 -->
    <el-dialog title="任务详情" v-model="detailOpen" width="1000px" append-to-body>
      <el-tabs v-model="activeTab">
        <el-tab-pane label="基本信息" name="info">
          <el-descriptions :column="2" border>
            <el-descriptions-item label="任务编号">{{ taskDetail.taskId }}</el-descriptions-item>
            <el-descriptions-item label="任务名称">{{ taskDetail.taskName }}</el-descriptions-item>
            <el-descriptions-item label="流程实例ID">{{ taskDetail.processInstanceId }}</el-descriptions-item>
            <el-descriptions-item label="流程名称">{{ taskDetail.processName }}</el-descriptions-item>
            <el-descriptions-item label="流程发起人">{{ taskDetail.startUser }}</el-descriptions-item>
            <el-descriptions-item label="任务创建时间">{{ taskDetail.createTime }}</el-descriptions-item>
            <el-descriptions-item label="任务类型">
              <el-tag :type="taskDetail.taskType === 'todo' ? 'warning' : 'success'">
                {{ taskDetail.taskType === 'todo' ? '待办' : '已办' }}
              </el-tag>
            </el-descriptions-item>
          </el-descriptions>
        </el-tab-pane>
        <el-tab-pane label="流程图" name="diagram">
          <div class="process-image">
            <img :src="processImage" alt="流程图" style="max-width: 100%;" />
          </div>
        </el-tab-pane>
        <el-tab-pane label="表单数据" name="form">
          <el-descriptions :column="2" border>
            <el-descriptions-item 
              v-for="(value, key) in taskDetail.formData" 
              :key="key" 
              :label="key"
            >{{ value }}</el-descriptions-item>
          </el-descriptions>
        </el-tab-pane>
      </el-tabs>
    </el-dialog>
  </div>
</template>

<script lang="ts" setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  listTask,
  getTask,
  completeTask,
  getTaskDiagram
} from '@/api/workflow/task'

const loading = ref(false)
const taskList = ref([])
const total = ref(0)
const approveOpen = ref(false)
const detailOpen = ref(false)
const activeTab = ref('info')
const processImage = ref('')
const currentTask = ref<any>(null)

// 查询参数
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  taskName: '',
  processName: '',
  taskType: 'todo'
})

// 审批表单参数
const approveForm = reactive({
  taskId: '',
  approved: true,
  comment: ''
})

// 任务详情
const taskDetail = reactive({
  taskId: '',
  taskName: '',
  processInstanceId: '',
  processName: '',
  startUser: '',
  createTime: '',
  taskType: '',
  formData: {}
})

// 审批表单校验规则
const approveRules = {
  comment: [
    { required: true, message: '请输入审批意见', trigger: 'blur' }
  ]
}

// 查询任务列表
const getList = async () => {
  loading.value = true
  try {
    const res = await listTask(queryParams)
    taskList.value = res.data.records
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
  queryParams.taskName = ''
  queryParams.processName = ''
  handleQuery()
}

// 审批按钮操作
const handleApprove = (row: any) => {
  currentTask.value = row
  approveForm.taskId = row.taskId
  approveForm.approved = true
  approveForm.comment = ''
  approveOpen.value = true
}

// 提交审批
const submitApprove = async () => {
  try {
    await completeTask({
      taskId: approveForm.taskId,
      approved: approveForm.approved,
      comment: approveForm.comment
    })
    approveOpen.value = false
    getList()
    ElMessage.success('审批成功')
  } catch (error) {
    console.error(error)
  }
}

// 取消审批
const cancelApprove = () => {
  approveOpen.value = false
  currentTask.value = null
}

// 查看详情按钮操作
const handleDetail = async (row: any) => {
  try {
    const [taskRes, imageRes] = await Promise.all([
      getTask(row.taskId),
      getTaskDiagram(row.processInstanceId)
    ])
    Object.assign(taskDetail, taskRes.data)
    processImage.value = 'data:image/png;base64,' + imageRes.data
    detailOpen.value = true
    activeTab.value = 'info'
  } catch (error) {
    console.error(error)
  }
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
</style> 