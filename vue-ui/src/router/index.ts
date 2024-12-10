import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'
import Layout from '../layout/index.vue'

export const constantRoutes: RouteRecordRaw[] = [
  {
    path: '/login',
    component: () => import('../views/login/index.vue'),
    meta: { title: '登录' }
  },
  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        component: () => import('../views/dashboard/index.vue'),
        name: 'Dashboard',
        meta: { title: '首页', icon: 'House', affix: true }
      }
    ]
  },
  {
    path: '/system',
    component: Layout,
    redirect: '/system/user',
    meta: { title: '系统管理', icon: 'Setting' },
    children: [
      {
        path: 'user',
        component: () => import('../views/system/user/index.vue'),
        name: 'User',
        meta: { title: '用户管理', icon: 'User' }
      },
      {
        path: 'role',
        component: () => import('../views/system/role/index.vue'),
        name: 'Role',
        meta: { title: '角色管理', icon: 'UserFilled' }
      },
      {
        path: 'menu',
        component: () => import('../views/system/menu/index.vue'),
        name: 'Menu',
        meta: { title: '菜单管理', icon: 'Menu' }
      },
      {
        path: 'dict',
        component: () => import('../views/system/dict/index.vue'),
        name: 'Dict',
        meta: { title: '字典管理', icon: 'Collection' }
      }
    ]
  },
  {
    path: '/workflow',
    component: Layout,
    redirect: '/workflow/process',
    meta: { title: '工作流程', icon: 'Tickets' },
    children: [
      {
        path: 'process',
        component: () => import('@/views/workflow/process/index.vue'),
        name: 'Process',
        meta: { title: '流程管理', icon: 'List' }
      },
      {
        path: 'process/start',
        component: () => import('@/views/workflow/process/start.vue'),
        name: 'StartProcess',
        meta: { title: '发起流程', icon: 'Plus', hidden: true }
      },
      {
        path: 'process/detail',
        component: () => import('@/views/workflow/process/detail.vue'),
        name: 'ProcessDetail',
        meta: { title: '流程详情', icon: 'Document', hidden: true }
      },
      {
        path: 'instance',
        component: () => import('@/views/workflow/instance/index.vue'),
        name: 'Instance',
        meta: { title: '流程实例', icon: 'Connection' }
      },
      {
        path: 'task',
        component: () => import('@/views/workflow/task/index.vue'),
        name: 'Task',
        meta: { title: '任务管理', icon: 'Checked' }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes: constantRoutes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  if (to.path === '/login') {
    if (token) {
      next('/')
    } else {
      next()
    }
  } else {
    if (token) {
      next()
    } else {
      next('/login')
    }
  }
})

export default router 