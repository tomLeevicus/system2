<template>
  <div class="dashboard-container">
    <div class="welcome-section">
      <h1>欢迎使用系统</h1>
      <p>{{ welcomeMessage }}</p>
    </div>
    <el-row :gutter="20">
      <el-col :span="8">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>待办任务</span>
            </div>
          </template>
          <div class="task-list">
            <div v-if="tasks.length === 0" class="empty-text">暂无待办任务</div>
            <div v-else v-for="task in tasks" :key="task.id" class="task-item">
              <span>{{ task.name }}</span>
              <el-tag size="small" :type="task.priority === 'high' ? 'danger' : 'info'">
                {{ task.priority === 'high' ? '紧急' : '普通' }}
              </el-tag>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>系统公告</span>
            </div>
          </template>
          <div class="notice-list">
            <div v-if="notices.length === 0" class="empty-text">暂无系统公告</div>
            <div v-else v-for="notice in notices" :key="notice.id" class="notice-item">
              <span>{{ notice.title }}</span>
              <span class="notice-time">{{ notice.time }}</span>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>快捷入口</span>
            </div>
          </template>
          <div class="shortcut-list">
            <el-button v-for="shortcut in shortcuts" :key="shortcut.id" 
              type="primary" plain @click="handleShortcut(shortcut)">
              {{ shortcut.name }}
            </el-button>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const userStore = useUserStore()
const userName = userStore.name

const welcomeMessage = ref(`${userName || '用户'}，欢迎回来！`)

const tasks = ref([
  { id: 1, name: '示例任务1', priority: 'high' },
  { id: 2, name: '示例任务2', priority: 'normal' }
])

const notices = ref([
  { id: 1, title: '系统升级通知', time: '2024-01-20' },
  { id: 2, title: '功能更新说明', time: '2024-01-19' }
])

const shortcuts = ref([
  { id: 1, name: '发起流程', path: '/workflow/process/start' },
  { id: 2, name: '我的任务', path: '/workflow/task' },
  { id: 3, name: '流程管理', path: '/workflow/process' }
])

const handleShortcut = (shortcut: any) => {
  router.push(shortcut.path)
}
</script>

<style lang="scss" scoped>
.dashboard-container {
  padding: 20px;

  .welcome-section {
    margin-bottom: 20px;
    h1 {
      font-size: 24px;
      color: #303133;
      margin-bottom: 10px;
    }
    p {
      font-size: 16px;
      color: #606266;
    }
  }

  .el-card {
    margin-bottom: 20px;
  }

  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }

  .empty-text {
    color: #909399;
    text-align: center;
    padding: 20px 0;
  }

  .task-list {
    .task-item {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 10px 0;
      border-bottom: 1px solid #EBEEF5;
      &:last-child {
        border-bottom: none;
      }
    }
  }

  .notice-list {
    .notice-item {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 10px 0;
      border-bottom: 1px solid #EBEEF5;
      &:last-child {
        border-bottom: none;
      }
      .notice-time {
        color: #909399;
        font-size: 14px;
      }
    }
  }

  .shortcut-list {
    display: flex;
    flex-wrap: wrap;
    gap: 10px;
    .el-button {
      margin: 5px;
    }
  }
}
</style> 