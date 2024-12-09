import { defineStore } from 'pinia'
import { ref } from 'vue'
import { login, getInfo, logout } from '@/api/auth'
import type { LoginForm, LoginResponse, UserInfo } from '@/types/auth'
import { getToken, setToken, removeToken } from '@/utils/auth'
import router from '@/router'

export const useUserStore = defineStore('user', () => {
  const token = ref(getToken() || '')
  const name = ref('')
  const avatar = ref('')
  const roles = ref<string[]>([])
  const permissions = ref<string[]>([])

  // 登录
  async function loginAction(loginForm: LoginForm) {
    try {
      const { data } = await login(loginForm)
      setToken(data.token)
      token.value = data.token
    } catch (error) {
      removeToken()
      throw error
    }
  }

  // 获取用户信息
  async function getInfoAction() {
    try {
      const { data } = await getInfo()
      name.value = data.name
      avatar.value = data.avatar
      roles.value = data.roles
      permissions.value = data.permissions
      return data
    } catch (error) {
      throw error
    }
  }

  // 登出
  async function logoutAction() {
    try {
      await logout()
      token.value = ''
      roles.value = []
      permissions.value = []
      removeToken()
      router.push('/login')
    } catch (error) {
      console.error('登出失败:', error)
    }
  }

  return {
    token,
    name,
    avatar,
    roles,
    permissions,
    login: loginAction,
    getInfo: getInfoAction,
    logout: logoutAction
  }
}) 