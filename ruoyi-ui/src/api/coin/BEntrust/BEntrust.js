import request from '@/utils/request'

// 查询委托列表
export function listBEntrust(query) {
  return request({
    url: '/coin/BEntrust/list',
    method: 'get',
    params: query
  })
}

// 查询委托详细
export function getBEntrust(id) {
  return request({
    url: '/coin/BEntrust/' + id,
    method: 'get'
  })
}

// 新增委托
export function addBEntrust(data) {
  return request({
    url: '/coin/BEntrust',
    method: 'post',
    data: data
  })
}

// 修改委托
export function updateBEntrust(data) {
  return request({
    url: '/coin/BEntrust',
    method: 'put',
    data: data
  })
}

// 删除委托
export function delBEntrust(id) {
  return request({
    url: '/coin/BEntrust/' + id,
    method: 'delete'
  })
}
