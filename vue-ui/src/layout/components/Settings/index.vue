<template>
  <div class="setting-drawer">
    <h3 class="setting-drawer-title">系统布局配置</h3>

    <div class="setting-drawer-block">
      <h4>主题色</h4>
      <el-color-picker v-model="theme" @change="themeChange" />
    </div>

    <el-divider />

    <div class="setting-drawer-block">
      <h4>系统主题</h4>
      <el-switch
        v-model="sideTheme"
        inline-prompt
        :active-value="'theme-dark'"
        :inactive-value="'theme-light'"
        :active-text="'深色'"
        :inactive-text="'浅色'"
        @change="sideThemeChange"
      />
    </div>

    <el-divider />

    <div class="setting-drawer-block">
      <h4>导航模式</h4>
      <el-switch
        v-model="topNav"
        inline-prompt
        :active-text="'顶部'"
        :inactive-text="'侧边'"
        @change="topNavChange"
      />
    </div>

    <el-divider />

    <div class="setting-drawer-block">
      <h4>系统布局</h4>
      <div class="setting-drawer-item">
        <span>开启 Tags-View</span>
        <el-switch v-model="tagsView" @change="tagsViewChange" />
      </div>
      <div class="setting-drawer-item">
        <span>固定 Header</span>
        <el-switch v-model="fixedHeader" @change="fixedHeaderChange" />
      </div>
      <div class="setting-drawer-item">
        <span>显示 Logo</span>
        <el-switch v-model="sidebarLogo" @change="sidebarLogoChange" />
      </div>
      <div class="setting-drawer-item">
        <span>动态标题</span>
        <el-switch v-model="dynamicTitle" @change="dynamicTitleChange" />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { useSettingsStore } from '@/stores/settings'

const settingsStore = useSettingsStore()

const theme = ref(settingsStore.theme)
const sideTheme = ref(settingsStore.sideTheme)
const topNav = ref(settingsStore.topNav)
const tagsView = ref(settingsStore.tagsView)
const fixedHeader = ref(settingsStore.fixedHeader)
const sidebarLogo = ref(settingsStore.sidebarLogo)
const dynamicTitle = ref(settingsStore.dynamicTitle)

function themeChange(val: string) {
  settingsStore.changeSetting('theme', val)
}

function sideThemeChange(val: string) {
  settingsStore.changeSetting('sideTheme', val)
}

function topNavChange(val: boolean) {
  settingsStore.changeSetting('topNav', val)
}

function tagsViewChange(val: boolean) {
  settingsStore.changeSetting('tagsView', val)
}

function fixedHeaderChange(val: boolean) {
  settingsStore.changeSetting('fixedHeader', val)
}

function sidebarLogoChange(val: boolean) {
  settingsStore.changeSetting('sidebarLogo', val)
}

function dynamicTitleChange(val: boolean) {
  settingsStore.changeSetting('dynamicTitle', val)
}
</script>

<style lang="scss" scoped>
.setting-drawer {
  padding: 24px;
  font-size: 14px;

  .setting-drawer-title {
    margin-bottom: 12px;
    font-size: 16px;
  }

  .setting-drawer-block {
    margin-bottom: 24px;

    h4 {
      margin-bottom: 12px;
      font-size: 14px;
    }
  }

  .setting-drawer-item {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 12px;

    span {
      font-size: 14px;
    }
  }
}
</style> 