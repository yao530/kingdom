const UserCollectionApi = {
  getList: '/userCollections/getMainCollection',
  getDetail: '/userCollections/getDetail',
  getAllArtList: '/userCollections/getAllCollectionsByMainAvatar',
  setMainDisplay: '/userCollections/setMainDisplay'
}

export default UserCollectionApi
// axios
import request from '@/utils/request'

export function getCollectionList(data) {
  return request({
    url: UserCollectionApi.getList,
    method: 'post',
    data,
    hideloading: true // 隐藏 loading 组件
  })
}
export function getCollectionDetail(data) {
  return request({
    url: UserCollectionApi.getDetail,
    method: 'post',
    data,
    hideloading: true // 隐藏 loading 组件
  })
}

export function getAllCollection(data) {
  return request({
    url: UserCollectionApi.getAllArtList,
    method: 'post',
    data,
    hideloading: true // 隐藏 loading 组件
  })
}
export function setCollectionDisplay(data) {
  return request({
    url: UserCollectionApi.setMainDisplay,
    method: 'post',
    data,
    hideloading: true // 隐藏 loading 组件
  })
}
