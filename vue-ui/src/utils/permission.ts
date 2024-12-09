import type { Router } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { usePermissionStore } from '@/stores/permission'

/**
 * 权限配置
 */
export function setupPermission(router: Router) {
  router.beforeEach(async (to, from, next) => {
    const userStore = useUserStore()
    const permissionStore = usePermissionStore()

    if (userStore.token) {
      if (to.path === '/login') {
        next({ path: '/' })
      } else {
        if (!userStore.roles.length) {
          try {
            // 获取用户信息
            await userStore.getInfo()
            // 生成可访问路由
            const accessRoutes = await permissionStore.generateRoutes()
            // 动态添加路由
            accessRoutes.forEach(route => {
              router.addRoute(route)
            })
            next({ ...to, replace: true })
          } catch (error) {
            // 移除 token 并跳转登录页
            await userStore.logout()
            next(`/login?redirect=${to.path}`)
          }
        } else {
          next()
        }
      }
    } else {
      if (to.path === '/login') {
        next()
      } else {
        next(`/login?redirect=${to.path}`)
      }
    }
  })
}

/**
 * 判断用户是否拥有指定权限
 * @param permissions 权限列表
 */
export function hasPermission(permissions: string | string[]): boolean {
  const userStore = useUserStore()
  const userPermissions = userStore.permissions

  if (typeof permissions === 'string') {
    return userPermissions.includes(permissions)
  }

  return permissions.some(permission => userPermissions.includes(permission))
}

/**
 * 判断用户是否拥有指定角色
 * @param roles 角色列表
 */
export function hasRole(roles: string | string[]): boolean {
  const userStore = useUserStore()
  const userRoles = userStore.roles

  if (typeof roles === 'string') {
    return userRoles.includes(roles)
  }

  return roles.some(role => userRoles.includes(role))
} 