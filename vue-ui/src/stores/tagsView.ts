import { defineStore } from 'pinia'
import { ref } from 'vue'
import type { RouteLocationNormalized } from 'vue-router'

export const useTagsViewStore = defineStore('tagsView', () => {
  const visitedViews = ref<RouteLocationNormalized[]>([])
  const cachedViews = ref<string[]>([])

  function addVisitedView(view: RouteLocationNormalized) {
    if (visitedViews.value.some(v => v.path === view.path)) return
    visitedViews.value.push(
      Object.assign({}, view, {
        title: view.meta?.title || 'no-name'
      })
    )
  }

  function addCachedView(view: RouteLocationNormalized) {
    if (cachedViews.value.includes(view.name as string)) return
    if (!view.meta?.noCache) {
      cachedViews.value.push(view.name as string)
    }
  }

  function delVisitedView(view: RouteLocationNormalized) {
    for (const [i, v] of visitedViews.value.entries()) {
      if (v.path === view.path) {
        visitedViews.value.splice(i, 1)
        break
      }
    }
  }

  function delCachedView(view: RouteLocationNormalized) {
    const index = cachedViews.value.indexOf(view.name as string)
    index > -1 && cachedViews.value.splice(index, 1)
  }

  function delOthersViews(view: RouteLocationNormalized) {
    visitedViews.value = visitedViews.value.filter(v => {
      return v.meta?.affix || v.path === view.path
    })
    cachedViews.value = cachedViews.value.filter(name => {
      return name === view.name
    })
  }

  function delAllViews() {
    visitedViews.value = visitedViews.value.filter(tag => tag.meta?.affix)
    cachedViews.value = []
  }

  return {
    visitedViews,
    cachedViews,
    addVisitedView,
    addCachedView,
    delVisitedView,
    delCachedView,
    delOthersViews,
    delAllViews
  }
}) 