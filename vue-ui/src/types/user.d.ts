// 用户信息
export interface UserInfo {
  userId: number
  username: string
  name: string
  avatar: string
  roles: string[]
  permissions: string[]
}

// 登录表单
export interface LoginForm {
  username: string
  password: string
  code?: string
  uuid?: string
}

// 登录响应
export interface LoginResult {
  token: string
}

// 用户表单
export interface UserForm {
  userId?: number
  deptId?: number
  username: string
  name: string
  nickName: string
  email?: string
  phonenumber?: string
  sex: string
  avatar?: string
  password?: string
  status: string
  delFlag?: string
  loginIp?: string
  loginDate?: string
  createBy?: string
  createTime?: string
  updateBy?: string
  updateTime?: string
  remark?: string
  roleIds?: number[]
  postIds?: number[]
} 