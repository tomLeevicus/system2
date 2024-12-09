<template>
  <el-color-picker
    v-model="theme"
    class="theme-picker"
    popper-class="theme-picker-dropdown"
    :predefine="['#409EFF', '#1890ff', '#304156', '#212121', '#11a983', '#13c2c2', '#6959CD', '#f5222d']"
    @change="themeChange"
  />
</template>

<script lang="ts" setup>
import { computed } from 'vue'
import { useSettingsStore } from '@/stores/settings'

const settingsStore = useSettingsStore()

const theme = computed({
  get: () => settingsStore.theme,
  set: (val: string) => {
    settingsStore.changeSetting({ key: 'theme', value: val })
  }
})

const themeChange = (val: string | null) => {
  if (val) {
    const style = document.documentElement.style
    style.setProperty('--el-color-primary', val)
    for (let i = 1; i <= 9; i++) {
      style.setProperty(`--el-color-primary-light-${i}`, `${val}${i}0`)
    }
    style.setProperty('--el-color-primary-darken-1', `${val}AA`)
  }
}
</script>

<style lang="scss" scoped>
.theme-picker {
  float: right;
  height: 26px;
  margin: 0 8px;

  .el-color-picker__trigger {
    height: 26px !important;
    width: 26px !important;
    padding: 2px;
  }

  .el-color-picker__color {
    border-radius: 4px !important;
  }
}
</style> 