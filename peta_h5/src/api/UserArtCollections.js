const UserArtCollectionApi = {
  getList: '/userCollections/getMainCollection',
  getDetail: '/userCollections/getDetail',
  setMainDisplay: 'userCollections/setMainDisplay',
  getAlumnCollection: 'userCollections/getAlumnCollections'
}

export default UserArtCollectionApi
// axios
import request from '@/utils/request'

export function getUserAvatars(data) {
  return request({
    url: UserArtCollectionApi.getList,
    method: 'post',
    data,
    hideloading: false // 隐藏 loading 组件
  })
}
export function getUserCollectionDetail(data) {
  return request({
    url: UserArtCollectionApi.getDetail,
    method: 'post',
    data,
    hideloading: false // 隐藏 loading 组件
  })
}

export function setAvatarMainDisplay(data) {
  return request({
    url: UserArtCollectionApi.setMainDisplay,
    method: 'post',
    data,
    hideloading: true // 隐藏 loading 组件
  })
}

export function getAlumnCollection(data) {
  return request({
    url: UserArtCollectionApi.getAlumnCollection,
    method: 'post',
    data,
    hideloading: true // 隐藏 loading 组件
  })
}
