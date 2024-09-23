import request from '@/utils/request'

// 查询币种列表
export function listBCoin(query) {
  return request({
    url: '/coin/BCoin/list',
    method: 'get',
    params: query
  })
}

// 查询币种详细
export function getBCoin(id) {
  return request({
    url: '/coin/BCoin/' + id,
    method: 'get'
  })
}

// 新增币种
export function addBCoin(data) {
  return request({
    url: '/coin/BCoin',
    method: 'post',
    data: data
  })
}

// 修改币种
export function updateBCoin(data) {
  return request({
    url: '/coin/BCoin',
    method: 'put',
    data: data
  })
}

// 删除币种
export function delBCoin(id) {
  return request({
    url: '/coin/BCoin/' + id,
    method: 'delete'
  })
}
