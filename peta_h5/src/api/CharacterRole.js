const CharacterRoleApi = {
  getList: '/characterRole/getOpenRoleList'
}

export default CharacterRoleApi
// axios
import request from '@/utils/request'

export function getRole(data) {
  return request({
    url: CharacterRoleApi.getList,
    method: 'post',
    data,
    hideloading: true // 隐藏 loading 组件
  })
}

