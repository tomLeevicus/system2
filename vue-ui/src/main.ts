import { createApp } from 'vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import '@/styles/index.scss'
import App from './App.vue'
import router from './router'
import { createPinia } from 'pinia'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'

// 导入全局组件
import GlobalComponents from './components'
// 导入自定义指令
import Directives from './directives'
// 导入权限控制
import './permission'

const app = createApp(App)

// 注册 Element Plus 图标
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}

// 注册全局组件
app.use(GlobalComponents)
// 注册自定义指令
app.use(Directives)

// 全局错误处理
import { errorHandler } from './utils/error-handler'
app.config.errorHandler = errorHandler

app.use(createPinia())
   .use(router)
   .use(ElementPlus)
   .mount('#app') 