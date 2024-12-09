<template>
  <div class="navbar">
    <hamburger :is-active="!sidebarStore.opened" class="hamburger-container" @toggleClick="toggleSideBar" />
    <breadcrumb class="breadcrumb-container" />
    <div class="right-menu">
      <el-dropdown class="avatar-container right-menu-item hover-effect" trigger="click">
        <div class="avatar-wrapper">
          <img :src="userStore.avatar" class="user-avatar">
          <span class="user-name">{{ userStore.name }}</span>
          <el-icon><CaretBottom /></el-icon>
        </div>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item @click="handleProfile">个人中心</el-dropdown-item>
            <el-dropdown-item divided @click="handleLogout">退出登录</el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { useUserStore } from '@/stores/user'
import { useSidebarStore } from '@/stores/sidebar'
import Breadcrumb from './Breadcrumb.vue'
import Hamburger from './Hamburger.vue'

const userStore = useUserStore()
const sidebarStore = useSidebarStore()
const router = useRouter()

const toggleSideBar = () => {
  sidebarStore.toggleSideBar()
}

const handleProfile = () => {
  router.push('/user/profile')
}

const handleLogout = () => {
  userStore.logout()
  router.push('/login')
}
</script>

<style lang="scss" scoped>
.navbar {
  height: $navbarHeight;
  overflow: hidden;
  position: relative;
  background: #fff;
  box-shadow: 0 1px 4px rgba(0,21,41,.08);

  .hamburger-container {
    line-height: $navbarHeight;
    height: 100%;
    float: left;
    padding: 0 15px;
    cursor: pointer;
    transition: background .3s;
    
    &:hover {
      background: rgba(0, 0, 0, .025)
    }
  }

  .breadcrumb-container {
    float: left;
    margin-left: 16px;
  }

  .right-menu {
    float: right;
    height: 100%;
    margin-right: 16px;

    .avatar-container {
      .avatar-wrapper {
        display: flex;
        align-items: center;
        
        .user-avatar {
          width: 24px;
          height: 24px;
          border-radius: 50%;
          margin-right: 8px;
        }
        
        .user-name {
          color: #606266;
          font-size: 14px;
        }
      }
    }
  }
}
</style> 