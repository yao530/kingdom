const UserGetArtCollectionApi = {
  userJoin: '/userJoinDrop/joinCollectionDrop'
}

export default UserGetArtCollectionApi
// axios
import request from '@/utils/request'

export function userJoinDrop(data) {
  return request({
    url: UserGetArtCollectionApi.userJoin,
    method: 'post',
    data,
    hideloading: false // 隐藏 loading 组件
  })
}
