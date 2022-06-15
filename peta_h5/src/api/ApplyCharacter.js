const ApplyCharacterApi = {
  getUserApply: '/applyCreator/getDetail',
  applyCreator: '/applyCreator/apply',
  applySetting: '/creatorApplySetting/getSetting'
}

export default ApplyCharacterApi
// axios
import request from '@/utils/request'

export function applyCreator(data) {
  return request({
    url: ApplyCharacterApi.applyCreator,
    method: 'post',
    data,
    hideloading: false // 隐藏 loading 组件
  })
}

export function getApplySetting(data) {
  return request({
    url: ApplyCharacterApi.applySetting,
    method: 'post',
    data,
    hideloading: true // 隐藏 loading 组件
  })
}

