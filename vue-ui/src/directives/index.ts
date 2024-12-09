import { Directive } from 'vue'
import { useUserStore } from '@/stores/user'

/**
 * 按钮权限校验
 */
export const hasPermi: Directive = {
  mounted(el, binding) {
    const { value } = binding
    const userStore = useUserStore()
    const permissions = userStore.permissions

    if (value && value instanceof Array && value.length > 0) {
      const hasPermission = permissions.some(permission => {
        return value.includes(permission)
      })

      if (!hasPermission) {
        el.parentNode?.removeChild(el)
      }
    } else {
      throw new Error('need permissions! Like v-hasPermi="[\'system:user:add\']"')
    }
  }
}

/**
 * 角色权限校验
 */
export const hasRole: Directive = {
  mounted(el, binding) {
    const { value } = binding
    const userStore = useUserStore()
    const roles = userStore.roles

    if (value && value instanceof Array && value.length > 0) {
      const hasRole = roles.some(role => {
        return value.includes(role)
      })

      if (!hasRole) {
        el.parentNode?.removeChild(el)
      }
    } else {
      throw new Error('need roles! Like v-hasRole="[\'admin\',\'editor\']"')
    }
  }
} 