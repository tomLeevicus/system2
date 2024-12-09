import type { RouteLocationNormalized } from 'vue-router'

export interface TagView extends Partial<RouteLocationNormalized> {
  title?: string
  meta?: {
    title?: string
    noCache?: boolean
    affix?: boolean
  }
}

export interface TagsViewState {
  visitedViews: TagView[]
  cachedViews: string[]
}

export interface TagsViewActions {
  addVisitedView(view: TagView): void
  addCachedView(view: TagView): void
  delVisitedView(view: TagView): void
  delCachedView(view: TagView): void
  delOthersViews(view: TagView): void
  delAllViews(): void
} 