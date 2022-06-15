const VirtualCharacter = {
  getDetail: '/virtualCharacter/getDetail',
  getListByArt: '/virtualCharacter/getCreators'
}

export default VirtualCharacter
// axios
import request from '@/utils/request'

export function getCharactersByArt(data) {
  return request({
    url: VirtualCharacter.getListByArt,
    method: 'post',
    data,
    hideloading: true // 隐藏 loading 组件
  })
}
export function getCharacterDetail(data) {
  return request({
    url: VirtualCharacter.getDetail,
    method: 'post',
    data,
    hideloading: true // 隐藏 loading 组件
  })
}

