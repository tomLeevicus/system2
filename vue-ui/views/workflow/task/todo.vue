<template>
  <div class="app-container">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>我的待办任务</span>
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
        <el-table-column label="接收时间" align="center" prop="createTime" width="160" />
        <el-table-column label="操作" align="center" width="200">
          <template #default="scope">
            <el-button
              type="primary"
              link
              @click="handleComplete(scope.row)"
            >办理</el-button>
            <el-button
              type="primary"
              link
              @click="handleView(scope.row)"
            >查看</el-button>
            <el-button
              type="primary"
              link
              @click="handleDelegate(scope.row)"
            >委派</el-button>
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

    <!-- 任务办理对话框 -->
    <el-dialog
      title="任务办理"
      v-model="completeDialog"
      width="500px"
      append-to-body
    >
      <el-form ref="completeForm" :model="completeForm" :rules="completeRules" label-width="80px">
        <el-form-item label="审批意见" prop="comment">
          <el-input
            v-model="completeForm.comment"
            type="textarea"
            placeholder="请输入审批意见"
            :rows="3"
          />
        </el-form-item>
        <el-form-item label="审批结果" prop="approved">
          <el-radio-group v-model="completeForm.approved">
            <el-radio :label="true">同意</el-radio>
            <el-radio :label="false">驳回</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitComplete">确 定</el-button>
          <el-button @click="completeDialog = false">取 消</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 任务委派对话框 -->
    <el-dialog
      title="任务委派"
      v-model="delegateDialog"
      width="500px"
      append-to-body
    >
      <el-form ref="delegateForm" :model="delegateForm" :rules="delegateRules" label-width="80px">
        <el-form-item label="委派用户" prop="userId">
          <el-select v-model="delegateForm.userId" placeholder="请选择委派用户">
            <el-option
              v-for="user in userList"
              :key="user.userId"
              :label="user.nickName"
              :value="user.userId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="委派说明" prop="reason">
          <el-input
            v-model="delegateForm.reason"
            type="textarea"
            placeholder="请输入委派说明"
            :rows="3"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitDelegate">确 定</el-button>
          <el-button @click="delegateDialog = false">取 消</el-button>
        </div>
      </template>
    </el-dialog>

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
  </div>
</template>

<script lang="ts" setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { listTodoTask, completeTask, delegateTask, getTaskImage } from '@/api/workflow/task'
import { listAllUser } from '@/api/system/user'

// 遮罩层
const loading = ref(false)
// 总条数
const total = ref(0)
// 任务列表
const taskList = ref([])
// 用户列表
const userList = ref([])
// 是否显示任务办理对话框
const completeDialog = ref(false)
// 是否显示任务委派对话框
const delegateDialog = ref(false)
// 是否显示流程图对话框
const processDialog = ref(false)
// 流程图URL
const processImage = ref('')

// 查询参数
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  processName: undefined,
  taskName: undefined
})

// 任务办理表单
const completeForm = reactive({
  taskId: undefined,
  comment: undefined,
  approved: true
})

// 任务委派表单
const delegateForm = reactive({
  taskId: undefined,
  userId: undefined,
  reason: undefined
})

// 表单校验规则
const completeRules = {
  comment: [
    { required: true, message: '请输入审批意见', trigger: 'blur' }
  ]
}

const delegateRules = {
  userId: [
    { required: true, message: '请选择委派用户', trigger: 'change' }
  ],
  reason: [
    { required: true, message: '请输入委派说明', trigger: 'blur' }
  ]
}

/** 查询任务列表 */
function getList() {
  loading.value = true
  listTodoTask(queryParams).then(response => {
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

/** 任务办理按钮操作 */
function handleComplete(row: any) {
  completeForm.taskId = row.taskId
  completeForm.comment = undefined
  completeForm.approved = true
  completeDialog.value = true
}

/** 提交任务办理 */
function submitComplete() {
  completeTask(completeForm).then(response => {
    ElMessage.success('办理成功')
    completeDialog.value = false
    getList()
  })
}

/** 任务委派按钮操作 */
function handleDelegate(row: any) {
  delegateForm.taskId = row.taskId
  delegateForm.userId = undefined
  delegateForm.reason = undefined
  // 获取用户列表
  listAllUser().then(response => {
    userList.value = response.data
  })
  delegateDialog.value = true
}

/** 提交任务委派 */
function submitDelegate() {
  delegateTask(delegateForm).then(response => {
    ElMessage.success('委派成功')
    delegateDialog.value = false
    getList()
  })
}

/** 查看流程图 */
function handleView(row: any) {
  getTaskImage(row.taskId).then(response => {
    processImage.value = 'data:image/png;base64,' + response.data
    processDialog.value = true
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