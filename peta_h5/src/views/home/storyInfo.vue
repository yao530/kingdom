<template>
  <div class="pad-15">
    <div class="backgroud flex-row">
      <van-image fit="cover" width="140" height="140" radius="10" :src="story.taleCover" />
      <div class="story flex-column">
        <span class="story-name">{{ story.taleTitle }}</span>
        <span class="story-label">故事宇宙 {{ story.metaName }}</span>

        <span class="story-status">更新至 {{ story.chapterNumber }} 话 </span>
        <span class="story-time">最新更新：{{ story.updateTime }}</span>
      </div>
    </div>

    <div class="title-view ">
      <div class="tips" />
      <div class="title font-size-14">故事简介</div>
    </div>

    <div class="story-sketch pad-15 white margin-top-10">{{ story.taleSketch }}</div>

    <div class="title-view padding-top-15">
      <div class="tips" />
      <div class="title font-size-14">全部角色</div>
    </div>

    <div class="backgroud margin-top-15" />

    <TitleDecorate :data="tipTitle" />
    <div class="backgroud margin-top-15">
      <div>
        <van-cell v-for="item in List" :key="item.id" @click="clickStoryItem(item)">
          <div class="flex-row">
            <div class="flex-row story-info">
              <span class="section">第{{ item.chapterNumber }}话</span>
              <span class="title margin-left-12">{{ item.storyTitle }}</span>
            </div>
            <div>
              <van-icon name="arrow" />
            </div>
          </div>
        </van-cell>
      </div>
    </div>
  </div>
</template>

<script>
import TitleDecorate from '@/components/TitleDecorate'
import { getStorys } from '@/api/storyApi.js'
import { getStoryTaleDetail } from '@/api/storyTale.js'

export default {
  name: 'StoryInfo',
  components: {
    TitleDecorate
  },
  data() {
    return {
      tipTitle: '故事线',
      storyTaleId: '',
      story: {
        id: 1,
        name: '迷之科学家',
        status: 1,
        title: '科学家',
        img: 'https://dieke.vshi5.com/image/2022/4/3/80e9173546d746beabf8ca402183f6d8.jpeg'
      },
      List: [
        {
          id: 1,
          title: '王国大事件',
          section: 1
        },
        {
          id: 2,
          title: '王国大事件',
          section: 1
        },
        {
          id: 3,
          title: '王国大事件',
          section: 1
        }
      ]
    }
  },
  computed: {},
  mounted() {
    this.getStoryTaleDetail()
    this.getStorysByTale()
  },
  created() {
    this.storyTaleId = this.$route.query.id
  },
  methods: {
    clickStoryItem(e) {
      this.$router.push({ name: 'StoryDetail', query: { id: e.id }})
    },
    getStoryTaleDetail() {
      const params = { id: this.storyTaleId }
      getStoryTaleDetail(params)
        .then(res => {
          if (res.code == 200 && res.data != null) {
            this.story = res.data
          }
        })
        .catch(() => {})
    },
    getStorysByTale() {
      const params = { id: this.storyTaleId }
      getStorys(params)
        .then(res => {
          if (res.code == 200 && res.data != null) {
            this.List = res.data
          }
        })
        .catch(() => {})
    }
  }
}
</script>
<style lang="scss" scoped>
.backgroud {
  width: 345px;
  background: #222629;
  border-radius: 10px;
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
.story {
  width: 205;
  color: white;
  padding: 12px;
  .story-name {
    font-size: 16px;
    font-weight: 500;
  }
  .story-label {
    font-size: 14px;
    font-weight: 500;
    color: #cccccc;
    margin-top: 5px;
  }
  .story-status {
    font-size: 12px;
    font-weight: 500;
    color: #cccccc;
    margin-top: 20px;
  }
  .story-time {
    font-size: 5px;
    font-weight: 500;
    color: #999999;
    margin-top: 5px;
  }
}
.story-sketch {
  width: 315px;
  background: #222629;
  border-radius: 10px;
}
.story-list {
  width: 325px;
}
.border-bottom-line {
  border-bottom: 1px solid #62676d;
}
.van-cell {
  background-color: transparent;
  padding: 15px;
  width: 345;
}
.van-cell__value--alone {
  color: white;
}
.story-info {
  color: white;
  width: 310px;
}
</style>
