<template>
  <div class="">
    <van-swipe class="my-swipe" :show-indicators="false" lazy-render>
      <van-swipe-item v-for="item in storys" :key="item.id" @click="itemClick(item.id)">
        <div class="white pad-15">
          <div class="font-size-18 height50 center" @click="toStoryTale">
            <span class="light-gold">《{{ item.storyTaleTitle }}》</span>
          </div>
          <div class="font-size-18 font-weight-500">
            <span>第{{ item.chapterNumber }}话 {{ item.storyTitle }}</span>
          </div>
          <div class="flex-row margin-top-10">
            <div class="font-size-14 label center">
              <span>原创</span>
            </div>
            <div class="center">
              <span class="font-size-14 margin-left-15 light-blue">#{{ item.writerName }}</span>
            </div>
            <div class="font-size-14 light-gray center margin-left-15">
              <span>{{ item.createTime | formatDate }}</span>
            </div>
          </div>

          <template>
            <div class="content">
              <p class="h5-html" v-html="item.context" />
            </div>
          </template>

          <template>
            <KingdomCharacterList v-show="!showTag" :data="arts" @click="clickItem" />
          </template>
        </div>
      </van-swipe-item>
    </van-swipe>
  </div>
</template>

<script>
import KingdomCharacterList from '@/components/KingdomCharacterList'
import { getStoryDetail } from '@/api/storyApi.js'
import { getStoryArts } from '@/api/ArtCollections.js'
import { formatDate } from '@/filters/filter'
import { transformNftInfo } from '@/filters/filter'

export default {
  name: 'StoryDetail',
  filters: {
    formatDate(time) {
      var date = new Date(time)

      return formatDate(date, 'yyyy-MM-dd hh:mm')
    }
  },
  components: {
    KingdomCharacterList
  },
  data() {
    return {
      id: '',
      storys: [],
      showTag: false,
      arts: []
    }
  },
  computed: {},
  mounted() {
    this.getDetai()
    this.getArtsByStory()
  },
  created() {
    this.id = this.$route.query.id
  },
  methods: {
    itemClick(e) {},
    clickItem(e) {},
    toStoryTale() {
      this.$router.push({ name: 'StoryInfo', query: { id: this.storys[0].storyTaleId }})
    },
    getDetai() {
      const params = { id: this.id }
      getStoryDetail(params)
        .then(res => {
          if (res.code == 200 && res.data != null) {
            res.data.context.replace(
              /<img/gi,
              '<img style="max-width:100%;height:auto;display:inline-block;margin:10px auto;"'
            )
            this.storys.unshift(res.data)
          }
        })
        .catch(() => {})
    },
    getArtsByStory() {
      const params = { id: this.id }
      getStoryArts(params)
        .then(res => {
          if (res.code == 200 && res.data.length > 0) {
            this.arts = transformNftInfo(res.data)
          }
        })
        .catch(() => {})
    }
  }
}
</script>
<style lang="scss" scoped>
.height50 {
  height: 50px;
}
.label {
  width: 35px;
  height: 20px;
  font-size: 12px;
  color: white;
  border-radius: 5px;
  background: #333333;
}
.content {
  margin-top: 30px;
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
