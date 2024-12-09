<template>
  <div class="app-container">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>流程详情</span>
          <div class="header-operations">
            <el-button 
              v-if="processInfo.status === 'running'"
              type="warning" 
              @click="handleCancel"
            >撤销流程</el-button>
            <el-button @click="$router.back()">返回</el-button>
          </div>
        </div>
      </template>

      <!-- 流程信息 -->
      <el-descriptions title="基本信息" :column="2" border>
        <el-descriptions-item label="流程名称">{{ processInfo.processName }}</el-descriptions-item>
        <el-descriptions-item label="流程编号">{{ processInfo.processInstanceId }}</el-descriptions-item>
        <el-descriptions-item label="流程发起人">{{ processInfo.startUser }}</el-descriptions-item>
        <el-descriptions-item label="当前节点">{{ processInfo.currentTask }}</el-descriptions-item>
        <el-descriptions-item label="开始时间">{{ processInfo.startTime }}</el-descriptions-item>
        <el-descriptions-item label="结束时间">{{ processInfo.endTime || '-' }}</el-descriptions-item>
        <el-descriptions-item label="流程状态">
          <el-tag :type="getStatusType(processInfo.status)">
            {{ getStatusLabel(processInfo.status) }}
          </el-tag>
        </el-descriptions-item>
      </el-descriptions>

      <!-- 表单数据 -->
      <el-descriptions 
        title="表单数据" 
        :column="2" 
        border 
        class="mt20"
        v-if="processInfo.formType === 'leave'"
      >
        <el-descriptions-item label="请假类型">
          {{ getLeaveTypeLabel(processInfo.formData?.leaveType) }}
        </el-descriptions-item>
        <el-descriptions-item label="请假天数">
          {{ processInfo.formData?.duration }}天
        </el-descriptions-item>
        <el-descriptions-item label="开始时间">{{ processInfo.formData?.startTime }}</el-descriptions-item>
        <el-descriptions-item label="结束时间">{{ processInfo.formData?.endTime }}</el-descriptions-item>
        <el-descriptions-item label="请假原因" :span="2">
          {{ processInfo.formData?.reason }}
        </el-descriptions-item>
      </el-descriptions>

      <!-- 流程图 -->
      <div class="process-diagram mt20">
        <div class="section-title">流程图</div>
        <div class="diagram-container">
          <img :src="processDiagram" alt="流程图" v-if="processDiagram" />
          <div class="no-diagram" v-else>暂无流程图</div>
        </div>
      </div>

      <!-- 审批历史 -->
      <div class="approval-history mt20">
        <div class="section-title">审批历史</div>
        <el-timeline>
          <el-timeline-item
            v-for="(activity, index) in processInfo.activities"
            :key="index"
            :type="getTimelineItemType(activity)"
            :timestamp="activity.time"
          >
            <h4>{{ activity.taskName }}</h4>
            <p class="activity-info">处理人：{{ activity.assignee }}</p>
            <p class="activity-info">
              处理结果：
              <el-tag 
                :type="activity.approved ? 'success' : 'danger'"
                size="small"
              >
                {{ activity.approved ? '同意' : '拒绝' }}
              </el-tag>
            </p>
            <p class="activity-info">处理意见：{{ activity.comment }}</p>
          </el-timeline-item>
        </el-timeline>
      </div>
    </el-card>
  </div>
</template>

<script lang="ts" setup>
import { ref, reactive, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getProcessInstance, getInstanceDiagram, cancelProcess } from '@/api/workflow/process'

const route = useRoute()
const router = useRouter()
const processDiagram = ref('')

// 流程信息
const processInfo = reactive({
  processInstanceId: '',
  processName: '',
  startUser: '',
  currentTask: '',
  startTime: '',
  endTime: '',
  status: '',
  formType: '',
  formData: null,
  activities: []
})

// 获取状态标签
const getStatusLabel = (status: string) => {
  const statusMap: { [key: string]: string } = {
    running: '运行中',
    suspended: '已挂起',
    finished: '已完成',
    canceled: '已取消'
  }
  return statusMap[status] || status
}

// 获取状态类型
const getStatusType = (status: string) => {
  const typeMap: { [key: string]: string } = {
    running: 'primary',
    suspended: 'warning',
    finished: 'success',
    canceled: 'info'
  }
  return typeMap[status] || 'info'
}

// 获取请假类型标签
const getLeaveTypeLabel = (type: string) => {
  const typeMap: { [key: string]: string } = {
    personal: '事假',
    sick: '病假',
    annual: '年假',
    compensatory: '调休'
  }
  return typeMap[type] || type
}

// 获取时间线项目类型
const getTimelineItemType = (activity: any) => {
  if (activity.type === 'startEvent') return 'primary'
  if (activity.type === 'endEvent') return 'success'
  return activity.approved ? 'success' : 'danger'
}

// 获取流程实例详情
const getProcessDetail = async () => {
  try {
    const processInstanceId = route.query.processInstanceId as string
    if (!processInstanceId) {
      ElMessage.error('流程实例ID不能为空')
      return
    }

    const [instanceRes, diagramRes] = await Promise.all([
      getProcessInstance(processInstanceId),
      getInstanceDiagram(processInstanceId)
    ])

    Object.assign(processInfo, instanceRes.data)
    processDiagram.value = 'data:image/png;base64,' + diagramRes.data
  } catch (error) {
    console.error(error)
  }
}

// 撤销流程
const handleCancel = () => {
  ElMessageBox.confirm('确认要撤销该流程吗？', '警告', {
    type: 'warning'
  }).then(async () => {
    await cancelProcess(processInfo.processInstanceId)
    ElMessage.success('撤销成功')
    getProcessDetail()
  })
}

onMounted(() => {
  getProcessDetail()
})
</script>

<style lang="scss" scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;

  .header-operations {
    .el-button {
      margin-left: 10px;
    }
  }
}

.mt20 {
  margin-top: 20px;
}

.section-title {
  font-size: 16px;
  color: #303133;
  margin-bottom: 15px;
  font-weight: 500;
}

.process-diagram {
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

.approval-history {
  .activity-info {
    color: #606266;
    margin: 5px 0;
    font-size: 13px;
  }
}
</style> 