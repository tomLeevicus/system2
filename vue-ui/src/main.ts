import { createApp } from 'vue'
import { createPinia } from 'pinia'
import ElementPlus from 'element-plus'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'

// 导入样式
import 'normalize.css'
import 'element-plus/dist/index.css'
import '@/styles/index.scss'
import '@/styles/element-dark.css'

import App from './App.vue'
import router from './router'
import { hasRole, hasPermi } from './directives/permission/hasRole'

const app = createApp(App)

// 注册所有图标
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}

// 注册自定义指令
app.directive('hasRole', hasRole)
app.directive('hasPermi', hasPermi)

app.use(createPinia())
app.use(router)
app.use(ElementPlus)

app.mount('#app') 