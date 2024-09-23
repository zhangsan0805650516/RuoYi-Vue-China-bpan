import request from '@/utils/request'

// 查询合约交易列表
export function listBCoinContract(query) {
  return request({
    url: '/coin/BCoinContract/list',
    method: 'get',
    params: query
  })
}

// 查询合约交易详细
export function getBCoinContract(id) {
  return request({
    url: '/coin/BCoinContract/' + id,
    method: 'get'
  })
}

// 新增合约交易
export function addBCoinContract(data) {
  return request({
    url: '/coin/BCoinContract',
    method: 'post',
    data: data
  })
}

// 修改合约交易
export function updateBCoinContract(data) {
  return request({
    url: '/coin/BCoinContract',
    method: 'put',
    data: data
  })
}

// 删除合约交易
export function delBCoinContract(id) {
  return request({
    url: '/coin/BCoinContract/' + id,
    method: 'delete'
  })
}
