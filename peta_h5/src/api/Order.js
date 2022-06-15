const OrderApi = {
  getPageList: '/order/getList',
  getDetail: '/order/getDetail',
  placeOrder: '/order/placeOrder',
  pay: '/order/pay'
}

export default OrderApi
// axios
import request from '@/utils/request'

export function getOrderList(data) {
  return request({
    url: OrderApi.getPageList,
    method: 'post',
    data,
    hideloading: true // 隐藏 loading 组件
  })
}
export function getOrderDetail(data) {
  return request({
    url: OrderApi.getDetail,
    method: 'post',
    data,
    hideloading: true // 隐藏 loading 组件
  })
}

export function createOrder(data) {
  return request({
    url: OrderApi.placeOrder,
    method: 'post',
    data,
    hideloading: false // 隐藏 loading 组件
  })
}
export function pay(data) {
  return request({
    url: OrderApi.pay,
    method: 'post',
    data,
    hideloading: false // 隐藏 loading 组件
  })
}
