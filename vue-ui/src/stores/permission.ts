import { defineStore } from 'pinia'
import { RouteRecordRaw } from 'vue-router'
import { getRouters } from '@/api/menu'
import Layout from '@/layout/index.vue'
import ParentView from '@/components/ParentView/index.vue'
import InnerLink from '@/layout/components/InnerLink/index.vue'
import { useUserStore } from '@/stores/user'

// 匹配views里面所有的.vue文件
const modules = import.meta.glob('../views/**/*.vue')

// 扩展路由类型
interface CustomRoute extends Omit<RouteRecordRaw, 'children'> {
  hidden?: boolean
  permissions?: string[]
  roles?: string[]
  children?: CustomRoute[]
  component?: any
  meta?: {
    title?: string
    icon?: string
    noCache?: boolean
    link?: string | null
  }
}

export const usePermissionStore = defineStore('permission', {
  state: () => ({
    routes: [] as CustomRoute[],
    addRoutes: [] as CustomRoute[],
    defaultRoutes: [] as CustomRoute[],
    topbarRouters: [] as CustomRoute[],
    sidebarRouters: [] as CustomRoute[]
  }),

  actions: {
    setRoutes(routes: CustomRoute[]) {
      this.addRoutes = routes
      this.routes = routes
    },
    setSidebarRouters(routes: CustomRoute[]) {
      this.sidebarRouters = routes
    },
    generateRoutes() {
      return new Promise<CustomRoute[]>((resolve) => {
        // 向后端请求路由数据
        getRouters().then(res => {
          const sdata = JSON.parse(JSON.stringify(res.data))
          const rdata = JSON.parse(JSON.stringify(res.data))
          const defaultData = JSON.parse(JSON.stringify(res.data))
          const sidebarRoutes = filterAsyncRouter(sdata)
          const rewriteRoutes = filterAsyncRouter(rdata, false, true)
          const defaultRoutes = filterAsyncRouter(defaultData)
          this.setRoutes(rewriteRoutes)
          this.setSidebarRouters(constantRoutes.concat(sidebarRoutes))
          resolve(rewriteRoutes)
        })
      })
    }
  }
})

// 遍历后台传来的路由字符串，转换为组件对象
function filterAsyncRouter(asyncRouterMap: any[], lastRouter = false, type = false): CustomRoute[] {
  return asyncRouterMap.filter(route => {
    if (type && route.children) {
      route.children = filterChildren(route.children)
    }
    if (route.component) {
      // Layout ParentView 组件特殊处理
      if (route.component === 'Layout') {
        route.component = Layout
      } else if (route.component === 'ParentView') {
        route.component = ParentView
      } else if (route.component === 'InnerLink') {
        route.component = InnerLink
      } else {
        route.component = loadView(route.component)
      }
    }
    if (route.children != null && route.children && route.children.length) {
      route.children = filterAsyncRouter(route.children, route, type)
    } else {
      delete route['children']
      delete route['redirect']
    }
    return true
  })
}

function filterChildren(childrenMap: any[], lastRouter: any = false): CustomRoute[] {
  const children: CustomRoute[] = []
  childrenMap.forEach((el) => {
    if (el.children && el.children.length) {
      if (el.component === 'ParentView' && !lastRouter) {
        el.children.forEach((c: any) => {
          c.path = el.path + '/' + c.path
          if (c.children && c.children.length) {
            children.push(...filterChildren(c.children, c))
            return
          }
          children.push(c)
        })
        return
      }
    }
    if (lastRouter) {
      el.path = lastRouter.path + '/' + el.path
    }
    children.push(el)
  })
  return children
}

// 动态路由遍历，验证是否具备权限
export function filterDynamicRoutes(routes: CustomRoute[]) {
  const res: CustomRoute[] = []
  const userStore = useUserStore()
  routes.forEach(route => {
    if (route.permissions) {
      if (userStore.permissions.some(permission => route.permissions?.includes(permission))) {
        res.push(route)
      }
    } else if (route.roles) {
      if (userStore.roles.some(role => route.roles?.includes(role))) {
        res.push(route)
      }
    }
  })
  return res
}

export const loadView = (view: string) => {
  let res;
  for (const path in modules) {
    const dir = path.split('views/')[1]?.split('.vue')[0];
    if (dir === view) {
      res = () => modules[path]();
    }
  }
  return res;
}

// 默认路由
export const constantRoutes: CustomRoute[] = [
  {
    path: '/login',
    component: () => import('@/views/login/index.vue'),
    hidden: true,
    meta: { title: '登录' }
  },
  {
    path: '/404',
    component: () => import('@/views/error/404.vue'),
    hidden: true,
    meta: { title: '404' }
  },
  {
    path: '/',
    component: Layout,
    redirect: 'index',
    children: [
      {
        path: 'index',
        component: () => import('@/views/index.vue'),
        name: 'Index',
        meta: { title: '首页', icon: 'dashboard', affix: true }
      }
    ]
  }
] 