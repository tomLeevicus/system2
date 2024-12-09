export interface LoginForm {
  username: string
  password: string
  code: string
  uuid: string
}

export interface LoginResponse {
  token: string
}

export interface UserInfo {
  userId: number
  username: string
  name: string
  avatar: string
  roles: string[]
  permissions: string[]
}

export interface CaptchaResult {
  uuid: string
  img: string
} 