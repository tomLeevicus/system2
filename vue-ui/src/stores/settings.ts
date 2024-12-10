import { defineStore } from 'pinia'
import { ref } from 'vue'
import defaultSettings from '@/settings'

export const useSettingsStore = defineStore('settings', () => {
  // 基础配置
  const title = ref(defaultSettings.title)
  const theme = ref(defaultSettings.theme)
  const sideTheme = ref(defaultSettings.sideTheme)
  const showSettings = ref(defaultSettings.showSettings)
  const topNav = ref(defaultSettings.topNav)
  const tagsView = ref(defaultSettings.tagsView)
  const fixedHeader = ref(defaultSettings.fixedHeader)
  const sidebarLogo = ref(defaultSettings.sidebarLogo)
  const dynamicTitle = ref(defaultSettings.dynamicTitle)

  // 菜单配置
  const menu = ref(defaultSettings.menu)

  // 工作流配置
  const workflow = ref(defaultSettings.workflow)

  // 修改配置
  function changeSetting(key: keyof typeof defaultSettings, value: any) {
    switch (key) {
      case 'title':
        title.value = value
        break
      case 'theme':
        theme.value = value
        break
      case 'sideTheme':
        sideTheme.value = value
        break
      case 'showSettings':
        showSettings.value = value
        break
      case 'topNav':
        topNav.value = value
        break
      case 'tagsView':
        tagsView.value = value
        break
      case 'fixedHeader':
        fixedHeader.value = value
        break
      case 'sidebarLogo':
        sidebarLogo.value = value
        break
      case 'dynamicTitle':
        dynamicTitle.value = value
        break
      case 'menu':
        menu.value = { ...menu.value, ...value }
        break
      case 'workflow':
        workflow.value = { ...workflow.value, ...value }
        break
    }
  }

  return {
    title,
    theme,
    sideTheme,
    showSettings,
    topNav,
    tagsView,
    fixedHeader,
    sidebarLogo,
    dynamicTitle,
    menu,
    workflow,
    changeSetting
  }
}) 