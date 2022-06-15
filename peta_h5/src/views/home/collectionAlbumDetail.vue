<template>
  <div class="pad-15">
    <div class="info white flex-row" @click="clickToStoryIp">
      <div>
        <van-image fit="cover" width="100" height="100" radius="6" :round="false" :src="detail.taleCover" />
      </div>

      <div class="flex-column margin-left-10">
        <div class="">
          <span class="font-size-16">{{ detail.taleTitle }}</span>
          <div class="flex-row">
            <span class="light-gray-99 font-size-14">{{ detail.metaName }}</span>
            <span v-if="detail.storyType == 1" class="light-gray font-size-14 margin-left-15">王国故事线</span>
            <span v-else-if="detail.storyType == 2" class="light-gray-99 font-size-14 margin-left-15">宠物故事</span>
            <span v-else-if="detail.storyType == 3" class="light-gray-99 font-size-14 margin-left-15">番外篇</span>
          </div>
        </div>

        <div class="flex-column margin-top-15">
          <div class="font-size-13 flex-row">
            <span>由</span>
            <span class="light-gold">#{{ detail.writerName }}</span>
            <span>创作</span>
          </div>
        </div>
        <div class="flex-row margin-top-5">
          <div class="width80 font-size-12 light-gray">更新至{{ detail.chapterNumber }}话</div>
          <!-- <div class="right width130">
            <span class="main-color font-size-12">¥</span>
            <span class="main-color font-size-14 margin-left-5">{{ detail.publishValue }}</span>
          </div> -->
        </div>
      </div>
    </div>

    <StoryBackGroundNft v-if="mainBackList != ''" :data="mainBackList" />
    <StoryBackGroundNft v-if="mainAvatar != ''" :data="mainAvatar" />

    <StoryNft :data="List" />

    <div class="foot-nav">
      <div class="nav-info center flex-column">
        <span class="font-size-16 white">合辑铸造</span>
        <span class="">(已收藏¥ 2000)</span>
      </div>
    </div>
  </div>
</template>

<script>
import StoryBackGroundNft from '@/components/StoryBackGroundNft'
import StoryNft from '@/components/StoryNft'
import { getStoryTaleDetail } from '@/api/storyTale.js'
import { getAlumnCollection } from '@/api/UserArtCollections.js'

export default {
  name: 'CollectionAlbumDetail',
  components: {
    StoryBackGroundNft,
    StoryNft
  },
  data() {
    return {
      id: '',
      detail: {},
      mainBackList: '',
      mainAvatar: '',
      List: []
    }
  },
  computed: {},
  created() {
    this.id = this.$route.query.id
  },
  mounted() {
    this.getDetail()
    this.getStoryTaleUserAlumns()
  },
  methods: {
    getDetail() {
      const parmas = { id: this.id }
      getStoryTaleDetail(parmas)
        .then(res => {
          if (res.code == 200) {
            if (res.data.publishValue >= 10000) {
              res.data.publishValue = res.data.publishValue / 1000 + 'k'
            }
            if (res.data.publishValue > 100000) {
              res.data.publishValue = res.data.publishValue / 10000 + 'w'
            }
            this.detail = res.data
          }
        })
        .catch(err => console.log(err))
    },

    clickToStoryIp(){
        this.$router.push({ name: 'StoryInfo', query: { id: this.id }})
    },

    getStoryTaleUserAlumns() {
      const parmas = { id: this.id }
      getAlumnCollection(parmas)
        .then(res => {
          if (res.code == 200 && res.data.length > 0) {
            for (var i = 0; i < res.data.length; i++) {
              const item = res.data[i]
              var itm = item.detailDtoList[0]
              item.mintAmount = itm.mintAmount
              if (item.mainDisplay == 1 && item.collectionType == 2) {
                this.mainBackList = item
              } else if (item.mainDisplay == 1 && item.collectionType == 3) {
                this.mainAvatar = item
              } else {
                this.List.push(item)
              }
            }
          }
        })
        .catch(err => console.log(err))
    }
  }
}
</script>
<style lang="scss" scoped>
.info {
  width: 325px;
  height: 100px;
  background: #222629;
  border-radius: 10px 15px 15px 15px;
  padding: 10px;
}
.width80 {
  width: 80px;
}
.width130 {
  width: 130px;
}
.foot-nav {
  width: 100%;
  height: 65px;
  background: linear-gradient(180deg, rgba(20, 20, 22, 0.32) 0%, #141416 100%);
  backdrop-filter: blur(10px);
  position: fixed;
  bottom: 0px;
  z-index: 1000;
  padding: 12px;
  display: flex !important;
  flex-direction: row !important;
  align-items: center;
  .nav-info {
    width: 325px;
    height: 44px;
    background: linear-gradient(180deg, #3aa9f5 0%, #6becfa 100%);
    border-radius: 22px;
  }
}
</style>
