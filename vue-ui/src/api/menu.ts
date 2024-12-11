import request from '@/utils/request'

// 获取路由
export const getRouters = () => {
  return request({
    url: '/system/menu/getRouters',
    method: 'get'
  })
}

// 查询菜单列表
export const listMenu = (query?: any) => {
  return request({
    url: '/system/menu/list',
    method: 'get',
    params: query
  })
}

// 查询菜单详细
export const getMenu = (menuId: string | number) => {
  return request({
    url: `/system/menu/${menuId}`,
    method: 'get'
  })
}

// 查询菜单下拉树结构
export const treeselect = () => {
  return request({
    url: '/system/menu/treeselect',
    method: 'get'
  })
}

// 根据角色ID查询菜单下拉树结构
export const roleMenuTreeselect = (roleId: string | number) => {
  return request({
    url: `/system/menu/roleMenuTreeselect/${roleId}`,
    method: 'get'
  })
} 