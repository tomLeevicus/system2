<template>
  <div class="app-container">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>发起流程 - {{ processName }}</span>
          <el-button type="primary" @click="handleSubmit">提交</el-button>
        </div>
      </template>
      
      <!-- 流程表单 -->
      <el-form 
        ref="formRef"
        :model="formData"
        :rules="formRules"
        label-width="100px"
      >
        <!-- 请假类型 -->
        <el-form-item label="请假类型" prop="leaveType">
          <el-select v-model="formData.leaveType" placeholder="请选择请假类型">
            <el-option label="事假" value="personal" />
            <el-option label="病假" value="sick" />
            <el-option label="年假" value="annual" />
            <el-option label="调休" value="compensatory" />
          </el-select>
        </el-form-item>

        <!-- 开始时间 -->
        <el-form-item label="开始时间" prop="startTime">
          <el-date-picker
            v-model="formData.startTime"
            type="datetime"
            placeholder="请选择开始时间"
            value-format="YYYY-MM-DD HH:mm:ss"
          />
        </el-form-item>

        <!-- 结束时间 -->
        <el-form-item label="结束时间" prop="endTime">
          <el-date-picker
            v-model="formData.endTime"
            type="datetime"
            placeholder="请选择结束时间"
            value-format="YYYY-MM-DD HH:mm:ss"
          />
        </el-form-item>

        <!-- 请假天数 -->
        <el-form-item label="请假天数" prop="duration">
          <el-input-number 
            v-model="formData.duration" 
            :precision="1" 
            :step="0.5" 
            :min="0.5"
          />
        </el-form-item>

        <!-- 请假原因 -->
        <el-form-item label="请假原因" prop="reason">
          <el-input
            v-model="formData.reason"
            type="textarea"
            placeholder="请输入请假原因"
            :rows="3"
          />
        </el-form-item>
      </el-form>

      <!-- 流程图预览 -->
      <div class="process-diagram">
        <h3>流程图预览</h3>
        <div class="diagram-container">
          <img :src="processDiagram" alt="流程图" v-if="processDiagram" />
          <div class="no-diagram" v-else>暂无流程图</div>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script lang="ts" setup>
import { ref, reactive, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getProcessDefinition, getProcessDiagram, startProcess } from '@/api/workflow/process'

const route = useRoute()
const router = useRouter()
const formRef = ref()
const processName = ref('')
const processDiagram = ref('')

// 表单数据
const formData = reactive({
  leaveType: '',
  startTime: '',
  endTime: '',
  duration: 0.5,
  reason: ''
})

// 表单校验规则
const formRules = {
  leaveType: [
    { required: true, message: '请选择请假类型', trigger: 'change' }
  ],
  startTime: [
    { required: true, message: '请选择开始时间', trigger: 'change' }
  ],
  endTime: [
    { required: true, message: '请选择结束时间', trigger: 'change' }
  ],
  duration: [
    { required: true, message: '请输入请假天数', trigger: 'blur' }
  ],
  reason: [
    { required: true, message: '请输入请假原因', trigger: 'blur' }
  ]
}

// 获取流程定义信息
const getProcessInfo = async () => {
  try {
    const processDefinitionId = route.query.processDefinitionId as string
    if (!processDefinitionId) {
      ElMessage.error('���程定义ID不能为空')
      return
    }

    const [definitionRes, diagramRes] = await Promise.all([
      getProcessDefinition(processDefinitionId),
      getProcessDiagram(processDefinitionId)
    ])

    processName.value = definitionRes.data.name
    processDiagram.value = 'data:image/png;base64,' + diagramRes.data
  } catch (error) {
    console.error(error)
  }
}

// 提交表单
const handleSubmit = async () => {
  if (!formRef.value) return
  
  try {
    await formRef.value.validate()
    
    const processDefinitionId = route.query.processDefinitionId as string
    await startProcess(processDefinitionId, formData)
    
    ElMessage.success('流程发起成功')
    router.push('/workflow/task')
  } catch (error) {
    console.error(error)
  }
}

// 检查时间是否合法
const validateTime = () => {
  if (formData.startTime && formData.endTime) {
    const start = new Date(formData.startTime).getTime()
    const end = new Date(formData.endTime).getTime()
    if (end <= start) {
      ElMessage.warning('结束时间必须大于开始时间')
      formData.endTime = ''
    }
  }
}

// 监听时间变化
watch(() => formData.startTime, validateTime)
watch(() => formData.endTime, validateTime)

onMounted(() => {
  getProcessInfo()
})
</script>

<style lang="scss" scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.process-diagram {
  margin-top: 20px;
  border-top: 1px solid #eee;
  padding-top: 20px;

  h3 {
    margin-bottom: 15px;
    font-weight: normal;
    color: #606266;
  }

  .diagram-container {
    text-align: center;
    background-color: #f5f7fa;
    padding: 20px;
    border-radius: 4px;

    img {
      max-width: 100%;
    }

    .no-diagram {
      color: #909399;
      padding: 30px 0;
    }
  }
}

.el-form {
  max-width: 600px;
}
</style> 