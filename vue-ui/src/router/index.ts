import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'
import Layout from '@/layout/index.vue'

export const constantRoutes: RouteRecordRaw[] = [
  {
    path: '/login',
    component: () => import('@/views/login/index.vue'),
    meta: { hidden: true }
  },
  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/dashboard/index.vue'),
        meta: { title: '首页', icon: 'House' }
      }
    ]
  },
  {
    path: '/system',
    component: Layout,
    meta: { title: '系统管理', icon: 'Setting' },
    children: [
      {
        path: 'user',
        name: 'User',
        component: () => import('@/views/system/user/index.vue'),
        meta: { title: '用户管理' }
      },
      {
        path: 'role',
        name: 'Role',
        component: () => import('@/views/system/role/index.vue'),
        meta: { title: '角色管理' }
      },
      {
        path: 'menu',
        name: 'Menu',
        component: () => import('@/views/system/menu/index.vue'),
        meta: { title: '菜单管理' }
      },
      {
        path: 'dict',
        name: 'Dict',
        component: () => import('@/views/system/dict/type/index.vue'),
        meta: { title: '字典管理' }
      }
    ]
  },
  {
    path: '/workflow',
    component: Layout,
    meta: { title: '工作流程', icon: 'Connection' },
    children: [
      {
        path: 'process',
        name: 'Process',
        component: () => import('@/views/workflow/process/index.vue'),
        meta: { title: '流程管理' }
      },
      {
        path: 'instance',
        name: 'Instance',
        component: () => import('@/views/workflow/instance/index.vue'),
        meta: { title: '流程实例' }
      },
      {
        path: 'task/todo',
        name: 'TodoTask',
        component: () => import('@/views/workflow/task/todo.vue'),
        meta: { title: '待办任务' }
      },
      {
        path: 'task/done',
        name: 'DoneTask',
        component: () => import('@/views/workflow/task/done.vue'),
        meta: { title: '已办任务' }
      },
      {
        path: 'history',
        name: 'History',
        component: () => import('@/views/workflow/history/index.vue'),
        meta: { title: '流程历史' }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes: constantRoutes,
  scrollBehavior: () => ({ left: 0, top: 0 })
})

export default router 