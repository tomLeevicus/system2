<template>
  <div class="right-panel" :class="{ show: visible }">
    <div class="right-panel-background" @click="close" />
    <div class="right-panel-content">
      <div class="right-panel-header">
        <el-icon class="right-panel-close" @click="close">
          <Close />
        </el-icon>
      </div>
      <div class="right-panel-body">
        <slot />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'
import { Close } from '@element-plus/icons-vue'

const props = defineProps({
  modelValue: {
    type: Boolean,
    default: false
  },
  showClose: {
    type: Boolean,
    default: true
  }
})

const emit = defineEmits(['update:modelValue'])

const visible = ref(props.modelValue)

watch(
  () => props.modelValue,
  val => {
    visible.value = val
  }
)

function close() {
  visible.value = false
  emit('update:modelValue', false)
}
</script>

<style lang="scss" scoped>
.right-panel {
  position: fixed;
  top: 0;
  right: 0;
  z-index: 40;
  width: 100%;
  height: 100%;
  background: transparent;
  pointer-events: none;
  transform: translate3d(100%, 0, 0);
  transition: transform 0.3s;

  &.show {
    pointer-events: auto;
    transform: translate3d(0, 0, 0);
  }

  .right-panel-background {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: rgba(0, 0, 0, 0.4);
    cursor: pointer;
  }

  .right-panel-content {
    position: fixed;
    top: 0;
    right: 0;
    width: 300px;
    height: 100%;
    background: #fff;
    box-shadow: 0 0 15px 0 rgba(0, 0, 0, 0.05);

    .right-panel-header {
      position: relative;
      height: 50px;
      padding: 0 15px;
      border-bottom: 1px solid #dcdfe6;

      .right-panel-close {
        position: absolute;
        top: 50%;
        right: 15px;
        font-size: 20px;
        transform: translateY(-50%);
        cursor: pointer;
        color: #909399;

        &:hover {
          color: #409eff;
        }
      }
    }

    .right-panel-body {
      height: calc(100% - 50px);
      padding: 10px 15px;
      overflow-y: auto;
    }
  }
}
</style> 