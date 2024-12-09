import 'vue-router'
import type { RouteLocationMatched } from 'vue-router'

declare module 'vue-router' {
  interface RouteMeta {
    title?: string
    icon?: string
    hidden?: boolean
    noCache?: boolean
    breadcrumb?: boolean
    affix?: boolean
    activeMenu?: string
    noTagsView?: boolean
    alwaysShow?: boolean
    roles?: string[]
    permissions?: string[]
  }
}

// 路由配置
export interface RouteOption {
  path: string
  name?: string
  component?: any
  components?: any
  redirect?: string
  meta?: RouteMeta
  children?: RouteOption[]
}

// 路由模块
export interface RouteModule {
  route: RouteOption
  name: string
}

// 动态路由
export interface AsyncRouteMap {
  [key: string]: () => Promise<typeof import('*.vue')>
}

// 路由导航
export interface BreadcrumbItem {
  path: string
  title: string
}

// 标签页
export interface TagView extends Partial<RouteOption> {
  title?: string
  name?: string
  fullPath?: string
}

export interface DashboardRoute extends RouteLocationMatched {
  path: string
  name: string
  meta: {
    title: string
  }
  redirect: string
  components: Record<string, any>
  children: never[]
  props: Record<string, any>
  beforeEnter: undefined
  leaveGuards: never[]
  updateGuards: never[]
  enterCallbacks: Record<string, any>
  instances: Record<string, any>
  mods: never[]
  aliasOf: null
}

export interface RouteMetaData {
  title?: string
  icon?: string
  noCache?: boolean
  affix?: boolean
  breadcrumb?: boolean
  activeMenu?: string
}

export interface AppRouteRecordRaw {
  path: string
  name?: string
  meta?: RouteMetaData
  redirect?: string
  component?: any
  children?: AppRouteRecordRaw[]
  hidden?: boolean
  alwaysShow?: boolean
} 