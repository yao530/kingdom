const ArtCollectionApi = {
  getList: '/artCollection/getPageList',
  getDetail: '/artCollection/getDetail',
  getArtListByStory: '/artCollection/getStoryArtCollections',
  getArtSoldSetting: 'collectionSoldSetting/getDetail'
}

export default ArtCollectionApi
// axios
import request from '@/utils/request'

export function getArtList(data) {
  return request({
    url: ArtCollectionApi.getList,
    method: 'post',
    data,
    hideloading: false // 隐藏 loading 组件
  })
}
export function getArtDetail(data) {
  return request({
    url: ArtCollectionApi.getDetail,
    method: 'post',
    data,
    hideloading: false // 隐藏 loading 组件
  })
}

export function getStoryArts(data) {
  return request({
    url: ArtCollectionApi.getArtListByStory,
    method: 'post',
    data,
    hideloading: false // 隐藏 loading 组件
  })
}

export function getArtSoldSettig(data) {
  return request({
    url: ArtCollectionApi.getArtSoldSetting,
    method: 'post',
    data,
    hideloading: true // 隐藏 loading 组件
  })
}

