import { defineStore } from 'pinia'
import { login, logout, getInfo } from '@/api/auth'
import { getToken, setToken, removeToken } from '@/utils/auth'

export const useUserStore = defineStore('user', {
  state: () => ({
    token: getToken(),
    name: '',
    avatar: '',
    roles: [] as string[],
    permissions: [] as string[]
  }),

  actions: {
    // 登录
    async login(userInfo: { username: string; password: string }) {
      const { username, password } = userInfo
      const response = await login({ username: username.trim(), password })
      const { token } = response.data
      setToken(token)
      this.token = token
    },

    // 获取用户信息
    async getInfo() {
      const response = await getInfo()
      const { roles, name, avatar, permissions } = response.data
      this.roles = roles
      this.name = name
      this.avatar = avatar
      this.permissions = permissions
    },

    // 退出登录
    async logout() {
      await logout()
      this.token = ''
      this.roles = []
      this.permissions = []
      removeToken()
    }
  }
}) 