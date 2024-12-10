// Element Plus 相关类型定义

// 表单验证规则类型
export interface FormItemRule {
  required?: boolean
  message?: string
  trigger?: 'blur' | 'change' | ['blur', 'change']
  min?: number
  max?: number
  type?: 'string' | 'number' | 'boolean' | 'array' | 'object' | 'date'
  validator?: (rule: any, value: any, callback: (error?: Error) => void) => void
}

export type FormRules = Record<string, FormItemRule[]>

// Element Plus 组件类型
declare module 'element-plus' {
  export interface ElMessageBoxOptions {
    confirmButtonText?: string
    cancelButtonText?: string
    type?: 'success' | 'warning' | 'info' | 'error'
  }

  export const ElMessage: {
    success(message: string): void
    warning(message: string): void
    info(message: string): void
    error(message: string): void
  }

  export const ElMessageBox: {
    confirm(message: string, title: string, options?: ElMessageBoxOptions): Promise<void>
  }
} 