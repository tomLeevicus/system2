import { App, DirectiveBinding } from 'vue'
import { useUserStore } from '@/stores/user'

// 权限指令
function hasPermi(el: HTMLElement, binding: DirectiveBinding) {
  const userStore = useUserStore()
  const { value } = binding
  const permissions = userStore.permissions

  if (Array.isArray(value)) {
    if (value.length > 0) {
      const permissionFlag = value
      const hasPermissions = permissions.some(permission => {
        return permissionFlag.includes(permission)
      })

      if (!hasPermissions) {
        el.parentNode?.removeChild(el)
      }
    }
  } else {
    if (!permissions.includes(value)) {
      el.parentNode?.removeChild(el)
    }
  }
}

// 角色指令
function hasRole(el: HTMLElement, binding: DirectiveBinding) {
  const userStore = useUserStore()
  const { value } = binding
  const roles = userStore.roles

  if (Array.isArray(value)) {
    if (value.length > 0) {
      const roleFlag = value
      const hasRole = roles.some(role => {
        return roleFlag.includes(role)
      })

      if (!hasRole) {
        el.parentNode?.removeChild(el)
      }
    }
  } else {
    if (!roles.includes(value)) {
      el.parentNode?.removeChild(el)
    }
  }
}

export default {
  install(app: App) {
    app.directive('hasPermi', {
      mounted: hasPermi
    })
    app.directive('hasRole', {
      mounted: hasRole
    })
  }
} 