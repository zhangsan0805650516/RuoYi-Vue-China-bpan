import request from '@/utils/request'

// 查询持仓明细列表
export function listBHoldDetail(query) {
  return request({
    url: '/coin/BHoldDetail/list',
    method: 'get',
    params: query
  })
}

// 查询持仓明细详细
export function getBHoldDetail(id) {
  return request({
    url: '/coin/BHoldDetail/' + id,
    method: 'get'
  })
}

// 新增持仓明细
export function addBHoldDetail(data) {
  return request({
    url: '/coin/BHoldDetail',
    method: 'post',
    data: data
  })
}

// 修改持仓明细
export function updateBHoldDetail(data) {
  return request({
    url: '/coin/BHoldDetail',
    method: 'put',
    data: data
  })
}

// 删除持仓明细
export function delBHoldDetail(id) {
  return request({
    url: '/coin/BHoldDetail/' + id,
    method: 'delete'
  })
}
