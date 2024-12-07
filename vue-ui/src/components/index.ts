import { App } from 'vue'
import Pagination from './Pagination/index.vue'
import SvgIcon from './SvgIcon/index.vue'
import IconSelect from './IconSelect/index.vue'

export default {
  install(app: App) {
    app.component('Pagination', Pagination)
    app.component('SvgIcon', SvgIcon)
    app.component('IconSelect', IconSelect)
  }
} 