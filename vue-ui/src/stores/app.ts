import { defineStore } from 'pinia'
import { ref } from 'vue'
import { CACHE_KEY } from '@/constants/cache-keys'

export type DeviceType = 'desktop' | 'mobile'

export interface AppState {
  device: DeviceType
  sidebar: {
    opened: boolean
    withoutAnimation: boolean
  }
}

export const useAppStore = defineStore('app', () => {
  // 侧边栏状态
  const sidebar = ref({
    opened: localStorage.getItem(CACHE_KEY.SIDEBAR_STATUS) !== '0',
    withoutAnimation: false
  })

  // 设备类型
  const device = ref<DeviceType>('desktop')

  // 切换侧边栏
  function toggleSideBar() {
    sidebar.value.opened = !sidebar.value.opened
    sidebar.value.withoutAnimation = false
    if (sidebar.value.opened) {
      localStorage.setItem(CACHE_KEY.SIDEBAR_STATUS, '1')
    } else {
      localStorage.setItem(CACHE_KEY.SIDEBAR_STATUS, '0')
    }
  }

  // 关闭侧边栏
  function closeSideBar(withoutAnimation: boolean) {
    localStorage.setItem(CACHE_KEY.SIDEBAR_STATUS, '0')
    sidebar.value.opened = false
    sidebar.value.withoutAnimation = withoutAnimation
  }

  // 切换设备类型
  function toggleDevice(val: DeviceType) {
    device.value = val
  }

  return {
    device,
    sidebar,
    toggleDevice,
    closeSideBar,
    toggleSideBar
  }
}) 