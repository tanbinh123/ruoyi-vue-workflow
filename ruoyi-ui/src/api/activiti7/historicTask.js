import request from '@/utils/request'

// 查询历史任务列表
export function listHistoricTask(query) {
  return request({
    url: '/activiti7/historic/userHistoricTask',
    method: 'get',
    params: query
  })
}
