const StoryTaleApi = {
  getList: '/storyTales/getPage',
  getDetail: '/storyTales/getDetail'
}

export default StoryTaleApi
// axios
import request from '@/utils/request'

export function getStoryList(data) {
  return request({
    url: StoryTaleApi.getList,
    method: 'post',
    data,
    hideloading: true // 隐藏 loading 组件
  })
}
export function getStoryTaleDetail(data) {
  return request({
    url: StoryTaleApi.getDetail,
    method: 'post',
    data,
    hideloading: true // 隐藏 loading 组件
  })
}
