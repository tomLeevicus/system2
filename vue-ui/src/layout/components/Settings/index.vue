<template>
  <div class="drawer-container">
    <div class="drawer-item">
      <span>主题色</span>
      <theme-picker class="right-field" />
    </div>

    <div class="drawer-item">
      <span>开启 Tags-View</span>
      <el-switch v-model="tagsView" class="right-field" />
    </div>

    <div class="drawer-item">
      <span>固定 Header</span>
      <el-switch v-model="fixedHeader" class="right-field" />
    </div>

    <div class="drawer-item">
      <span>显示 Logo</span>
      <el-switch v-model="sidebarLogo" class="right-field" />
    </div>

    <div class="drawer-item">
      <span>动态标题</span>
      <el-switch v-model="dynamicTitle" class="right-field" />
    </div>
  </div>
</template>

<script lang="ts" setup>
import { computed } from 'vue'
import { useSettingsStore } from '@/stores/settings'
import ThemePicker from './ThemePicker.vue'

const settingsStore = useSettingsStore()

const tagsView = computed({
  get: () => settingsStore.tagsView,
  set: (val: boolean) => settingsStore.changeSetting({ key: 'tagsView', value: val })
})

const fixedHeader = computed({
  get: () => settingsStore.fixedHeader,
  set: (val: boolean) => settingsStore.changeSetting({ key: 'fixedHeader', value: val })
})

const sidebarLogo = computed({
  get: () => settingsStore.sidebarLogo,
  set: (val: boolean) => settingsStore.changeSetting({ key: 'sidebarLogo', value: val })
})

const dynamicTitle = computed({
  get: () => settingsStore.dynamicTitle,
  set: (val: boolean) => settingsStore.changeSetting({ key: 'dynamicTitle', value: val })
})
</script>

<style lang="scss" scoped>
.drawer-container {
  padding: 24px;
  font-size: 14px;
  line-height: 1.5;
  word-wrap: break-word;

  .drawer-item {
    color: rgba(0, 0, 0, .65);
    font-size: 14px;
    padding: 12px 0;
    display: flex;
    justify-content: space-between;
    align-items: center;

    .right-field {
      float: right;
    }
  }
}
</style> 