import request from '@/utils/request'
import type { LoginForm, LoginResponse, UserInfo } from '@/types/auth'

// 登录
export function login(data: LoginForm) {
  return request<LoginResponse>({
    url: '/auth/login',
    method: 'post',
    data
  })
}

// 获取用户信息
export function getInfo() {
  return request<UserInfo>({
    url: '/auth/info',
    method: 'get'
  })
}

// 登出
export function logout() {
  return request({
    url: '/auth/logout',
    method: 'post'
  })
}

// 获取验证码
export function getCaptcha() {
  return request<{
    img: string
    uuid: string
  }>({
    url: '/auth/captcha',
    method: 'get'
  })
} 