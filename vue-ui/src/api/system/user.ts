import request from '@/utils/request'
import type { UserForm } from '@/types/user'

// 查询用户列表
export function listUser(params?: any) {
  return request({
    url: '/system/user/list',
    method: 'get',
    params
  })
}

// 查询用户详细
export function getUser(userId: number) {
  return request({
    url: `/system/user/${userId}`,
    method: 'get'
  })
}

// 新增用户
export function addUser(data: UserForm) {
  return request({
    url: '/system/user',
    method: 'post',
    data
  })
}

// 修改用户
export function updateUser(data: UserForm) {
  return request({
    url: '/system/user',
    method: 'put',
    data
  })
}

// 删除用户
export function deleteUser(userId: number) {
  return request({
    url: `/system/user/${userId}`,
    method: 'delete'
  })
}

// 用户密码重置
export function resetUserPwd(userId: number, password: string) {
  return request({
    url: '/system/user/resetPwd',
    method: 'put',
    data: {
      userId,
      password
    }
  })
}

// 查询用户个人信息
export function getUserProfile() {
  return request({
    url: '/system/user/profile',
    method: 'get'
  })
}

// 修改用户个人信息
export function updateUserProfile(data: UserForm) {
  return request({
    url: '/system/user/profile',
    method: 'put',
    data
  })
}

// 用户密码修改
export function updateUserPwd(oldPassword: string, newPassword: string) {
  return request({
    url: '/system/user/profile/updatePwd',
    method: 'put',
    data: {
      oldPassword,
      newPassword
    }
  })
}

// 用户头像上传
export function uploadAvatar(data: FormData) {
  return request({
    url: '/system/user/profile/avatar',
    method: 'post',
    data,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
} 