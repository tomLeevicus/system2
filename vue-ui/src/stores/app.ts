import { defineStore } from 'pinia'
import { ref } from 'vue'
import { CACHE_KEY } from '@/constants/cache-keys'

export type DeviceType = 'desktop' | 'mobile' | 'tablet'

export const useAppStore = defineStore('app', () => {
  // 侧边栏状态
  const sidebar = ref({
    opened: localStorage.getItem(CACHE_KEY.SIDEBAR_STATUS) !== '0',
    withoutAnimation: false
  })

  // 设备类型
  const device = ref<DeviceType>('desktop')

  // 屏幕大小
  const size = ref<'default' | 'small' | 'large'>('default')

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
    sidebar.value.opened = false
    sidebar.value.withoutAnimation = withoutAnimation
    localStorage.setItem(CACHE_KEY.SIDEBAR_STATUS, '0')
  }

  // 切换设备类型
  function toggleDevice(val: DeviceType) {
    device.value = val
  }

  // 设置屏幕大小
  function setSize(val: 'default' | 'small' | 'large') {
    size.value = val
  }

  return {
    device,
    sidebar,
    size,
    toggleDevice,
    closeSideBar,
    toggleSideBar,
    setSize
  }
}) 