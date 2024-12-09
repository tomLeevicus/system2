<template>
  <div class="right-panel-wrapper">
    <el-drawer
      v-model="show"
      :with-header="false"
      :size="width"
      direction="rtl"
      custom-class="right-panel"
    >
      <div class="right-panel-items">
        <slot />
      </div>
    </el-drawer>
  </div>
</template>

<script lang="ts" setup>
import { computed } from 'vue'

const props = defineProps({
  modelValue: {
    type: Boolean,
    default: false
  },
  width: {
    type: String,
    default: '300px'
  }
})

const emit = defineEmits(['update:modelValue'])

const show = computed({
  get: () => props.modelValue,
  set: (val) => emit('update:modelValue', val)
})
</script>

<style lang="scss">
.right-panel {
  background-color: var(--el-bg-color);
  box-shadow: 0px 0px 15px 0px rgba(0, 0, 0, .05);

  .el-drawer__body {
    padding: 0;
  }

  .right-panel-items {
    overflow-y: auto;
    height: 100%;
  }
}
</style> 