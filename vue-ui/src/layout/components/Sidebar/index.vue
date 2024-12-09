<template>
  <div :class="{ 'has-logo': showLogo }">
    <Logo v-if="showLogo" :collapse="isCollapse" />
    <el-scrollbar wrap-class="scrollbar-wrapper">
      <el-menu
        :default-active="activeMenu"
        :collapse="isCollapse"
        :background-color="variables.menuBg"
        :text-color="variables.menuText"
        :active-text-color="variables.menuActiveText"
        :unique-opened="false"
        :collapse-transition="false"
        mode="vertical"
      >
        <SidebarItem
          v-for="route in routes"
          :key="route.path"
          :item="route"
          :base-path="route.path"
        />
      </el-menu>
    </el-scrollbar>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import { useSettingsStore } from '@/stores/settings'
import { useSidebarStore } from '@/stores/sidebar'
import variables from '@/styles/variables.scss?inline'
import SidebarItem from './SidebarItem.vue'
import Logo from './Logo.vue'

const route = useRoute()
const settingsStore = useSettingsStore()
const sidebarStore = useSidebarStore()

// 是否显示 Logo
const showLogo = computed(() => settingsStore.sidebarLogo)
// 是否折叠
const isCollapse = computed(() => !sidebarStore.opened)
// 激活菜单
const activeMenu = computed(() => {
  const { meta, path } = route
  if (meta?.activeMenu) {
    return meta.activeMenu
  }
  return path
})
// 路由列表
const routes = computed(() => sidebarStore.routes)
</script>

<style lang="scss" scoped>
.scrollbar-wrapper {
  overflow-x: hidden !important;
}

.el-scrollbar__view {
  height: 100%;
}

.has-logo {
  .el-scrollbar {
    height: calc(100% - 50px);
  }
}

.el-menu {
  border: none;
  height: 100%;
  width: 100% !important;
}
</style> 