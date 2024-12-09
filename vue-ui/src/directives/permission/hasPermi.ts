import type { DirectiveBinding } from 'vue'
import { useUserStore } from '@/stores/user'

/**
 * 操作权限处理
 */
export default {
  mounted(el: HTMLElement, binding: DirectiveBinding) {
    // 获取对应的权限
    const { value } = binding
    const permissions = useUserStore().permissions

    if (value && value instanceof Array && value.length > 0) {
      const permissionFlag = value

      const hasPermissions = permissions.some(permission => {
        return permissionFlag.includes(permission)
      })

      if (!hasPermissions) {
        el.parentNode && el.parentNode.removeChild(el)
      }
    } else {
      throw new Error('请设置操作权限标签值')
    }
  }
} 