<template>
  <el-scrollbar
    ref="scrollbarRef"
    class="scroll-container"
    :vertical="false"
    @wheel.prevent="handleScroll"
  >
    <slot />
  </el-scrollbar>
</template>

<script lang="ts" setup>
import { ref } from 'vue'
import type { ElScrollbar } from 'element-plus'

const scrollbarRef = ref<InstanceType<typeof ElScrollbar>>()
const tagSpacing = 4

const handleScroll = (e: WheelEvent) => {
  const eventDelta = e.deltaX || e.deltaY
  scrollbarRef.value?.setScrollLeft(scrollbarRef.value?.wrapRef.scrollLeft + eventDelta)
}

const moveToTarget = (currentTag: HTMLElement) => {
  const container = scrollbarRef.value?.wrapRef
  if (!container) return
  
  const containerWidth = container.offsetWidth
  const tagList = container.querySelectorAll('.tags-view-item')
  let firstTag = null
  let lastTag = null

  if (tagList.length > 0) {
    firstTag = tagList[0]
    lastTag = tagList[tagList.length - 1]
  }

  if (firstTag === currentTag) {
    scrollbarRef.value?.setScrollLeft(0)
  } else if (lastTag === currentTag) {
    scrollbarRef.value?.setScrollLeft(container.scrollWidth - containerWidth)
  } else {
    const currentIndex = Array.from(tagList).indexOf(currentTag)
    const prevTag = tagList[currentIndex - 1]
    const nextTag = tagList[currentIndex + 1]
    
    const afterNextTagOffsetLeft = nextTag?.offsetLeft ?? 0
    const afterNextTagWidth = nextTag?.offsetWidth ?? 0
    const currentTagOffsetLeft = currentTag.offsetLeft
    const currentTagWidth = currentTag.offsetWidth
    const beforePrevTagOffsetLeft = prevTag?.offsetLeft ?? 0
    
    if (afterNextTagOffsetLeft + afterNextTagWidth > containerWidth + container.scrollLeft) {
      scrollbarRef.value?.setScrollLeft(afterNextTagOffsetLeft - (containerWidth - afterNextTagWidth) + tagSpacing)
    } else if (beforePrevTagOffsetLeft < container.scrollLeft) {
      scrollbarRef.value?.setScrollLeft(beforePrevTagOffsetLeft - tagSpacing)
    }
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