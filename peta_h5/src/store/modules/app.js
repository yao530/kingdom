/*
 * @Author: lija 904050844@qq.com
 * @Date: 2022-06-04 20:32:54
 * @LastEditors: lija 904050844@qq.com
 * @LastEditTime: 2022-06-06 19:06:48
 * @FilePath: \peta_h5\src\store\modules\app.js
 * @Description:
 *
 * Copyright (c) 2022 by lija 904050844@qq.com, All Rights Reserved.
 */
const state = {
  userName: '',
  showShare: false
}
const mutations = {
  SET_USER_NAME(state, name) {
    state.userName = name
  },
  SET_SHOW_SHARE(state, data) {
    state.showShare = data
  }
}
const actions = {
  // 设置name
  setUserName({ commit }, name) {
    commit('SET_USER_NAME', name)
  },
  setShowShare({ commit }, data) {
    commit('SET_SHOW_SHARE', data)
  }
}
export default {
  state,
  mutations,
  actions
}
