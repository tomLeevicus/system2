import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useSidebarStore = defineStore('sidebar', () => {
  const opened = ref(true)

  function toggleSideBar() {
    opened.value = !opened.value
  }

  return {
    opened,
    toggleSideBar
  }
}) 