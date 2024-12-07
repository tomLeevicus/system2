export interface UserInfo {
  userId: number
  username: string
  nickname: string
  avatar: string
  roles: string[]
  permissions: string[]
}

export interface LoginForm {
  username: string
  password: string
  code?: string
  uuid?: string
}

export interface LoginResult {
  token: string
}

export interface Response<T = any> {
  code: number
  msg: string
  data: T
} 