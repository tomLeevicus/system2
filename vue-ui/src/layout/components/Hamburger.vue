<template>
  <div style="padding: 0 15px;" @click="toggleClick">
    <el-icon :class="{'is-active': isActive}">
      <Expand />
    </el-icon>
  </div>
</template>

<script lang="ts" setup>
import { Expand } from '@element-plus/icons-vue'

defineProps({
  isActive: {
    type: Boolean,
    default: false
  }
})

const emit = defineEmits(['toggleClick'])

const toggleClick = () => {
  emit('toggleClick')
}
</script>

<style lang="scss" scoped>
.el-icon {
  vertical-align: middle;
  width: 20px;
  height: 20px;
  cursor: pointer;
  transition: transform .38s;
  
  &.is-active {
    transform: rotate(180deg);
  }
}
</style> 