import { defineStore } from 'pinia'
import { ref } from 'vue'
import type { RouteRecordRaw } from 'vue-router'
import { constantRoutes } from '@/router'
import { useUserStore } from './user'
import Layout from '@/layout/index.vue'

/**
 * 使用meta.role来确定当前用户是否有权限
 */
function hasPermission(roles: string[], route: RouteRecordRaw) {
  if (route.meta && route.meta.roles) {
    return roles.some(role => (route.meta?.roles as string[]).includes(role))
  }
  return true
}

/**
 * 使用meta.permissions来确定当前用户是否有权限
 */
function hasPermissions(permissions: string[], route: RouteRecordRaw) {
  if (route.meta && route.meta.permissions) {
    return permissions.some(permission => (route.meta?.permissions as string[]).includes(permission))
  }
  return true
}

/**
 * 通过递归过滤异步路由表
 */
function filterAsyncRoutes(routes: RouteRecordRaw[], roles: string[], permissions: string[]) {
  const res: RouteRecordRaw[] = []

  routes.forEach(route => {
    const tmp = { ...route }
    if (hasPermission(roles, tmp) && hasPermissions(permissions, tmp)) {
      if (tmp.children) {
        tmp.children = filterAsyncRoutes(tmp.children, roles, permissions)
      }
      res.push(tmp)
    }
  })

  return res
}

// 工作流权限
export const workflowPermissions = {
  process: {
    list: 'workflow:process:list',
    create: 'workflow:process:create',
    update: 'workflow:process:update',
    delete: 'workflow:process:delete',
    deploy: 'workflow:process:deploy',
    start: 'workflow:process:start'
  },
  instance: {
    list: 'workflow:instance:list',
    view: 'workflow:instance:view',
    cancel: 'workflow:instance:cancel',
    delete: 'workflow:instance:delete'
  },
  task: {
    list: 'workflow:task:list',
    view: 'workflow:task:view',
    complete: 'workflow:task:complete',
    delegate: 'workflow:task:delegate',
    transfer: 'workflow:task:transfer'
  }
} as const

// 工作流角色
export const workflowRoles = {
  admin: 'workflow:admin',
  manager: 'workflow:manager',
  user: 'workflow:user'
} as const

// 异步路由配置
export const asyncRoutes: RouteRecordRaw[] = [
  {
    path: '/system',
    component: Layout,
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
        meta: { 
          title: '菜单管理', 
          icon: 'Menu',
          permissions: ['system:menu:list']
        }
      }
    ]
  },
  {
    path: '/workflow',
    component: Layout,
    redirect: '/workflow/process',
    meta: { 
      title: '工作流程',
      icon: 'Tickets',
      roles: [workflowRoles.admin, workflowRoles.manager, workflowRoles.user]
    },
    children: [
      {
        path: 'process',
        component: () => import('@/views/workflow/process/index.vue'),
        name: 'Process',
        meta: {
          title: '流程管理',
          icon: 'List',
          roles: [workflowRoles.admin, workflowRoles.manager],
          permissions: [workflowPermissions.process.list]
        }
      },
      {
        path: 'process/start',
        component: () => import('@/views/workflow/process/start.vue'),
        name: 'StartProcess',
        meta: {
          title: '发起流程',
          icon: 'Plus',
          hidden: true,
          roles: [workflowRoles.admin, workflowRoles.manager, workflowRoles.user],
          permissions: [workflowPermissions.process.start]
        }
      },
      {
        path: 'process/detail',
        component: () => import('@/views/workflow/process/detail.vue'),
        name: 'ProcessDetail',
        meta: {
          title: '流程详情',
          icon: 'Document',
          hidden: true,
          roles: [workflowRoles.admin, workflowRoles.manager, workflowRoles.user],
          permissions: [workflowPermissions.process.list]
        }
      },
      {
        path: 'instance',
        component: () => import('@/views/workflow/instance/index.vue'),
        name: 'Instance',
        meta: {
          title: '流程实例',
          icon: 'Connection',
          roles: [workflowRoles.admin, workflowRoles.manager],
          permissions: [workflowPermissions.instance.list]
        }
      },
      {
        path: 'task',
        component: () => import('@/views/workflow/task/index.vue'),
        name: 'Task',
        meta: {
          title: '任务管理',
          icon: 'Checked',
          roles: [workflowRoles.admin, workflowRoles.manager, workflowRoles.user],
          permissions: [workflowPermissions.task.list]
        }
      }
    ]
  }
]

// 权限 store
export const usePermissionStore = defineStore('permission', () => {
  const routes = ref<RouteRecordRaw[]>([])
  const dynamicRoutes = ref<RouteRecordRaw[]>([])

  async function generateRoutes() {
    const userStore = useUserStore()
    const { roles, permissions } = userStore

    try {
      let accessedRoutes
      if (roles.includes('admin')) {
        accessedRoutes = asyncRoutes
      } else {
        accessedRoutes = filterAsyncRoutes(asyncRoutes, roles, permissions)
      }
      routes.value = constantRoutes.concat(accessedRoutes)
      dynamicRoutes.value = accessedRoutes
      return accessedRoutes
    } catch (error) {
      return []
    }
  }

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