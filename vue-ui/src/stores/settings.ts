import { defineStore } from 'pinia'
import { ref } from 'vue'
import defaultSettings from '@/settings'
import type { Settings } from '@/settings'

export const useSettingsStore = defineStore('settings', () => {
  // 侧边栏 Logo
  const sidebarLogo = ref(defaultSettings.sidebarLogo)
  // 固定 Header
  const fixedHeader = ref(defaultSettings.fixedHeader)
  // 显示 Tags View
  const tagsView = ref(defaultSettings.tagsView)
  // 显示设置面板
  const showSettings = ref(defaultSettings.showSettings)
  // 主题色
  const theme = ref(defaultSettings.theme)

  // 修改设置
  function changeSetting({ key, value }: { key: keyof Settings, value: any }) {
    const k = key as keyof typeof defaultSettings
    if (defaultSettings.hasOwnProperty(k)) {
      switch(key) {
        case 'sidebarLogo':
          sidebarLogo.value = value
          break
        case 'fixedHeader':
          fixedHeader.value = value
          break
        case 'tagsView':
          tagsView.value = value
          break
        case 'showSettings':
          showSettings.value = value
          break
        case 'theme':
          theme.value = value
          break
      }
    }
  }

  // 重置主题
  function resetTheme() {
    theme.value = defaultSettings.theme
  }

  return {
    sidebarLogo,
    fixedHeader,
    tagsView,
    showSettings,
    theme,
    changeSetting,
    resetTheme
  }
})

// 导出类型
export interface SettingsState {
  sidebarLogo: boolean
  fixedHeader: boolean
  showTagsView: boolean
  showSidebarSearch: boolean
  showSettings: boolean
  theme: string
} 