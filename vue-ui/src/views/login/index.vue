<template>
  <div class="login-container">
    <el-form
      ref="loginFormRef"
      :model="loginForm"
      :rules="loginRules"
      class="login-form"
      autocomplete="on"
      label-position="left"
    >
      <div class="title-container">
        <h3 class="title">后台管理系统</h3>
      </div>

      <el-form-item prop="username">
        <el-input
          v-model="loginForm.username"
          placeholder="用户名"
          type="text"
          tabindex="1"
          autocomplete="on"
        >
          <template #prefix>
            <el-icon><user /></el-icon>
          </template>
        </el-input>
      </el-form-item>

      <el-form-item prop="password">
        <el-input
          v-model="loginForm.password"
          placeholder="密码"
          :type="passwordVisible ? 'text' : 'password'"
          tabindex="2"
        >
          <template #prefix>
            <el-icon><lock /></el-icon>
          </template>
          <template #suffix>
            <el-icon class="show-pwd" @click="passwordVisible = !passwordVisible">
              <view v-if="passwordVisible" />
              <hide v-else />
            </el-icon>
          </template>
        </el-input>
      </el-form-item>

      <el-form-item prop="code">
        <el-input
          v-model="loginForm.code"
          placeholder="验证码"
          style="width: 63%"
          @keyup.enter="handleLogin"
        >
          <template #prefix>
            <el-icon><picture /></el-icon>
          </template>
        </el-input>
        <div class="login-code">
          <img :src="codeUrl" @click="getCode" class="captcha" />
        </div>
      </el-form-item>

      <el-button
        :loading="loading"
        type="primary"
        style="width: 100%; margin-bottom: 30px"
        @click.prevent="handleLogin"
      >
        登录
      </el-button>
    </el-form>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import type { FormInstance } from 'element-plus'
import { User, Lock, View, Hide, Picture } from '@element-plus/icons-vue'
import { useUserStore } from '@/stores/user'
import { usePermissionStore } from '@/stores/permission'
import { login, getCaptcha } from '@/api/auth'

const router = useRouter()
const userStore = useUserStore()
const permissionStore = usePermissionStore()

const loginFormRef = ref<FormInstance>()
const loading = ref(false)
const passwordVisible = ref(false)
const codeUrl = ref('')

const loginForm = ref({
  username: '',
  password: '',
  code: '',
  uuid: ''
})

const loginRules = {
  username: [{ required: true, trigger: 'blur', message: '请输入用户名' }],
  password: [{ required: true, trigger: 'blur', message: '请输入密码' }],
  code: [{ required: true, trigger: 'blur', message: '请输入验证码' }]
}

function getCode() {
  getCaptcha().then(res => {
    codeUrl.value = 'data:image/gif;base64,' + res.data.img
    loginForm.value.uuid = res.data.uuid
  })
}

async function handleLogin() {
  if (!loginFormRef.value) return
  
  try {
    await loginFormRef.value.validate()
    loading.value = true
    
    // 登录
    await userStore.login(loginForm.value)
    // 获取用户信息
    await userStore.getInfo()
    // 生成路由
    const accessRoutes = await permissionStore.generateRoutes()
    // 动态添加路由
    accessRoutes.forEach(route => {
      router.addRoute(route as any)
    })
    // 跳转到首页或重定向页面
    const redirect = router.currentRoute.value.query.redirect?.toString()
    router.push(redirect || '/index')
    
    ElMessage.success('登录成功')
  } catch (error: any) {
    console.error('Login error:', error)
    ElMessage.error(error.message || '登录失败')
    getCode()
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  getCode()
})
</script>

<style lang="scss" scoped>
.login-container {
  min-height: 100%;
  width: 100%;
  background-color: #2d3a4b;
  overflow: hidden;

  .login-form {
    position: relative;
    width: 520px;
    max-width: 100%;
    padding: 160px 35px 0;
    margin: 0 auto;
    overflow: hidden;
  }

  .title-container {
    position: relative;

    .title {
      font-size: 26px;
      color: #eee;
      margin: 0 auto 40px auto;
      text-align: center;
      font-weight: bold;
    }
  }

  .login-code {
    width: 33%;
    height: 40px;
    float: right;

    img {
      cursor: pointer;
      vertical-align: middle;
    }
  }

  .captcha {
    height: 40px;
    width: 100%;
  }
}
</style> 