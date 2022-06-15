<!-- home -->
<template>
  <div class="about-container">
    <!-- <van-nav-bar
      title="故事情报"
    /> -->
    <div class="warpper">
      <van-search v-model="value" class="van-seach" shape="round" background="#141416" placeholder="搜索">
        <template slot="left-icon">
          <div class="white">
            <van-icon class="iconfont icon-fjdd_icon_seach" />
          </div>
        </template>
      </van-search>
    </div>
    <template>
      <div class="title-view pad-12">
        <div class="tips"></div>
        <div class="title">
          主故事线</div>
      </div>
      <div class="story-content">
        <StoryNew :data="kingdomList" @click="clickItem"/>
      </div>
    </template>
    <div class="title-view pad-15">
      <div class="tips" />
      <div class="title">多元故事线</div>
    </div>
    <div class="story-content">
      <StoryNew :data="petList" @click="clickItem" />
    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import StoryNew from '@/components/StoryNew'
import { getStoryList } from '@/api/storyApi.js'
import { formatTime } from '@/utils/index'

export default {
  components: {
    StoryNew
  },
  data() {
    return {
      wechat: `${this.$cdn}/wx/640.gif`,
      value: '',
      seachicon: require('@/assets/seach.png'),
      kingdomList: [],
      petList: [],
      listNum: []
    }
  },
  computed: {
    ...mapGetters(['userName'])
  },
  mounted() {
    this.getKingdomList()
    this.getPetStoryList()
  },
  methods: {
    clickItem(e) {
      this.$router.push({ name: 'StoryDetail', query: { id: e.id }})
    },

    getKingdomList() {
      getStoryList({ pageNumber: 1, pageSize: 10, storyType: 1 }).then(res => {
        res?.data?.records?.forEach(item => {
          // 将后端每一项的字符串用空格分开变成数组
          item.storyCover = item.storyCover.length === 0 ? [] : item.storyCover.split(',')
          item.storyLine = '《' + item.storyTaleTitle + '》' + ' 第' + item.chapterNumber + '话' + ' ' + item.storyTitle
         
        })
        this.kingdomList = res.data.records
      })
    },
    getPetStoryList() {
      getStoryList({ pageNumber: 1, pageSize: 10, storyType: 2 })
        .then(res => {
          res?.data?.records?.forEach(item => {
            item.storyCover = item.storyCover.length === 0 ? [] : item.storyCover.split(',')
            item.storyLine = '《' + item.storyTaleTitle + '》 第' + item.chapterNumber + '话' + ' ' + item.storyTitle
          })
          this.petList = res.data.records
        })
    }
  }
}
</script>
<style lang="scss" scoped>
.about-container {
  /* 你的命名空间 */
  box-sizing: border-box;
}

//********** 提示标题样式/
.title-view {
  height: 30px;
  width: 345px;
  padding-bottom: 0px;
  padding-top: 10px;
  display: flex !important;
  flex-direction: row !important;
  align-items: center;
  .tips {
    width: 2px;
    height: 15px;
    background: #6becfa;
    border-radius: 1px;
  }
  .title {
    width: 333px;
    height: 14px;

    font-family: SourceHanSansCN-Medium, SourceHanSansCN;
    font-weight: 500;
    color: #ffffff;
    line-height: 14px;
    margin-left: 10px;
  }
}
.van-nav-bar {
  background: #141416;
}
.van-nav-bar__title {
  color: white;
}
.van-seach {
  padding: 15px 15px 0 15px;
  .van-search__content {
    width: 345px;
    height: 40px;
    background: #29292c;
    border-radius: 5px;
    display: flex !important;
    justify-content: center;
    align-items: center;
  }
}

.story-content {
  padding: 15px;
  padding-top: 0px;
}
/deep/.van-field__control {
  color: white;
}
</style>
