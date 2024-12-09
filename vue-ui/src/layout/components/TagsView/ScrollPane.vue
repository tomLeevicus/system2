<template>
  <el-scrollbar
    ref="scrollContainer"
    :vertical="false"
    class="scroll-container"
    @wheel.prevent="handleScroll"
  >
    <slot />
  </el-scrollbar>
</template>

<script lang="ts" setup>
import { ref, computed, onMounted, onBeforeUnmount } from 'vue'

const scrollWrapper = ref<HTMLElement>()
const scrollContainer = ref()

const emits = defineEmits(['scroll'])

const tagSpacing = 4

onMounted(() => {
  scrollWrapper.value = scrollContainer.value.$el.querySelector('.el-scrollbar__wrap')
})

function handleScroll(e: WheelEvent) {
  const eventDelta = (e as any).wheelDelta || -e.deltaY * 40
  const $scrollWrapper = scrollWrapper.value
  if (!$scrollWrapper) return

  $scrollWrapper.scrollLeft = $scrollWrapper.scrollLeft + eventDelta / 4
  emits('scroll')
}

function moveToTarget(currentTag: HTMLElement) {
  const $container = scrollContainer.value.$el
  const $containerWidth = $container.offsetWidth
  const $scrollWrapper = scrollWrapper.value
  if (!$scrollWrapper) return

  let firstTag = null
  let lastTag = null

  // find first and last tag
  const tagList = $container.querySelectorAll('.tags-view-item')
  if (tagList.length > 0) {
    firstTag = tagList[0]
    lastTag = tagList[tagList.length - 1]
  }
  if (!firstTag || !lastTag) return

  // determine if scroll is needed
  const beforeTagOffsetLeft = (currentTag as any).offsetLeft
  const afterTagOffsetLeft = currentTag.offsetLeft + currentTag.offsetWidth

  if (beforeTagOffsetLeft < $scrollWrapper.scrollLeft) {
    // target tag is to the left of the container
    $scrollWrapper.scrollLeft = beforeTagOffsetLeft - tagSpacing
  } else if (afterTagOffsetLeft > $scrollWrapper.scrollLeft + $containerWidth) {
    // target tag is to the right of the container
    $scrollWrapper.scrollLeft = afterTagOffsetLeft - $containerWidth + tagSpacing
  }
}

defineExpose({
  moveToTarget
})
</script>

<style lang="scss" scoped>
.scroll-container {
  white-space: nowrap;
  position: relative;
  overflow: hidden;
  width: 100%;
  
  :deep(.el-scrollbar__bar) {
    bottom: 0px;
  }
  
  :deep(.el-scrollbar__wrap) {
    height: 49px;
  }
}
</style> 