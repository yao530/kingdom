<!--
 * @Author: lija 904050844@qq.com
 * @Date: 2022-06-08 11:29:15
 * @LastEditors: lija 904050844@qq.com
 * @LastEditTime: 2022-06-09 14:31:07
 * @FilePath: \peta_h5\src\views\home\collectionAlbum.vue
 * @Description:
 *
 * Copyright (c) 2022 by lija 904050844@qq.com, All Rights Reserved.
-->
<template>
  <div class="">
    <van-search v-model="value" class="van-seach" shape="round" background="#141416" placeholder="搜索">
      <template slot="left-icon">
        <div class="white">
          <van-icon class="iconfont icon-fjdd_icon_seach" />
        </div>
      </template>
    </van-search>

    <div>
      <van-list v-model="loading" class="margin-top-5" :finished="finished" finished-text="没有更多了" @load="onLoad">
        <StoryTaleList :data="TaleList" @click="clickItem" />
      </van-list>
    </div>
  </div>
</template>

<script>
import StoryTaleList from '@/components/StoryTaleList'
import { getStoryList } from '@/api/storyTale.js'

export default {
  name: 'CollectionAlbum',
  components: {
    StoryTaleList
  },
  data() {
    return {
      value: '',
      loading: false,
      finished: false,
      pageNumber: 1,
      pageSize: 10,
      TaleList: []
    }
  },
  computed: {},
  methods: {
    clickItem(e) {
      this.$router.push({ name: 'CollectionAlbumDetail', query: { id: e.id }})
    },
    onLoad() {
      getStoryList({
        pageNumber: this.pageNumber,
        pageSize: this.pageSize,
        taleTitle: this.value
      }).then(res => {
        if (res.data.records.length > 0) {
          this.TaleList = res.data.records
        }
        // 加载状态结束
        this.loading = false
        // 数据全部加载完成
        if (this.TaleList.length >= res.data.total) {
          this.finished = true
        }
      })
    }
  }
}
</script>
<style lang="scss" scoped>
/deep/.van-cell::after {
  border-bottom: 0px solid transparent;
}
.van-search {
  background-color: #141416;
  padding: 15px 15px 0px 15px;
}
.van-search__content {
  background-color: #141416;
  background: #29292c;
  border-radius: 5px;
  height: 40px;
  display: flex !important;
  justify-content: center;
  align-items: center;
}
.van-field__left-icon {
  color: white;
}
</style>
