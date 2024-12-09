import type { RouteOption } from './router'
import type { IFuseOptions } from 'fuse.js'

// 搜索选项
export interface SearchOption {
  path: string
  title: string
  icon?: string
}

// 搜索结果
export interface SearchResult {
  item: SearchOption
  refIndex: number
  score?: number
  matches?: Array<{
    indices: Array<[number, number]>
    key: string
    value: string
  }>
}

// 搜索配置
export type SearchConfig = IFuseOptions<SearchOption>

// 搜索助手
export interface SearchHelper {
  init(routes: RouteOption[]): void
  search(keyword: string): SearchResult[]
  clear(): void
} 