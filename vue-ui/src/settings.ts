interface Settings {
  /**
   * 网页标题
   */
  title: string
  
  /**
   * 侧边栏主题 深色主题theme-dark，浅色主题theme-light
   */
  sideTheme: string
  
  /**
   * 是否系统布局配置
   */
  showSettings: boolean

  /**
   * 是否显示顶部导航
   */
  topNav: boolean

  /**
   * 是否显示 Tags View
   */
  tagsView: boolean

  /**
   * 是否固定头部
   */
  fixedHeader: boolean

  /**
   * 是否显示logo
   */
  sidebarLogo: boolean

  /**
   * 是否显示动态标题
   */
  dynamicTitle: boolean

  /**
   * 主题色
   */
  theme: string
}

const settings: Settings = {
  title: '后台管理系统',
  sideTheme: 'theme-dark',
  showSettings: true,
  topNav: false,
  tagsView: true,
  fixedHeader: false,
  sidebarLogo: true,
  dynamicTitle: false,
  theme: '#409EFF'
}

export default settings
export type { Settings }