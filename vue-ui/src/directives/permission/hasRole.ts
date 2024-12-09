import type { Directive, DirectiveBinding } from 'vue'
import { useUserStore } from '@/stores/user'

/**
 * 角色权限处理
 */
export const hasRole: Directive = {
  mounted(el: HTMLElement, binding: DirectiveBinding) {
    const { value } = binding
    const userStore = useUserStore()
    const roles = userStore.roles

    if (value && value instanceof Array && value.length > 0) {
      const hasRole = roles.some(role => value.includes(role))
      if (!hasRole) {
        el.parentNode?.removeChild(el)
      }
    } else {
      throw new Error('请设置角色权限标签值')
    }
  }
}

/**
 * 权限处理
 */
export const hasPermi: Directive = {
  mounted(el: HTMLElement, binding: DirectiveBinding) {
    const { value } = binding
    const userStore = useUserStore()
    const permissions = userStore.permissions

    if (value && value instanceof Array && value.length > 0) {
      const hasPermission = permissions.some(permission => value.includes(permission))
      if (!hasPermission) {
        el.parentNode?.removeChild(el)
      }
    } else {
      throw new Error('请设置操作权限标签值')
    }
  }
} 