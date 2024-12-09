import request from '@/utils/request'

// 查询角色列表
export function listRole(params?: any) {
  return request({
    url: '/system/role/list',
    method: 'get',
    params
  })
}

// 查询角色详细
export function getRole(roleId: number) {
  return request({
    url: `/system/role/${roleId}`,
    method: 'get'
  })
}

// 新增角色
export function addRole(data: RoleForm) {
  return request({
    url: '/system/role',
    method: 'post',
    data
  })
}

// 修改角色
export function updateRole(data: RoleForm) {
  return request({
    url: '/system/role',
    method: 'put',
    data
  })
}

// 删除角色
export function deleteRole(roleId: number) {
  return request({
    url: `/system/role/${roleId}`,
    method: 'delete'
  })
}

// 修改角色状态
export function changeRoleStatus(roleId: number, status: string) {
  return request({
    url: `/system/role/${roleId}/status`,
    method: 'put',
    data: { status }
  })
}

// 导出类型定义
export interface RoleForm {
  roleId?: number
  roleName: string
  roleKey: string
  roleSort: number
  status: string
  remark?: string
  menuIds?: number[]
}

export interface RoleInfo {
  roleId: number
  roleName: string
  roleKey: string
  roleSort: number
  status: string
  remark: string
  createTime: string
  menuIds: number[]
} 