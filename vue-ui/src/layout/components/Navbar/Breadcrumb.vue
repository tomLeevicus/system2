<template>
  <el-breadcrumb class="app-breadcrumb" separator="/">
    <transition-group name="breadcrumb">
      <el-breadcrumb-item v-for="(item, index) in levelList" :key="item.path">
        <span v-if="item.redirect === 'noRedirect' || index === levelList.length - 1" class="no-redirect">
          {{ item.meta?.title }}
        </span>
        <a v-else @click.prevent="handleLink(item)">{{ item.meta?.title }}</a>
      </el-breadcrumb-item>
    </transition-group>
  </el-breadcrumb>
</template>

<script lang="ts" setup>
import { ref, watch } from 'vue'
import { useRoute, useRouter, type RouteLocationMatched } from 'vue-router'

const route = useRoute()
const router = useRouter()

const levelList = ref<RouteLocationMatched[]>([])

function isDashboard(route: RouteLocationMatched) {
  const name = route?.name
  return name === 'Dashboard'
}

function getBreadcrumb() {
  let matched = route.matched.filter(item => item.meta && item.meta.title)
  const first = matched[0]

  if (!isDashboard(first)) {
    matched = [{
      path: '/dashboard',
      name: 'Dashboard',
      meta: { title: '首页' },
      redirect: '',
      components: {},
      children: [],
      props: {},
      beforeEnter: undefined,
      leaveGuards: [],
      updateGuards: [],
      enterCallbacks: {},
      instances: {},
    } as RouteLocationMatched].concat(matched)
  }

  levelList.value = matched
}

function handleLink(item: RouteLocationMatched) {
  const { redirect, path } = item
  if (redirect) {
    router.push(redirect.toString())
    return
  }
  router.push(path)
}

watch(
  () => route.path,
  () => getBreadcrumb(),
  {
    immediate: true
  }
)
</script>

<style lang="scss" scoped>
.app-breadcrumb.el-breadcrumb {
  display: inline-block;
  font-size: 14px;
  line-height: 50px;
  margin-left: 8px;

  .no-redirect {
    color: #97a8be;
    cursor: text;
  }
}

.breadcrumb-enter-active,
.breadcrumb-leave-active {
  transition: all .5s;
}

.breadcrumb-enter-from,
.breadcrumb-leave-active {
  opacity: 0;
  transform: translateX(20px);
}

.breadcrumb-leave-active {
  position: absolute;
}
</style> 