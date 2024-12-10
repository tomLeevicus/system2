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

  /**
   * 工作流配置
   */
  workflow: {
    /**
     * 是否启用工作流
     */
    enabled: boolean

    /**
     * 流程图配置
     */
    diagram: {
      /**
       * 流程图主题色
       */
      theme: string
      
      /**
       * 流程图字体
       */
      font: string
      
      /**
       * 流程图背景色
       */
      backgroundColor: string
    }

    /**
     * 表单配置
     */
    form: {
      /**
       * 表单最大宽度
       */
      maxWidth: number
      
      /**
       * 表单内边距
       */
      padding: number
    }
  }

  /**
   * 菜单配置
   */
  menu: {
    /**
     * 是否显示父级菜单
     */
    showParent: boolean

    /**
     * 是否只显示一级菜单
     */
    onlyFirstLevel: boolean

    /**
     * 是否显示图标
     */
    showIcon: boolean

    /**
     * 菜单主题 light | dark
     */
    theme: 'light' | 'dark'

    /**
     * 菜单宽度
     */
    width: number

    /**
     * 折叠时的宽度
     */
    collapsedWidth: number
  }
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
  theme: '#409EFF',
  workflow: {
    enabled: true,
    diagram: {
      theme: '#409EFF',
      font: 'Arial',
      backgroundColor: '#ffffff'
    },
    form: {
      maxWidth: 800,
      padding: 20
    }
  },
  menu: {
    showParent: true,
    onlyFirstLevel: false,
    showIcon: true,
    theme: 'dark',
    width: 210,
    collapsedWidth: 64
  }
}

export default settings
export type { Settings }