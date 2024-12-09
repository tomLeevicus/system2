<template>
  <div class="dashboard-container">
    <div class="dashboard-text">
      欢迎 {{ name }}
    </div>
  </div>
</template>

<script lang="ts" setup>
import { computed } from 'vue'
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()
const name = computed(() => userStore.name)
</script>

<style lang="scss" scoped>
.dashboard {
  &-container {
    padding: 30px;
  }

  &-text {
    font-size: 30px;
    line-height: 46px;
  }
}
</style> 