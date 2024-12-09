/**
 * 验证工具类
 */

/**
 * 判断是否为外部链接
 */
export function isExternal(path: string): boolean {
  return /^(https?:|mailto:|tel:)/.test(path)
}

/**
 * 判断是否为数组
 */
export function isArray(arg: any): arg is any[] {
  if (typeof Array.isArray === 'undefined') {
    return Object.prototype.toString.call(arg) === '[object Array]'
  }
  return Array.isArray(arg)
}

/**
 * 判断是否为空
 */
export function isEmpty(value: any): boolean {
  if (value === null || value === undefined) {
    return true
  }
  if (typeof value === 'string' && value.trim() === '') {
    return true
  }
  if (isArray(value) && value.length === 0) {
    return true
  }
  if (value instanceof Object && Object.keys(value).length === 0) {
    return true
  }
  return false
}

/**
 * 判断是否是字符串
 */
export function isString(str: any): boolean {
  return typeof str === 'string' || str instanceof String
}

/**
 * 判断是否为URL
 */
export function isValidURL(url: string): boolean {
  const reg = /^(https?|ftp):\/\/([a-zA-Z0-9.-]+(:[a-zA-Z0-9.&%$-]+)*@)*((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]?)(\.(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9]?[0-9])){3}|([a-zA-Z0-9-]+\.)*[a-zA-Z0-9-]+\.(com|edu|gov|int|mil|net|org|biz|arpa|info|name|pro|aero|coop|museum|[a-zA-Z]{2}))(:[0-9]+)*(\/($|[a-zA-Z0-9.,?'\\+&%$#=~_-]+))*$/
  return reg.test(url)
}

/**
 * 判断是否为邮箱
 */
export function isEmail(email: string): boolean {
  const reg = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
  return reg.test(email)
}

/**
 * 判断是否为手机号
 */
export function isMobile(mobile: string): boolean {
  return /^1[3-9]\d{9}$/.test(mobile)
}

/**
 * 判断是否为身份证号
 */
export function isIdCard(idCard: string): boolean {
  return /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/.test(idCard)
}

/**
 * 验证流程定义Key
 */
export function validateProcessKey(key: string): boolean {
  return /^[a-zA-Z][a-zA-Z0-9_]*$/.test(key)
}

/**
 * 验证流程名称
 */
export function validateProcessName(name: string): boolean {
  return name.length >= 2 && name.length <= 50
}

/**
 * 验证请假天数
 */
export function validateLeaveDays(days: number): boolean {
  return days >= 0.5 && days <= 365
}

/**
 * 验证金额
 */
export function validateAmount(amount: number): boolean {
  return amount > 0 && amount <= 1000000
}

/**
 * 验证审批意见
 */
export function validateComment(comment: string): boolean {
  return comment.length <= 500
}

/**
 * 验证文件大小
 */
export function validateFileSize(size: number): boolean {
  const maxSize = 10 * 1024 * 1024 // 10MB
  return size <= maxSize
}

/**
 * 验证文件类型
 */
export function validateFileType(type: string): boolean {
  const allowTypes = ['image/jpeg', 'image/png', 'application/pdf']
  return allowTypes.includes(type)
} 