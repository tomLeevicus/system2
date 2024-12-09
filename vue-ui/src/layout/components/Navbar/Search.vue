<template>
  <div class="header-search">
    <el-autocomplete
      v-model="search"
      class="search-input"
      placeholder="搜索页面"
      :fetch-suggestions="querySearch"
      :trigger-on-focus="false"
      @select="handleSelect"
    >
      <template #prefix>
        <el-icon class="el-input__icon"><search /></el-icon>
      </template>
      <template #default="{ item }">
        <div class="search-item">
          <el-icon v-if="item.icon"><component :is="item.icon" /></el-icon>
          <span>{{ item.title }}</span>
        </div>
      </template>
    </el-autocomplete>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { Search } from '@element-plus/icons-vue'
import Fuse from 'fuse.js'
import type { SearchOption, SearchConfig } from '@/types/search'

const router = useRouter()
const search = ref('')
const options = ref<SearchOption[]>([])
const fuse = ref<Fuse<SearchOption>>()

// 搜索配置
const searchConfig: SearchConfig = {
  keys: ['title', 'path'],
  threshold: 0.3,
  location: 0,
  distance: 100,
  includeScore: true,
  minMatchCharLength: 2,
  shouldSort: true,
  findAllMatches: false,
  useExtendedSearch: false
}

// 初始化搜索
onMounted(() => {
  const routes = router.getRoutes()
  options.value = routes
    .filter(route => !route.meta?.hidden)
    .map(route => ({
      path: route.path,
      title: route.meta?.title || '',
      icon: route.meta?.icon
    }))

  fuse.value = new Fuse(options.value, searchConfig)
})

// 搜索
function querySearch(query: string, cb: (arg: SearchOption[]) => void) {
  if (query !== '' && fuse.value) {
    const results = fuse.value.search(query).map(result => result.item)
    cb(results)
  } else {
    cb([])
  }
}

// 选择
function handleSelect(item: SearchOption) {
  router.push(item.path)
  search.value = ''
}
</script>

<style lang="scss" scoped>
.header-search {
  .search-input {
    width: 210px;
    margin-left: 10px;
    transition: width 0.2s;
    
    :deep(.el-input__inner) {
      border-radius: 20px;
    }
    
    &:focus {
      width: 250px;
    }
  }
}

.search-item {
  display: flex;
  align-items: center;
  
  .el-icon {
    margin-right: 8px;
    width: 16px;
    color: #909399;
  }
}
</style> 