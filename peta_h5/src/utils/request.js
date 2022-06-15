/*
 * @Author: lija 904050844@qq.com
 * @Date: 2022-06-04 20:32:54
 * @LastEditors: lija 904050844@qq.com
 * @LastEditTime: 2022-06-09 19:52:12
 * @FilePath: \peta_h5\src\utils\request.js
 * @Description:
 *
 * Copyright (c) 2022 by lija 904050844@qq.com, All Rights Reserved.
 */
import axios from 'axios'
// import store from '@/store'
import { Toast } from 'vant'
import router from '@/router'
// 根据环境不同引入不同api地址
import { baseApi } from '@/config'
// create an axios instance
const service = axios.create({
  baseURL: baseApi, // url = base api url + request url
  withCredentials: true, // send cookies when cross-domain requests
  timeout: 5000 // request timeout
})

// request拦截器 request interceptor
service.interceptors.request.use(
  config => {
    // 不传递默认开启loading
    if (!config.hideloading) {
      // loading
      Toast.loading({
        forbidClick: true
      })
    }
    localStorage.getItem('token') ? config.headers['x-auth-token'] = localStorage.getItem('token') : ''
    return config
  },
  error => {
    // do something with request error
    return Promise.reject(error)
  }
)
// respone拦截器
service.interceptors.response.use(
  response => {
    Toast.clear()
    const res = response.data
    if (res.code !== 200) {
      // 登录超时,重新登录
      if (res.code === 401) {
        // store.dispatch('Login').then(() => {
        //   location.reload()
        // })
        localStorage.removeItem('token')
        return router.push({ name: 'Login' })
      }
      return Promise.reject(res || 'error')
    } else {
      return Promise.resolve(res)
    }
  },
  error => {
    Toast.clear()
    return Promise.reject(error)
  }
)

export default service
