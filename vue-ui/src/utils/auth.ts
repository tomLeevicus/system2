import { CACHE_KEY } from '@/constants/cache-keys'

const TokenKey = CACHE_KEY.TOKEN

// 获取 token
export function getToken(): string | null {
  return localStorage.getItem(TokenKey)
}

// 设置 token
export function setToken(token: string): void {
  localStorage.setItem(TokenKey, token)
}

// 移除 token
export function removeToken(): void {
  localStorage.removeItem(TokenKey)
}

// 获取用户信息
export function getUserInfo(): any {
  const userInfo = localStorage.getItem(CACHE_KEY.USER_INFO)
  return userInfo ? JSON.parse(userInfo) : null
}

// 设置用户信息
export function setUserInfo(userInfo: any): void {
  localStorage.setItem(CACHE_KEY.USER_INFO, JSON.stringify(userInfo))
}

// 移除用户信息
export function removeUserInfo(): void {
  localStorage.removeItem(CACHE_KEY.USER_INFO)
}

// 清除所有认证信息
export function clearAuth(): void {
  removeToken()
  removeUserInfo()
} 