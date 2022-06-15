<!--
 * @Author: lija 904050844@qq.com
 * @Date: 2022-06-08 11:29:15
 * @LastEditors: lija 904050844@qq.com
 * @LastEditTime: 2022-06-08 16:07:52
 * @FilePath: \peta_h5\src\views\home\about.vue
 * @Description:
 *
 * Copyright (c) 2022 by lija 904050844@qq.com, All Rights Reserved.
-->
<!-- home -->
<template>
  <div class="about-container">
    <div class="warpper">
      <div class="personal_box" @click="toUserInfo">
        <van-image fit="cover" round width="50" height="50" :src="userInfo.userAvatar" />
        <span class="title">我的</span>
      </div>
      <AvatarMainInfo :data="avatar" @click="itemClick" />
    </div>
  </div>
</template>

<script>
// 请求接口
import { mapGetters } from 'vuex'
import AvatarMainInfo from '@/components/AvatarMainInfo'
import { getUserAvatars } from '@/api/UserArtCollections.js'
import { getUserInfo } from '@/api/user.js'

export default {
  components: {
    AvatarMainInfo
  },
  data() {
    return {
      userInfo: {},
      avatar: []
    }
  },
  computed: {
    ...mapGetters(['userName'])
  },
  mounted() {
    this.getUserInfo()
    this.getUserMainAvatars()
  },
  methods: {
    itemClick(e) {
      this.$router.push({ name: 'MyArtDetail', query: { id: e }})
    },
    toUserInfo() {
      this.$router.push({ name: 'UserCenter' })
    },
    getUserInfo() {
      getUserInfo().then(res => {
        this.userInfo = res.data
        localStorage.setItem('userInfo', JSON.stringify(this.userInfo))
      })
    },
    getUserMainAvatars() {
      getUserAvatars({ pageNumber: 1, pageSize: 50 }).then(res => {
        res?.data?.records?.forEach(item => {
          // 将后端每一项的字符串用空格分开变成数组
          item.characterName = '【' + item.characterName + ' 的化身】'
          item.storyTitle =
            '《' + item.storyTaleTitle + '》' + ' 第' + item.chapterNumber + '话' + ' ' + item.storyTitle
        })
        this.avatar = res?.data?.records
      })
    }
  }
}
</script>
<style lang="scss" scoped>
.about-container {
  box-sizing: border-box;
  .warpper {
    padding: 15px;
  }
  .personal_box {
    height: 50px;
    display: flex !important;
    flex-direction: row !important;
    align-items: center;
    .title {
      color: white;
      font-size: 16px;
      margin-left: 10px;
      font-weight: 500;
    }
  }
}
</style>
