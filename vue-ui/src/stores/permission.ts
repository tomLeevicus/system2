import { defineStore } from 'pinia'
import { ref } from 'vue'
import type { RouteRecordRaw } from 'vue-router'
import { constantRoutes } from '@/router'
import { useUserStore } from './user'

/**
 * 使用meta.role来确定当前用户是否有权限
 * @param roles 用户角色
 * @param route 路由
 */
function hasPermission(roles: string[], route: RouteRecordRaw) {
  if (route.meta && route.meta.roles) {
    return roles.some(role => (route.meta?.roles as string[]).includes(role))
  }
  return true
}

/**
 * 通过递归过滤异步路由表
 * @param routes 异步路由
 * @param roles 用户角色
 */
function filterAsyncRoutes(routes: RouteRecordRaw[], roles: string[]) {
  const res: RouteRecordRaw[] = []

  routes.forEach(route => {
    const tmp = { ...route }
    if (hasPermission(roles, tmp)) {
      if (tmp.children) {
        tmp.children = filterAsyncRoutes(tmp.children, roles)
      }
      res.push(tmp)
    }
  })

  return res
}

export const usePermissionStore = defineStore('permission', () => {
  // 路由
  const routes = ref<RouteRecordRaw[]>([])
  // 动态路由
  const dynamicRoutes = ref<RouteRecordRaw[]>([])

  /**
   * 生成路由
   */
  async function generateRoutes() {
    const userStore = useUserStore()
    const { roles } = userStore

    try {
      let accessedRoutes
      if (roles.includes('admin')) {
        // 管理员拥有所有权限
        accessedRoutes = asyncRoutes || []
      } else {
        accessedRoutes = filterAsyncRoutes(asyncRoutes, roles)
      }
      routes.value = constantRoutes.concat(accessedRoutes)
      dynamicRoutes.value = accessedRoutes
      return accessedRoutes
    } catch (error) {
      return []
    }
  }

  /**
   * 重置路由
   */
  function resetRoutes() {
    routes.value = []
    dynamicRoutes.value = []
  }

  return {
    routes,
    dynamicRoutes,
    generateRoutes,
    resetRoutes
  }
})

// 异步路由
export const asyncRoutes: RouteRecordRaw[] = [
  {
    path: '/system',
    component: () => import('@/layout/index.vue'),
    redirect: '/system/user',
    meta: { title: '系统管理', icon: 'Setting', roles: ['admin'] },
    children: [
      {
        path: 'user',
        component: () => import('@/views/system/user/index.vue'),
        name: 'User',
        meta: { title: '用户管理', icon: 'User' }
      },
      {
        path: 'role',
        component: () => import('@/views/system/role/index.vue'),
        name: 'Role',
        meta: { title: '角色管理', icon: 'UserFilled' }
      },
      {
        path: 'menu',
        component: () => import('@/views/system/menu/index.vue'),
        name: 'Menu',
        meta: { title: '菜单管理', icon: 'Menu' }
      }
    ]
  },
  {
    path: '/workflow',
    component: () => import('@/layout/index.vue'),
    redirect: '/workflow/process',
    meta: { title: '工作流程', icon: 'Operation' },
    children: [
      {
        path: 'process',
        component: () => import('@/views/workflow/process/index.vue'),
        name: 'Process',
        meta: { title: '流程管理', icon: 'List' }
      },
      {
        path: 'task',
        component: () => import('@/views/workflow/task/index.vue'),
        name: 'Task',
        meta: { title: '任务管理', icon: 'Tickets' }
      }
    ]
  }
] 