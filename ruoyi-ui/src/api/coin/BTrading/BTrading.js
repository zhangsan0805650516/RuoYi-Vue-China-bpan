import request from '@/utils/request'

// 查询成交记录列表
export function listBTrading(query) {
  return request({
    url: '/coin/BTrading/list',
    method: 'get',
    params: query
  })
}

// 查询成交记录详细
export function getBTrading(id) {
  return request({
    url: '/coin/BTrading/' + id,
    method: 'get'
  })
}

// 新增成交记录
export function addBTrading(data) {
  return request({
    url: '/coin/BTrading',
    method: 'post',
    data: data
  })
}

// 修改成交记录
export function updateBTrading(data) {
  return request({
    url: '/coin/BTrading',
    method: 'put',
    data: data
  })
}

// 删除成交记录
export function delBTrading(id) {
  return request({
    url: '/coin/BTrading/' + id,
    method: 'delete'
  })
}
