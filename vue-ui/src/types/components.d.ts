declare module '*.vue' {
  import type { DefineComponent } from 'vue'
  const component: DefineComponent<{}, {}, any>
  export default component
}

declare module '@/layout/components' {
  import type { DefineComponent } from 'vue'
  export const AppMain: DefineComponent
  export const Navbar: DefineComponent
  export const Settings: DefineComponent
  export const Sidebar: DefineComponent
  export const TagsView: DefineComponent
} 