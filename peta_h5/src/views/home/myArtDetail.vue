<template>
  <div>
    <MyArtCollection :data="userCollectionDetail" @click="itemClick" />
    <TitleDecorate :data="storyTitle" />

    <div class="story-info flex-row light-gray-99 margin-left-15 margin-top-15">
      <div class="flex-row">
        <span class="section margin-left-12">#{{ userCollectionDetail.storyTitle }}</span>
        <!-- <span class="section  margin-left-15">第{{userCollectionDetail.section}}话</span>
        <span class="title margin-left-12 ">{{userCollectionDetail.title}}</span> -->
      </div>
      <div>
        <van-icon name="arrow" />
      </div>
    </div>

    <TitleDecorate :data="mate" />
    <div class="story-info flex-row light-gray-99 margin-left-15 margin-top-15">
      <div class="section margin-left-12 light-gold font-weight-500">#宠物王国</div>
      <div>
        <van-icon name="arrow" />
      </div>
    </div>
    <div class="center flex-row tabbar">
      <div class="flex-row van-cell w100 center">
        <div class="">
          <van-icon class="iconfont icon-badge" />
        </div>
        <div class="margin-left-10">
          <span>炫耀</span>
        </div>
      </div>
      <div class="flex-row van-cell w100 center">
        <div class="">
          <van-icon class="iconfont icon-liwu" />
        </div>
        <div class="margin-left-10">
          <span>转增</span>
        </div>
      </div>
      <div class="flex-row van-cell w100 center" @click="userAsAvtar">
        <div class="">
          <van-icon class="iconfont icon-yunhangshi-qiehuanbanben" />
        </div>
        <div class="margin-left-10">
          <span>使用</span>
        </div>
      </div>
    </div>

    <TitleDecorate :data="tipTitle" />
  </div>
</template>
<script>
import MyArtCollection from '@/components/MyArtCollection'
import TitleDecorate from '@/components/TitleDecorate'
import { getUserCollectionDetail,setAvatarMainDisplay } from '@/api/UserArtCollections.js'
import { Toast } from 'vant'

export default {
  name: 'MyArtDetail',
  components: {
    MyArtCollection,
    TitleDecorate
  },
  data() {
    return {
      id: '',
      storyTitle: '故事线',
      mate: '故事宇宙',
      tipTitle: '使用须知',
      userInfo: '',
      userCollectionDetail: {},
      storyInfo: {
        id: 1,
        title: '暴君事件',
        section: 12,
        story: '宠物王国'
      },
      tab: [
        {
          id: 1,
          icon: 'iconfont icon-aboutme',
          name: '炫耀'
        },
        {
          id: 2,
          icon: '',
          name: '转赠'
        },
        {
          id: 3,
          icon: '',
          name: '使用'
        }
      ]
    }
  },
  computed: {},
  created() {
    this.id = this.$route.query.id
  },
  mounted() {
    this.getCollectionDetail()
    if (localStorage.getItem('userInfo') != '') {
      const userInfo = JSON.parse(localStorage.getItem('userInfo'))
      this.userInfo = userInfo
    }
  },
  methods: {
    itemClick() {},
    getCollectionDetail() {
      const parmas = { id: this.id }
      getUserCollectionDetail(parmas)
        .then(res => {
          if (res.code == 200) {
            res.data.characterName = '【' + res.data.characterName + '】的分身'
            res.data.storyTitle =
              '《' + res.data.storyTaleTitle + '》' + ' 第' + res.data.chapterNumber + '话' + ' ' + res.data.storyTitle
            this.userCollectionDetail = res.data
          }
        })
        .catch(() => {})
    },
    userAsAvtar(){
        const parmas = { id: this.id }
      setAvatarMainDisplay(parmas) .then(res => {
          if (res.code == 200) {
            Toast("设置头像成功")
          }
        })
        .catch(() => {})
    }
  }
}
</script>
<style lang="scss" scoped>
.story-info {
  width: 345px;
  background: #222629;
  border-radius: 5px;
  height: 44px;
  display: flex !important;

  align-items: center;
  .title {
    width: 200px;
  }
}
.tabbar {
  position: fixed;
  z-index: 2;
  bottom: 0px;
  width: 100%;
}
.van-cell {
  background: #141416;
  color: white;
  padding: 15px;
  .w100 {
    width: 100px;
  }
}
.van-cell__value--alone {
  color: white;
}
.van-cell__value {
  text-align: center;
  justify-items: center;
}
.section {
  width: 310px;
}
</style>
