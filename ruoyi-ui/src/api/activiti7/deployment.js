import request from '@/utils/request'

// 查询部署列表
export function listDeployment(query) {
  return request({
    url: '/activiti7/processDef/getDefinitions',
    method: 'get',
    params: query
  })
}
// ?current=' + pagination.current + "&pageSize=" + pagination.pageSize


// 启动流程实例
export function startInstance(data) {
  return request({
    url: '/activiti7/processInstance/startProcess',
    method: 'get',
    params: data
  })
}

// 删除部署
export function delDeployment(data) {
  return request({
    url: 'activiti7/processDef/delDefinition?depID=' + data,
    method: 'get'
  })
}

// 启动流程实例
export function uploadFile(data) {
  return request({
    url: '/activiti7/processDef/uploadBPMN',
    method: 'post',
    data: data
  })
}
