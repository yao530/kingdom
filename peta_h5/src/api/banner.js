const BannarApi = {
  getList: '/bannerEntity/getList'
}

export default BannarApi
// axios
import request from '@/utils/request'

export function getList(data) {
  return request({
    url: BannarApi.getList,
    method: 'post',
    data,
    hideloading: true // 隐藏 loading 组件
  })
}
