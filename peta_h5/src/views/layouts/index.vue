<!--
 * @Author: lija 904050844@qq.com
 * @Date: 2022-06-04 20:32:54
 * @LastEditors: lija 904050844@qq.com
 * @LastEditTime: 2022-06-08 15:12:41
 * @FilePath: \peta_h5\src\views\layouts\index.vue
 * @Description:
 *
 * Copyright (c) 2022 by lija 904050844@qq.com, All Rights Reserved.
-->
<template>
  <div class="app-container">
    <div class="layout-content">
      <keep-alive>
        <router-view v-if="$route.meta.keepAlive" style="margin-bottom: 50px" />
      </keep-alive>
      <router-view v-if="!$route.meta.keepAlive" style="margin-bottom: 50px" />
    </div>

    <!--    底部导航组件   -->
    <div class="layout-footer">
      <TabBar v-if="$route.meta.tabbarShow" :data="tabbars" @change="handleChange" />
    </div>
    <van-share-sheet
      :value="showShare"
      title="立即分享给好友"
      :options="options"
      @select="selectShare"
      @cancel="$store.dispatch('setShowShare', false)"
      @click-overlay="$store.dispatch('setShowShare', false)"
    />
  </div>
</template>

<script>
import TabBar from '@/components/TabBar'
import clipboard from '@/utils/clipboard'
import Qs from 'qs'
import { mapGetters } from 'vuex'

export default {
  name: 'Home',
  components: {
    TabBar
  },

  data() {
    return {
      pic: '@/assets/tabbar/home_icon.png',
      cachedViews: 'Home',
      tabbars: [
        {
          title: '首页',
          to: {
            name: 'Home'
          },
          normal: require('@/assets/tabbar/home-icon.png'),
          active: require('@/assets/tabbar/home-icon-active.png')
        },
        {
          title: '故事线',
          to: {
            name: 'Story'
          },
          normal: require('@/assets/tabbar/story-icon.png'),
          active: require('@/assets/tabbar/story-icon-active.png')
        },
        {
          title: '我的',
          to: {
            name: 'UserCenter'
          },
          normal: require('@/assets/tabbar/about-icon.png'),
          active: require('@/assets/tabbar/about-icon-active.png')
        }
      ],
      options: [
        // { name: '微信', icon: 'wechat' },
        // { name: '微博', icon: 'weibo' },
        { name: '复制链接', icon: 'link' }
        // { name: '分享海报', icon: 'poster' },
        // { name: '二维码', icon: 'qrcode' }
      ]
    }
  },
  computed: {
    ...mapGetters(['showShare']),
    key() {
      return this.$route.path
    }
  },
  methods: {
    handleChange(v) {},
    selectShare(e) {
      const data = Qs.parse(location.hash.split('?')[1])
      data.userid = '1212'
      clipboard(`${location.href.split('?')[0]}?${Qs.stringify(data)}`)
    }
  }
}
</script>
