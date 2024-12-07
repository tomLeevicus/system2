import { ElMessage } from 'element-plus'
import { useUserStore } from '@/stores/user'

export function errorHandler(error: any) {
  console.error('Error:', error)
  const userStore = useUserStore()

  if (error.response) {
    const { status, data } = error.response

    switch (status) {
      case 401:
        ElMessage.error('未授权，请重新登录')
        userStore.logout()
        break
      case 403:
        ElMessage.error('拒绝访问')
        break
      case 404:
        ElMessage.error('请求错误，未找到该资源')
        break
      case 500:
        ElMessage.error('服务器错误')
        break
      default:
        ElMessage.error(data.message || '未知错误')
    }
  } else if (error.message) {
    if (error.message.includes('timeout')) {
      ElMessage.error('网络超时')
    } else {
      ElMessage.error(error.message)
    }
  } else {
    ElMessage.error('未知错误')
  }
} 