<template>
  <div>
    <TopBackground />
    <div class="user-info-view">
      <UserInfo :data="characterInfo" />
      <div class="to-top">
        <SwithTab :data="list" @click="onClickTab" />
        <van-list v-if="navTab == 0" v-model="loading" :finished="finished" finished-text="没有更多了" @load="onLoad">
          <CharacterLive :data="Orders" />
        </van-list>
        <van-list v-if="navTab == 1" v-model="loading" :finished="finished" finished-text="没有更多了" @load="onLoad">
          <StoryTaleList :data="Orders" />
        </van-list>
        <div v-if="navTab == 2" class="content">
          <p class="h5-html" v-html="characterInfo.context" />
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import TopBackground from '@/components/TopBackground'
import UserInfo from '@/components/UserInfo'
import SwithTab from '@/components/SwithTab'
import CharacterLive from '@/components/CharacterLive'
import StoryTaleList from '@/components/StoryTaleList'
import { getCharacterDetail } from '@/api/VirtualCharacter.js'
import { getArtList } from '@/api/ArtCollections.js'
import { getStoryList } from '@/api/storyTale.js'
import { transformNftInfo } from '@/filters/filter'

export default {
  name: 'CharacterDetail',
  components: {
    TopBackground,
    UserInfo,
    SwithTab,
    CharacterLive,
    StoryTaleList
  },
  data() {
    return {
      id: '',
      //
      list: ['动态', '故事线', '人物介绍'],
      navTab: 0,
      loading: false,
      finished: false,
      pageNumber: 1,
      pageSize: 10,
      characterInfo: {
        id: 1,
        name: '迷之科学家',
        status: 1,
        title: '科学家',
        img: 'https://dieke.vshi5.com/image/2022/4/3/80e9173546d746beabf8ca402183f6d8.jpeg'
      },
      Orders: [
        {
          id: 1,
          name: '迷之科学家',
          soldStatus: 1,
          soldType: 1,
          mintAmount: 10000,
          collectionType: 1,
          title: '科学家',
          img: 'https://dieke.vshi5.com/image/2022/4/3/80e9173546d746beabf8ca402183f6d8.jpeg'
        }
      ]
    }
  },
  computed: {},
  created() {
    this.id = this.$route.query.id
  },
  mounted() {
    this.getCharacterInfo()
  },
  methods: {
    onLoad() {
      if (this.navTab == 0) {
        getArtList({ status: this.orderStatus }).then(res => {
          if (res.data.records.length > 0) {
            this.Orders = transformNftInfo(res.data.records)
          }
          // 加载状态结束
          this.loading = false
          // 数据全部加载完成
          if (this.Orders.length >= res.data.total) {
            this.finished = true
          }
        })
      } else if (this.navTab == 1) {
        getStoryList({ status: this.orderStatus }).then(res => {
          if (res.data.records.length > 0) {
            res.data.records.forEach(item => {
              item.finshOrder = true
            })
            this.Orders = res.data.records
          }
          // 加载状态结束
          this.loading = false
          // 数据全部加载完成
          if (this.Orders.length >= res.data.total) {
            this.finished = true
          }
        })
      }
    },
    onClickTab(e) {
      this.navTab = e
      this.loading = false
      this.finished = false
      this.pageNumber = 1
      this.pageSize = 10
      this.Orders = []
    },
    getCharacterInfo() {
      getCharacterDetail({ id: this.id }).then(res => {
        res.data.context.replace(
          /<img/gi,
          '<img style="max-width:100%;height:auto;display:inline-block;margin:10px auto;"'
        )
        this.characterInfo = res.data
      })
    }
  }
}
</script>
<style lang="scss" scoped>
.user-info-view {
  width: 100%;
  position: absolute;
  top: 35px;
  z-index: 2;
}
.to-top {
  margin-top: 20px;
}
.content {
  width: 345px;
  margin-left: 15px;
}
.van-cell::after {
  border-bottom: 0px solid transparent;
}
.h5-html {
  width: 100%;
  color: white;
  font-size: 12px;
  ::v-deep {
    img {
      width: 100%;
      display: block;
    }
  }
}
</style>
