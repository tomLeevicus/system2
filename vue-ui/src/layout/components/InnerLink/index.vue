<template>
  <div v-if="isExternal" class="inner-link">
    <a :href="path" target="_blank">
      <slot></slot>
    </a>
  </div>
  <div v-else class="inner-link" @click="goTo">
    <slot></slot>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'

const props = defineProps({
  path: {
    type: String,
    required: true
  }
})

const router = useRouter()
const route = useRoute()

const isExternal = computed(() => {
  return /^(https?:|mailto:|tel:)/.test(props.path)
})

const goTo = () => {
  if (route.path !== props.path) {
    router.push(props.path)
  }
}
</script>

<style lang="scss" scoped>
.inner-link {
  cursor: pointer;
  color: #303133;
  text-decoration: none;

  &:hover {
    color: #409EFF;
  }

  a {
    color: inherit;
    text-decoration: none;
  }
}
</style> 