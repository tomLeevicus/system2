<template>
  <div>
    <el-tooltip
      effect="dark"
      :content="isFullscreen ? '退出全屏' : '全屏'"
      placement="bottom"
    >
      <el-icon class="screenfull" @click="click">
        <component :is="isFullscreen ? 'Aim' : 'FullScreen'" />
      </el-icon>
    </el-tooltip>
  </div>
</template>

<script lang="ts" setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Aim, FullScreen } from '@element-plus/icons-vue'
import screenfull from 'screenfull'

const isFullscreen = ref(false)

function click() {
  if (!screenfull.isEnabled) {
    ElMessage.warning('你的浏览器不支持全屏')
    return
  }
  screenfull.toggle()
}

function change() {
  isFullscreen.value = screenfull.isFullscreen
}

onMounted(() => {
  if (screenfull.isEnabled) {
    screenfull.on('change', change)
  }
})

onUnmounted(() => {
  if (screenfull.isEnabled) {
    screenfull.off('change', change)
  }
})
</script>

<style lang="scss" scoped>
.screenfull {
  cursor: pointer;
  font-size: 20px;
  vertical-align: -5px !important;
}
</style> 