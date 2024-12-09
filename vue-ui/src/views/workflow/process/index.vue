<template>
  <div class="app-container">
    <!-- 搜索区域 -->
    <el-form :model="queryParams" ref="queryRef" :inline="true">
      <el-form-item label="流程名称" prop="name">
        <el-input v-model="queryParams.name" placeholder="请输入流程名称" clearable />
      </el-form-item>
      <el-form-item label="流程分类" prop="category">
        <el-select v-model="queryParams.category" placeholder="流程分类" clearable>
          <el-option label="OA办公" value="oa" />
          <el-option label="财务" value="finance" />
          <el-option label="人事" value="hr" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleQuery">搜索</el-button>
        <el-button @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 操作按钮区域 -->
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" @click="handleDeploy">部署流程</el-button>
      </el-col>
    </el-row>

    <!-- 流程定义列表 -->
    <el-table v-loading="loading" :data="processList">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="流程编号" align="center" prop="processDefinitionId" />
      <el-table-column label="流程名称" align="center" prop="name" :show-overflow-tooltip="true" />
      <el-table-column label="流程分类" align="center" prop="category" />
      <el-table-column label="版本" align="center" prop="version" width="80" />
      <el-table-column label="部署时间" align="center" prop="deploymentTime" width="180" />
      <el-table-column label="状态" align="center" width="100">
        <template #default="scope">
          <el-switch
            v-model="scope.row.suspended"
            :active-value="false"
            :inactive-value="true"
            @change="handleStatusChange(scope.row)"
          />
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="320">
        <template #default="scope">
          <el-button type="primary" link @click="handleStartProcess(scope.row)">发起流程</el-button>
          <el-button type="primary" link @click="handleViewImage(scope.row)">查看图片</el-button>
          <el-button type="primary" link @click="handleViewXml(scope.row)">查看XML</el-button>
          <el-button type="danger" link @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 部署流程对话框 -->
    <el-dialog title="部署流程" v-model="deployOpen" width="500px" append-to-body>
      <el-form ref="deployFormRef" :model="deployForm" :rules="deployRules" label-width="80px">
        <el-form-item label="流程名称" prop="name">
          <el-input v-model="deployForm.name" placeholder="请输入流程名称" />
        </el-form-item>
        <el-form-item label="流程分类" prop="category">
          <el-select v-model="deployForm.category" placeholder="请选择流程分类">
            <el-option label="OA办公" value="oa" />
            <el-option label="财务" value="finance" />
            <el-option label="人事" value="hr" />
          </el-select>
        </el-form-item>
        <el-form-item label="流程文件" prop="file">
          <el-upload
            ref="uploadRef"
            :action="null"
            :auto-upload="false"
            :limit="1"
            accept=".bpmn,.bpmn20.xml"
            :on-change="handleFileChange"
          >
            <template #trigger>
              <el-button type="primary">选��文件</el-button>
            </template>
            <template #tip>
              <div class="el-upload__tip">只能上传bpmn文件</div>
            </template>
          </el-upload>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitDeploy">确 定</el-button>
          <el-button @click="cancelDeploy">取 消</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 查看流程图对话框 -->
    <el-dialog title="流程图" v-model="imageOpen" width="800px" append-to-body>
      <div class="process-image">
        <img :src="processImage" alt="流程图" style="max-width: 100%;" />
      </div>
    </el-dialog>

    <!-- 查看XML对话框 -->
    <el-dialog title="流程XML" v-model="xmlOpen" width="800px" append-to-body>
      <pre class="process-xml">{{ processXml }}</pre>
    </el-dialog>
  </div>
</template>

<script lang="ts" setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useRouter } from 'vue-router'
import {
  listProcess,
  deployProcess,
  deleteProcess,
  updateProcessState,
  getProcessImage,
  getProcessXml
} from '@/api/workflow/process'

const router = useRouter()
const loading = ref(false)
const processList = ref([])
const deployOpen = ref(false)
const imageOpen = ref(false)
const xmlOpen = ref(false)
const processImage = ref('')
const processXml = ref('')
const uploadRef = ref()

// 查询参数
const queryParams = reactive({
  name: '',
  category: undefined
})

// 部署表单参数
const deployForm = reactive({
  name: '',
  category: '',
  file: null as File | null
})

// 部署表单校验规则
const deployRules = {
  name: [
    { required: true, message: '流程名称不能为空', trigger: 'blur' }
  ],
  category: [
    { required: true, message: '流程分类不能为空', trigger: 'change' }
  ],
  file: [
    { required: true, message: '请选择流程文件', trigger: 'change' }
  ]
}

// 查询流程定义列表
const getList = async () => {
  loading.value = true
  try {
    const res = await listProcess(queryParams)
    processList.value = res.data
  } finally {
    loading.value = false
  }
}

// 搜索按钮操作
const handleQuery = () => {
  getList()
}

// 重置按钮操作
const resetQuery = () => {
  queryParams.name = ''
  queryParams.category = undefined
  handleQuery()
}

// 部署按钮操作
const handleDeploy = () => {
  resetDeployForm()
  deployOpen.value = true
}

// 文件上传change事件
const handleFileChange = (file: any) => {
  deployForm.file = file.raw
}

// 提交部署
const submitDeploy = async () => {
  if (!deployForm.file) {
    ElMessage.warning('请选择流程文件')
    return
  }
  
  const formData = new FormData()
  formData.append('name', deployForm.name)
  formData.append('category', deployForm.category)
  formData.append('file', deployForm.file)

  try {
    await deployProcess(formData)
    deployOpen.value = false
    getList()
    ElMessage.success('部署成功')
  } catch (error) {
    console.error(error)
  }
}

// 重置部署表单
const resetDeployForm = () => {
  deployForm.name = ''
  deployForm.category = ''
  deployForm.file = null
  if (uploadRef.value) {
    uploadRef.value.clearFiles()
  }
}

// 取消部署
const cancelDeploy = () => {
  resetDeployForm()
  deployOpen.value = false
}

// 发起流程
const handleStartProcess = (row: any) => {
  router.push({ 
    path: '/workflow/process/start',
    query: { processDefinitionId: row.processDefinitionId }
  })
}

// 查看流程图
const handleViewImage = async (row: any) => {
  try {
    const res = await getProcessImage(row.processDefinitionId)
    processImage.value = 'data:image/png;base64,' + res.data
    imageOpen.value = true
  } catch (error) {
    console.error(error)
  }
}

// 查看XML
const handleViewXml = async (row: any) => {
  try {
    const res = await getProcessXml(row.processDefinitionId)
    processXml.value = res.data
    xmlOpen.value = true
  } catch (error) {
    console.error(error)
  }
}

// 删除按钮操作
const handleDelete = (row: any) => {
  ElMessageBox.confirm('是否确认删除该流程?', '警告', {
    type: 'warning'
  }).then(async () => {
    await deleteProcess(row.deploymentId)
    getList()
    ElMessage.success('删除成功')
  })
}

// 状态修改
const handleStatusChange = async (row: any) => {
  try {
    await updateProcessState(row.processDefinitionId, !row.suspended)
    ElMessage.success(row.suspended ? '激活成功' : '挂起成功')
  } catch (error) {
    row.suspended = !row.suspended
  }
}

onMounted(() => {
  getList()
})
</script>

<style lang="scss" scoped>
.process-image {
  text-align: center;
}

.process-xml {
  max-height: 500px;
  overflow: auto;
  padding: 10px;
  background-color: #f5f5f5;
  font-family: monospace;
}
</style> 