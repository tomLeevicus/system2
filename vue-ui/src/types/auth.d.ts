// 登录请求参数
export interface LoginForm {
  username: string
  password: string
  code: string
  uuid: string
}

// 登录响应数据
export interface LoginResult {
  token: string
}

// 用户信息
export interface UserInfo {
  userId: number
  username: string
  nickname: string
  name: string
  deptId: number
  email: string
  mobile: string
  sex: string
  avatar: string
  status: string
  roles: string[]
  permissions: string[]
}

// 验证码响应
export interface CaptchaResult {
  uuid: string
  img: string
} 