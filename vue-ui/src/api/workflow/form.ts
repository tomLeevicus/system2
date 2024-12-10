import request from '@/utils/request'

// 获取流程表单数据
export function getFormData(processInstanceId: string) {
  return request({
    url: `/workflow/form/${processInstanceId}`,
    method: 'get'
  })
}

// 保存流程表单数据
export function saveFormData(data: {
  processInstanceId: string
  formData: any
}) {
  return request({
    url: '/workflow/form/save',
    method: 'post',
    data
  })
}

// 提交流程表单
export function submitForm(data: {
  processDefinitionId: string
  businessKey: string
  formData: any
}) {
  return request({
    url: '/workflow/form/submit',
    method: 'post',
    data
  })
} 