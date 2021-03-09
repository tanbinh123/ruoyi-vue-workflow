import request from '@/utils/request'

// 查询部署列表
export function listInstance(query) {
  return request({
    url: '/activiti7/processInstance/processInstanceList',
    method: 'get',
    params: query
  })
}
// ?current=' + pagination.current + "&pageSize=" + pagination.pageSize

// 挂起  instanceId: record.id
export function doHangUp(instanceId) {
  return request({
    url: '/activiti7/processInstance/suspendInstance?instanceId=' + instanceId,
    method: 'get',
  })
}

// 激活  instanceId: record.id
export function doActive(instanceId) {
  return request({
    url: '/activiti7/processInstance/resumeInstance?instanceId=' + instanceId,
    method: 'get',
  })
}

// 删除  instanceId: record.id
export function removeInc(instanceId) {
  return request({
    url: '/activiti7/processInstance/delInstance?instanceId=' + instanceId,
    method: 'get',
  })
}
