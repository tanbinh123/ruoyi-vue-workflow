import request from '@/utils/request'

// 查询部署列表
export function listTask(query) {
  return request({
    url: '/activiti7/task/taskByUser',
    method: 'get',
    params: query
  })
}

// /activiti7/task/completeTask/

// 办理任务
export function handleTask(taskId) {
  return request({
    url: '/activiti7/task/completeTask/' + taskId,
    method: 'get',
  })
}
// ?current=' + pagination.current + "&pageSize=" + pagination.pageSize
