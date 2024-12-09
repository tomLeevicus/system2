import request from '@/utils/request'
import type { MenuItem } from '@/types/system'

// 查询菜单列表
export function listMenu() {
  return request<MenuItem[]>({
    url: '/system/menu/list',
    method: 'get'
  })
}

// 查询菜单详细
export function getMenu(menuId: number) {
  return request<MenuItem>({
    url: `/system/menu/${menuId}`,
    method: 'get'
  })
}

// 新增菜单
export function addMenu(data: MenuItem) {
  return request({
    url: '/system/menu',
    method: 'post',
    data
  })
}

// 修改菜单
export function updateMenu(data: MenuItem) {
  return request({
    url: '/system/menu',
    method: 'put',
    data
  })
}

// 删除菜单
export function deleteMenu(menuId: number) {
  return request({
    url: `/system/menu/${menuId}`,
    method: 'delete'
  })
}

// 查询菜单树结构
export function getMenuTree() {
  return request<MenuItem[]>({
    url: '/system/menu/tree',
    method: 'get'
  })
} 