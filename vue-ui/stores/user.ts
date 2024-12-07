import { defineStore } from 'pinia'
import { login, logout, getInfo } from '@/api/login'
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
      const res = await login({ username: username.trim(), password })
      setToken(res.token)
      this.token = res.token
    },
    
    // 获取用户信息
    async getInfo() {
      const res = await getInfo()
      const { roles, name, avatar, permissions } = res
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