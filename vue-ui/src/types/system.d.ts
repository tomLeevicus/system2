// 菜单信息
export interface MenuInfo {
  menuId: number
  parentId: number
  menuName: string
  orderNum: number
  path: string
  component: string
  query?: string
  isFrame: boolean
  isCache: boolean
  menuType: string
  visible: boolean
  status: string
  perms?: string
  icon?: string
  createBy?: string
  createTime?: string
  updateBy?: string
  updateTime?: string
  remark?: string
  children?: MenuInfo[]
}

// 角色信息
export interface RoleInfo {
  roleId: number
  roleName: string
  roleKey: string
  roleSort: number
  dataScope: string
  menuCheckStrictly: boolean
  deptCheckStrictly: boolean
  status: string
  delFlag: string
  createBy?: string
  createTime?: string
  updateBy?: string
  updateTime?: string
  remark?: string
  menuIds?: number[]
}

// 用户信息
export interface UserInfo {
  userId: number
  userName: string
  nickName: string
  deptId?: number
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

export interface UserForm {
  userId?: number
  username: string
  name: string
  deptId?: number
  email?: string
  phonenumber?: string
  sex?: string
  avatar?: string
  password?: string
  status?: string
  roleIds?: number[]
  postIds?: number[]
}

// 角色相关类型
export interface RoleInfo {
  roleId: number
  roleName: string
  roleKey: string
  roleSort: number
  dataScope: string
  menuCheckStrictly: boolean
  deptCheckStrictly: boolean
  status: string
  delFlag?: string
  createBy?: string
  createTime?: string
  updateBy?: string
  updateTime?: string
  remark?: string
  menuIds?: number[]
}

export interface RoleForm {
  roleId?: number
  roleName: string
  roleKey: string
  roleSort: number
  status: string
  menuIds: number[]
  remark?: string
}

// 菜单相关类型
export interface MenuItem {
  menuId: number
  menuName: string
  parentId: number
  orderNum: number
  path: string
  component: string
  query?: string
  isFrame: number
  isCache: number
  menuType: string
  visible: string
  status: string
  perms?: string
  icon?: string
  children?: MenuItem[]
}

export interface MenuForm {
  menuId?: number
  menuName: string
  parentId: number
  orderNum: number
  path: string
  component: string
  query?: string
  isFrame?: number
  isCache?: number
  menuType: string
  visible?: string
  status?: string
  perms?: string
  icon?: string
} 