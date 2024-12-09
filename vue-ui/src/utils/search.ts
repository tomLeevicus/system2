import Fuse from 'fuse.js'
import type { RouteOption } from '@/types/router'
import type { SearchOption, SearchResult, SearchConfig } from '@/types/search'

// 默认搜索配置
const defaultConfig: SearchConfig = {
  keys: ['title', 'path'],
  threshold: 0.3,
  location: 0,
  distance: 100,
  maxResults: 10,
  findAllMatches: true,
  includeScore: true,
  useExtendedSearch: true,
  ignoreLocation: true,
  ignoreFieldLength: true
}

// 路由转换为搜索选项
function routeToSearchOption(route: RouteOption): SearchOption {
  return {
    path: route.path,
    title: route.meta?.title || '',
    icon: route.meta?.icon
  }
}

// 递归获取路由
function getRoutes(routes: RouteOption[]): SearchOption[] {
  const result: SearchOption[] = []
  routes.forEach(route => {
    if (!route.meta?.hidden) {
      result.push(routeToSearchOption(route))
      if (route.children) {
        result.push(...getRoutes(route.children))
      }
    }
  })
  return result
}

// 搜索类
export class Search {
  private fuse: Fuse<SearchOption>
  private options: SearchOption[] = []

  constructor(config?: Partial<SearchConfig>) {
    this.fuse = new Fuse([], {
      ...defaultConfig,
      ...config
    })
  }

  // 初始化
  init(routes: RouteOption[]) {
    this.options = getRoutes(routes)
    this.fuse.setCollection(this.options)
  }

  // 搜索
  search(keyword: string): SearchResult[] {
    return this.fuse.search(keyword)
  }

  // 清空
  clear() {
    this.options = []
    this.fuse.setCollection([])
  }
}

// 创建搜索实例
export function createSearch(config?: Partial<SearchConfig>) {
  return new Search(config)
} 