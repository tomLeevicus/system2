<template>
  <div :class="classObj" class="app-wrapper">
    <div v-if="device === 'mobile' && sidebar.opened" class="drawer-bg" @click="handleClickOutside" />
    <sidebar class="sidebar-container" />
    <div class="main-container">
      <div :class="{ 'fixed-header': fixedHeader }">
        <navbar />
        <tags-view v-if="tagsView" />
      </div>
      <app-main />
      <right-panel v-if="showSettings">
        <settings />
      </right-panel>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { computed } from 'vue'
import { useAppStore } from '@/stores/app'
import { useSettingsStore } from '@/stores/settings'
import { AppMain, Navbar, Settings, Sidebar, TagsView } from './components'
import RightPanel from '@/components/RightPanel/index.vue'
import useResize from './hooks/useResize'

const appStore = useAppStore()
const settingsStore = useSettingsStore()

const sidebar = computed(() => appStore.sidebar)
const device = computed(() => appStore.device)
const fixedHeader = computed(() => settingsStore.fixedHeader)
const tagsView = computed(() => settingsStore.tagsView)
const showSettings = computed(() => settingsStore.showSettings)

const classObj = computed(() => ({
  hideSidebar: !sidebar.value.opened,
  openSidebar: sidebar.value.opened,
  withoutAnimation: sidebar.value.withoutAnimation,
  mobile: device.value === 'mobile'
}))

const handleClickOutside = () => {
  appStore.closeSideBar(false)
}

useResize()
</script>

<style lang="scss" scoped>
@use '@/styles/_variables' as v;

.app-wrapper {
  position: relative;
  height: 100%;
  width: 100%;

  &::after {
    content: '';
    display: table;
    clear: both;
  }
}

.drawer-bg {
  background: #000;
  opacity: 0.3;
  width: 100%;
  top: 0;
  height: 100%;
  position: absolute;
  z-index: 999;
}

.main-container {
  min-height: 100%;
  transition: margin-left .28s;
  margin-left: v.$sidebar-width;
  position: relative;
}

.sidebar-container {
  transition: width 0.28s;
  width: v.$sidebar-width !important;
  height: 100%;
  position: fixed;
  font-size: 0px;
  top: 0;
  bottom: 0;
  left: 0;
  z-index: 1001;
  overflow: hidden;
}

.fixed-header {
  position: fixed;
  top: 0;
  right: 0;
  z-index: 9;
  width: calc(100% - #{v.$sidebar-width});
  transition: width 0.28s;
}

.hideSidebar {
  .main-container {
    margin-left: v.$sidebar-hide-width;
  }
  
  .sidebar-container {
    width: v.$sidebar-hide-width !important;
  }

  .fixed-header {
    width: calc(100% - #{v.$sidebar-hide-width});
  }
}

.mobile {
  .main-container {
    margin-left: 0;
  }

  .sidebar-container {
    transition: transform .28s;
    width: v.$sidebar-width !important;
  }

  &.openSidebar {
    position: fixed;
    top: 0;
  }

  &.hideSidebar {
    .sidebar-container {
      pointer-events: none;
      transition-duration: 0.3s;
      transform: translate3d(-#{v.$sidebar-width}, 0, 0);
    }
  }
}

.withoutAnimation {
  .main-container,
  .sidebar-container {
    transition: none;
  }
}
</style> 