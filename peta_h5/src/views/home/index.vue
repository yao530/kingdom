<!-- home -->
<template>
  <div class="index-container">
    <Bg />
    <div class="warpper">
      <van-swipe class="my-swipe" :autoplay="3000" lazy-render>
        <van-swipe-item v-for="item in images" :key="item.id" @click="bannerClick(item.id)">
          <van-image lazy-load fit="cover" :src="item.bannerImage" />
        </van-swipe-item>
      </van-swipe>
    </div>
    <div class="tab">
      <div class="flex-row font-size-14 font-weight-500">
        <div v-for="item in navTab" :key="item.index" class="tab-cell" @click="onClickTab(item.collectionType)">
          <span :class="item.collectionType == collectionType ? 'white' : 'cancel-status'">{{ item.title }}</span>
        </div>
      </div>
      <van-list v-model="loading" class="" :finished="finished" finished-text="没有更多了" @load="onLoad">
        <!-- <KingdomNft :data="list" @click="clickItem" v-show="!showTag"/> -->
        <CharacterAvatarList :data="list" @click="clickItem" />
      </van-list>
    </div>
  </div>
</template>

<script>
import Bg from '@/components/TopBackground'
import CharacterAvatarList from '@/components/CharacterAvatarList'
import { getArtList } from '@/api/ArtCollections.js'
import { getList } from '@/api/banner.js'
import { transformNftInfo } from '@/filters/filter'

export default {
  components: {
    Bg,
    CharacterAvatarList
  },

  data() {
    return {
      time: 30 * 60 * 60 * 1000,
      images: [],
      active: 0,
      // "限时抢购","预约抽签","限量空投"
      navTab: [
        {
          collectionType: 0,
          title: '叙事藏品'
        },
        {
          collectionType: 1,
          title: '故事脚本'
        }
      ],
      list: [],
      loading: false,
      finished: false,
      pageNumber: 1,
      pageSize: 10,
      petaverseType: 1, // 阵营类型 1王国阵营 2宠物阵营
      soldType: 1,
      collectionType:0,
    }
  },
  computed: {},

  mounted() {
    this.initData()
  },

  methods: {
    // 请求数据案例
    initData() {
      // 请求接口数据，仅作为展示，需要配置src->config下环境文件
      const params = { pageNumber: 1, pageSize: 10 }
      getList(params)
        .then(res => {
          if (res.code == 200) {
            this.images = res.data.records
          }
        })
        .catch(() => {})
    },

    bannerClick(e) {
      this.$router.push({ name: 'Login' })
    },

    onClickTab(e) {
      this.collectionType = e
      this.pageNumber = 1
      this.pageSize = 10
      this.active = e
      this.list = []
      this.loading = false
      this.finished = false
    },
    clickItem(e) {
      // params里面放置的是我们要传递过去的参数
      this.$router.push({ name: 'ArtDetail', query: { id: e.id }})
    },
    onLoad() {
      const params = {
        pageNumber: this.pageNumber,
        pageSize: this.pageSize,
        //soldType: this.soldType，
        collectionType:this.collectionType
      }
      getArtList(params)
        .then(res => {
          if (res.code == 200) {
            if (res.data.records.length > 0) {
              this.list = transformNftInfo(res.data.records)
            }
            // 加载状态结束
            this.loading = false
            // 数据全部加载完成
            if (this.list.length >= res.data.total) {
              this.finished = true
            }
          }
        })
        .catch(() => {})
    }
  }
}
</script>

<style lang="scss" scoped>
.index-container {
  padding: 15px;
  .warpper {
    position: absolute;
    top: 15px;
    z-index: 1;
  }
}

.my-swipe {
  display: flex;
  flex-shrink: 0;
  flex-wrap: wrap;
  border-radius: 10px;
  opacity: 0.95;
  width: 345px;
  height: 180px;
}

/deep/.van-image {
  width: 345px;
  height: 180px;
  border-radius: 10px;
  background: transparent;
}

.van-image__img {
  width: 345px;
  height: 180px;
  border-radius: 10px;
}
.tab {
  width: 345px;
  position: absolute;
  top: 205px;
  z-index: 1;
  .tab-cell {
    width: 90px;
    height: 40px;
    display: flex !important;
    justify-content: left;
    align-items: center;
  }
}
</style>
