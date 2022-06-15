const UploadFileApi = {
  upLoadFile: '/imageUpload/imgUploadFile'
}

export default UploadFileApi
// axios
import request from '@/utils/request'

export function upLoadFile(data) {
  return request({
    url: UploadFileApi.upLoadFile,
    method: 'post',
    data,
    hideloading: true // 隐藏 loading 组件
  })
}

