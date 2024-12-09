declare module '*.vue' {
  import type { DefineComponent } from 'vue'
  const component: DefineComponent<{}, {}, any>
  export default component
}

declare module '*.scss' {
  const classes: { [key: string]: string }
  export default classes
}

declare module '*.css' {
  const classes: { [key: string]: string }
  export default classes
}

declare module '*.svg' {
  const content: string
  export default content
}

declare module '*.png' {
  const content: string
  export default content
}

declare module '*.jpg' {
  const content: string
  export default content
}

declare module '*.gif' {
  const content: string
  export default content
}

declare module '*.json' {
  const content: any
  export default content
}

declare module 'path-browserify' {
  import path from 'path'
  export default path
}

declare module 'screenfull' {
  interface Screenfull {
    isEnabled: boolean
    isFullscreen: boolean
    element: Element | null
    request(element?: Element): Promise<void>
    exit(): Promise<void>
    toggle(element?: Element): Promise<void>
    on(event: string, callback: () => void): void
    off(event: string, callback: () => void): void
  }
  const screenfull: Screenfull
  export default screenfull
}

// 环境变量类型
interface ImportMetaEnv {
  readonly VITE_APP_TITLE: string
  readonly VITE_APP_ENV: string
  readonly VITE_APP_BASE_API: string
}

interface ImportMeta {
  readonly env: ImportMetaEnv
}

// 全局变量类型
declare global {
  interface Window {
    // 是否存在无界
    __POWERED_BY_WUJIE__?: boolean
  }
} 