import { defineStore } from 'pinia'
import { login, logout, getInfo } from '@/api/auth'
import { cache } from '@/utils/cache'
import { TOKEN_KEY, USER_INFO_KEY, ROLES_KEY, PERMISSIONS_KEY } from '@/constants/cache-keys'

export const useUserStore = defineStore('user', {
  state: () => ({
    token: cache.get(TOKEN_KEY) || '',
    userInfo: cache.get(USER_INFO_KEY) || {},
    roles: cache.get(ROLES_KEY) || [],
    permissions: cache.get(PERMISSIONS_KEY) || []
  }),

  actions: {
    // 登录
    async login(userInfo: { username: string; password: string }) {
      const { username, password } = userInfo
      const response = await login({ username: username.trim(), password })
      const { token } = response.data
      cache.set(TOKEN_KEY, token)
      this.token = token
    },

    // 获取用户信息
    async getInfo() {
      const response = await getInfo()
      const { roles, permissions, ...userInfo } = response.data
      
      cache.set(USER_INFO_KEY, userInfo)
      cache.set(ROLES_KEY, roles)
      cache.set(PERMISSIONS_KEY, permissions)

      this.userInfo = userInfo
      this.roles = roles
      this.permissions = permissions
    },

    // 退出登录
    async logout() {
      await logout()
      this.token = ''
      this.roles = []
      this.permissions = []
      this.userInfo = {}
      
      cache.remove(TOKEN_KEY)
      cache.remove(USER_INFO_KEY)
      cache.remove(ROLES_KEY)
      cache.remove(PERMISSIONS_KEY)
    }
  }
}) 