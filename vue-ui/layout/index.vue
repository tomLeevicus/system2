<template>
  <div class="app-wrapper">
    <!-- 侧边栏 -->
    <sidebar class="sidebar-container" />
    
    <div class="main-container">
      <!-- 顶部导航栏 -->
      <navbar />
      
      <!-- 标签栏 -->
      <tags-view v-if="showTagsView" />
      
      <!-- 主要内容区 -->
      <app-main />
    </div>
  </div>
</template>

<script lang="ts" setup>
import { computed } from 'vue'
import { useSettingsStore } from '@/stores/settings'
import AppMain from './components/AppMain.vue'
import Navbar from './components/Navbar.vue'
import Sidebar from './components/Sidebar/index.vue'
import TagsView from './components/TagsView/index.vue'

const settingsStore = useSettingsStore()
const showTagsView = computed(() => settingsStore.showTagsView)
</script>

<style lang="scss" scoped>
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

.main-container {
  min-height: 100%;
  transition: margin-left 0.28s;
  margin-left: $sideBarWidth;
  position: relative;
}

.sidebar-container {
  position: fixed;
  top: 0;
  left: 0;
  bottom: 0;
  width: $sideBarWidth;
  height: 100%;
  overflow: hidden;
  background-color: $menuBg;
  transition: width 0.28s;
  z-index: 1001;
}
</style> 