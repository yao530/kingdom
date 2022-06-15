/*
 * @Author: lija 904050844@qq.com
 * @Date: 2022-06-04 20:32:54
 * @LastEditors: lija 904050844@qq.com
 * @LastEditTime: 2022-06-09 15:47:45
 * @FilePath: \peta_h5\src\router\index.js
 * @Description:
 *
 * Copyright (c) 2022 by lija 904050844@qq.com, All Rights Reserved.
 */
import Vue from 'vue'
import Router from 'vue-router'
import { constantRouterMap } from './router.config.js'
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'

// hack router push callback
const originalPush = Router.prototype.push
Router.prototype.push = function push(location, onResolve, onReject) {
  if (onResolve || onReject) return originalPush.call(this, location, onResolve, onReject)
  return originalPush.call(this, location).catch(err => err)
}

Vue.use(Router)

const createRouter = () =>
  new Router({
    // mode: 'history', // 如果你是 history模式 需要配置vue.config.js publicPath
    // base: process.env.BASE_URL,
    scrollBehavior: () => ({ y: 0 }),
    routes: constantRouterMap
  })

const router = createRouter()
router.beforeEach((to, from, next) => {
  NProgress.start()
  next()
  // if (to.query.userId) {
  //   next()
  //   return
  // }
  // if (!to.query.userId) {
  //   const toQuery = JSON.parse(JSON.stringify(to.query))
  //   toQuery.userId = '12212'
  //   toQuery.inviteCode = '333'
  //   next({
  //     path: to.path,
  //     query: toQuery
  //   })
  // }
})
router.afterEach(() => {
  NProgress.done()
})
// Detail see: https://github.com/vuejs/vue-router/issues/1234#issuecomment-357941465
export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher // reset router
}

export default router
