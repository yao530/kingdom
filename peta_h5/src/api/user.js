/*
 * @Author: lija 904050844@qq.com
 * @Date: 2022-06-04 20:32:54
 * @LastEditors: lija 904050844@qq.com
 * @LastEditTime: 2022-06-09 20:13:37
 * @FilePath: \peta_h5\src\api\user.js
 * @Description:
 *
 * Copyright (c) 2022 by lija 904050844@qq.com, All Rights Reserved.
 */
// axios
import request from '@/utils/request'

// 登录
export function login(data) {
  return request({
    url: '/login/loginBySmsCode',
    method: 'post',
    data
  })
}

// 用户信息 post 方法
export function getUserInfo(data) {
  return request({
    url: '/user/getDetail',
    method: 'post',
    data,
    hideloading: true
  })
}

export function verifyUserInfo(data) {
  return request({
    url: '/user/submitIdentityInfo',
    method: 'post',
    data,
    hideloading: true
  })
}
export function getSmsCode(data) {
  return request({
    url: '/verifySmsCode/getSmsCode',
    method: 'post',
    data,
    hideloading: true
  })
}
