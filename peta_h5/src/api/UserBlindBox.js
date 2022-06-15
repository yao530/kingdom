const UserBlindBoxApi = {
  getList: '/collectionBlind/getMePage',
  getDetail: '/collectionBlind/getDetail',
  open: '/collectionBlind/openBox',
  transfer: '/collectionBlind/transformBox'
}

export default UserBlindBoxApi
// axios
import request from '@/utils/request'

export function getBoxs(data) {
  return request({
    url: UserBlindBoxApi.getList,
    method: 'post',
    data,
    hideloading: true // 隐藏 loading 组件
  })
}

export function openBox(data) {
  return request({
    url: UserBlindBoxApi.open,
    method: 'post',
    data,
    hideloading: false // 隐藏 loading 组件
  })
}

export function transferBox(data) {
  return request({
    url: UserBlindBoxApi.transfer,
    method: 'post',
    data,
    hideloading: false // 隐藏 loading 组件
  })
}
