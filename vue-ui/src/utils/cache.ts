interface CacheConfig {
  type: 'localStorage' | 'sessionStorage'
  prefix: string
  expire: number
  isEncrypt: boolean
}

// 缓存配置
const config: CacheConfig = {
  type: 'localStorage', // 本地存储类型
  prefix: 'Admin_', // 名称前缀
  expire: 7 * 24 * 60 * 60, // 过期时间(秒)
  isEncrypt: false // 是否加密
}

// 缓存类
export class Cache {
  private storage: Storage
  private prefix: string

  constructor() {
    this.storage = config.type === 'localStorage' ? window.localStorage : window.sessionStorage
    this.prefix = config.prefix
  }

  private getKey(key: string): string {
    return `${this.prefix}${key}`
  }

  // 设置缓存
  set(key: string, value: any, expire: number = config.expire) {
    const stringData = JSON.stringify({
      value,
      time: Date.now(),
      expire: expire * 1000
    })

    this.storage.setItem(this.getKey(key), stringData)
  }

  // 获取缓存
  get<T = any>(key: string): T | null {
    const item = this.storage.getItem(this.getKey(key))
    if (item) {
      try {
        const data = JSON.parse(item)
        const { value, time, expire } = data
        // 在有效期内直接返回
        if (expire === null || expire === undefined || Date.now() - time < expire) {
          return value
        }
        this.remove(key)
      } catch (e) {
        return null
      }
    }
    return null
  }

  // 移除缓存
  remove(key: string) {
    this.storage.removeItem(this.getKey(key))
  }

  // 清空所有缓存
  clear() {
    this.storage.clear()
  }

  // 设置cookie
  setCookie(key: string, value: any, expire: number = config.expire) {
    document.cookie = `${this.getKey(key)}=${value}; Max-Age=${expire}`
  }

  // 获取cookie
  getCookie(key: string): string {
    const cookieArr = document.cookie.split('; ')
    for (let i = 0, length = cookieArr.length; i < length; i++) {
      const kv = cookieArr[i].split('=')
      if (kv[0] === this.getKey(key)) {
        return kv[1]
      }
    }
    return ''
  }

  // 移除cookie
  removeCookie(key: string) {
    this.setCookie(key, '', -1)
  }
}

export const cache = new Cache() 