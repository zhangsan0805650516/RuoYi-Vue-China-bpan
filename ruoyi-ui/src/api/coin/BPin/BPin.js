import request from '@/utils/request'

// 查询插针列表
export function listBPin(query) {
  return request({
    url: '/coin/BPin/list',
    method: 'get',
    params: query
  })
}

// 查询插针详细
export function getBPin(id) {
  return request({
    url: '/coin/BPin/' + id,
    method: 'get'
  })
}

// 新增插针
export function addBPin(data) {
  return request({
    url: '/coin/BPin',
    method: 'post',
    data: data
  })
}

// 修改插针
export function updateBPin(data) {
  return request({
    url: '/coin/BPin',
    method: 'put',
    data: data
  })
}

// 删除插针
export function delBPin(id) {
  return request({
    url: '/coin/BPin/' + id,
    method: 'delete'
  })
}
