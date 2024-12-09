declare module 'element-plus/dist/locale/zh-cn.mjs' {
  const locale: any
  export default locale
}

declare module 'element-plus' {
  import type { Plugin } from 'vue'
  const ElementPlus: Plugin
  export default ElementPlus

  export interface ColorPickerEmits {
    change: (val: string | null) => void
  }

  export const ElMessage: {
    success(message: string): void
    warning(message: string): void
    info(message: string): void
    error(message: string): void
  }

  export const ElMessageBox: {
    confirm(message: string, title: string, options?: {
      confirmButtonText?: string
      cancelButtonText?: string
      type?: 'success' | 'warning' | 'info' | 'error'
    }): Promise<void>
  }
} 