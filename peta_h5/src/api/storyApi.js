const StoryApi = {
  getList: '/story/getMePage',
  getDetail: '/story/getDetail',
  getStorysByTale: '/story/getList'
}

export default StoryApi
// axios
import request from '@/utils/request'

export function getStoryList(data) {
  return request({
    url: StoryApi.getList,
    method: 'post',
    data,
    hideloading: true // 隐藏 loading 组件
  })
}
export function getStoryDetail(data) {
  return request({
    url: StoryApi.getDetail,
    method: 'post',
    data,
    hideloading: true // 隐藏 loading 组件
  })
}
export function getStorys(data) {
  return request({
    url: StoryApi.getStorysByTale,
    method: 'post',
    data,
    hideloading: true // 隐藏 loading 组件
  })
}
