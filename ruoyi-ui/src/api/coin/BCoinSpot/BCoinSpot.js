import request from '@/utils/request'

// 查询现货交易列表
export function listBCoinSpot(query) {
  return request({
    url: '/coin/BCoinSpot/list',
    method: 'get',
    params: query
  })
}

// 查询现货交易详细
export function getBCoinSpot(id) {
  return request({
    url: '/coin/BCoinSpot/' + id,
    method: 'get'
  })
}

// 新增现货交易
export function addBCoinSpot(data) {
  return request({
    url: '/coin/BCoinSpot',
    method: 'post',
    data: data
  })
}

// 修改现货交易
export function updateBCoinSpot(data) {
  return request({
    url: '/coin/BCoinSpot',
    method: 'put',
    data: data
  })
}

// 删除现货交易
export function delBCoinSpot(id) {
  return request({
    url: '/coin/BCoinSpot/' + id,
    method: 'delete'
  })
}
