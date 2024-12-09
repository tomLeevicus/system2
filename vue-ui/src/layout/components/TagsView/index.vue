<template>
  <div id="tags-view-container" class="tags-view-container">
    <scroll-pane ref="scrollPane" class="tags-view-wrapper" @scroll="handleScroll">
      <router-link
        v-for="tag in visitedViews"
        :key="tag.path"
        :data-path="tag.path"
        :class="isActive(tag) ? 'active' : ''"
        :to="{ path: tag.path, query: tag.query, fullPath: tag.fullPath }"
        class="tags-view-item"
        @click.middle="!isAffix(tag) && closeSelectedTag(tag)"
        @contextmenu.prevent="openMenu(tag, $event)"
      >
        {{ tag.title }}
        <el-icon v-if="!isAffix(tag)" class="el-icon-close" @click.prevent.stop="closeSelectedTag(tag)">
          <Close />
        </el-icon>
      </router-link>
    </scroll-pane>
    <ul v-show="visible" :style="{ left: left + 'px', top: top + 'px' }" class="contextmenu">
      <li @click="refreshSelectedTag(selectedTag)">刷新页面</li>
      <li v-if="!isAffix(selectedTag)" @click="closeSelectedTag(selectedTag)">关闭当前</li>
      <li @click="closeOthersTags">关闭其他</li>
      <li @click="closeAllTags(selectedTag)">关闭所有</li>
    </ul>
  </div>
</template>

<script lang="ts" setup>
import { ref, computed, watch, nextTick, onMounted, onBeforeUnmount } from 'vue'
import { useRoute, useRouter, RouteLocationNormalized } from 'vue-router'
import { Close } from '@element-plus/icons-vue'
import ScrollPane from './ScrollPane.vue'
import { useTagsViewStore } from '@/stores/tagsView'

const route = useRoute()
const router = useRouter()
const tagsViewStore = useTagsViewStore()
const scrollPane = ref()
const visible = ref(false)
const top = ref(0)
const left = ref(0)
const selectedTag = ref<RouteLocationNormalized>()

const visitedViews = computed(() => tagsViewStore.visitedViews)

watch(
  () => route.path,
  () => {
    addTags()
    moveToCurrentTag()
  }
)

function isActive(route: RouteLocationNormalized) {
  return route.path === this.$route.path
}

function isAffix(tag: RouteLocationNormalized) {
  return tag.meta && tag.meta.affix
}

function addTags() {
  const { name } = route
  if (name) {
    tagsViewStore.addView(route)
  }
  return false
}

function moveToCurrentTag() {
  nextTick(() => {
    for (const tag of visitedViews.value) {
      if (tag.path === route.path) {
        scrollPane.value?.moveToTarget(tag)
        if (tag.fullPath !== route.fullPath) {
          tagsViewStore.updateVisitedView(route)
        }
        break
      }
    }
  })
}

function refreshSelectedTag(view: RouteLocationNormalized) {
  tagsViewStore.delCachedView(view).then(() => {
    const { fullPath } = view
    nextTick(() => {
      router.replace({
        path: '/redirect' + fullPath
      })
    })
  })
}

function closeSelectedTag(view: RouteLocationNormalized) {
  tagsViewStore.delView(view).then(({ visitedViews }) => {
    if (isActive(view)) {
      toLastView(visitedViews, view)
    }
  })
}

function closeOthersTags() {
  router.push(selectedTag.value!)
  tagsViewStore.delOthersViews(selectedTag.value!).then(() => {
    moveToCurrentTag()
  })
}

function closeAllTags(view: RouteLocationNormalized) {
  tagsViewStore.delAllViews().then(({ visitedViews }) => {
    if (isAffix(view)) {
      return
    }
    toLastView(visitedViews, view)
  })
}

function toLastView(visitedViews: RouteLocationNormalized[], view: RouteLocationNormalized) {
  const latestView = visitedViews.slice(-1)[0]
  if (latestView) {
    router.push(latestView.fullPath)
  } else {
    if (view.name === 'Dashboard') {
      router.replace({ path: '/redirect' + view.fullPath })
    } else {
      router.push('/')
    }
  }
}

function openMenu(tag: RouteLocationNormalized, e: MouseEvent) {
  const menuMinWidth = 105
  const offsetLeft = this.$el.getBoundingClientRect().left
  const offsetWidth = this.$el.offsetWidth
  const maxLeft = offsetWidth - menuMinWidth
  const left = e.clientX - offsetLeft + 15

  if (left > maxLeft) {
    this.left = maxLeft
  } else {
    this.left = left
  }

  this.top = e.clientY
  this.visible = true
  this.selectedTag = tag
}

function closeMenu() {
  visible.value = false
}

function handleScroll() {
  closeMenu()
}

onMounted(() => {
  addTags()
  document.addEventListener('click', closeMenu)
})

onBeforeUnmount(() => {
  document.removeEventListener('click', closeMenu)
})
</script>

<style lang="scss" scoped>
.tags-view-container {
  height: 34px;
  width: 100%;
  background: #fff;
  border-bottom: 1px solid #d8dce5;
  box-shadow: 0 1px 3px 0 rgba(0, 0, 0, .12), 0 0 3px 0 rgba(0, 0, 0, .04);

  .tags-view-wrapper {
    .tags-view-item {
      display: inline-block;
      position: relative;
      cursor: pointer;
      height: 26px;
      line-height: 26px;
      border: 1px solid #d8dce5;
      color: #495060;
      background: #fff;
      padding: 0 8px;
      font-size: 12px;
      margin-left: 5px;
      margin-top: 4px;

      &:first-of-type {
        margin-left: 15px;
      }

      &:last-of-type {
        margin-right: 15px;
      }

      &.active {
        background-color: #42b983;
        color: #fff;
        border-color: #42b983;

        &::before {
          content: '';
          background: #fff;
          display: inline-block;
          width: 8px;
          height: 8px;
          border-radius: 50%;
          position: relative;
          margin-right: 2px;
        }
      }
    }
  }

  .contextmenu {
    margin: 0;
    background: #fff;
    z-index: 3000;
    position: absolute;
    list-style-type: none;
    padding: 5px 0;
    border-radius: 4px;
    font-size: 12px;
    font-weight: 400;
    color: #333;
    box-shadow: 2px 2px 3px 0 rgba(0, 0, 0, .3);

    li {
      margin: 0;
      padding: 7px 16px;
      cursor: pointer;

      &:hover {
        background: #eee;
      }
    }
  }
}
</style> 