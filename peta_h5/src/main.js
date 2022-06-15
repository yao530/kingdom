/*
 * @Author: lija 904050844@qq.com
 * @Date: 2022-06-08 11:29:15
 * @LastEditors: lija 904050844@qq.com
 * @LastEditTime: 2022-06-09 15:39:36
 * @FilePath: \peta_h5\src\main.js
 * @Description:
 *
 * Copyright (c) 2022 by lija 904050844@qq.com, All Rights Reserved.
 */
// 兼容 IE
// https://github.com/zloirock/core-js/blob/master/docs/2019-03-19-core-js-3-babel-and-a-look-into-the-future.md#babelpolyfill
import 'core-js/stable'
import 'regenerator-runtime/runtime'

import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'

// 设置 js中可以访问 $cdn
import { $cdn } from '@/config'

// 规则检验
import test from '@/utils/test.js'
// 节流方法
import throttle from '@/utils/throttle.js'
const $l = {
  test,
  throttle
}
Vue.prototype.$cdn = $cdn
Vue.prototype.$l = $l

// 全局引入按需引入UI库 vant
import '@/plugins/vant'
// 引入全局样式
import '@/assets/css/index.scss'
// 移动端适配
import 'lib-flexible/flexible.js'
// 引入自定义样式
import '@/assets/css/self.scss'
// 引入阿里巴巴icon
import '@/assets/css/iconfont.css'

// filters
import './filters'
Vue.config.productionTip = false

// import * as filters from './filters/index'
// Object.keys(filters).forEach(key => {
//   Vue.filter(key, filters[key])
// })

new Vue({
  el: '#app',
  router,
  store,
  render: h => h(App)
})
