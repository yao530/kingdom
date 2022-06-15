<template>
  <div>
    <van-cell v-for="item in data" :key="item.id" @click="clickItem(item)">
      <div class="white flex-row">
        <div class="order-img">
          <img class="img" :src="item.taleCover" fit="cover">
        </div>

        <div class="flex-column margin-left-10 info">
          <div class="">
            <span class="font-size-16">{{ item.taleTitle }}</span>
            <div class="flex-row">
              <span class="light-gray-99 font-size-14">{{ item.metaName }}</span>
              <span v-if="item.storyType == 1" class="light-gray font-size-14 margin-left-15">王国故事线</span>
              <span v-else-if="item.storyType == 2" class="light-gray-99 font-size-14 margin-left-15">宠物故事</span>
              <span v-else-if="item.storyType == 3" class="light-gray-99 font-size-14 margin-left-15">番外篇</span>
            </div>
          </div>

          <div class="flex-row info margin-top-5 story-view">
            <div class="font-size-13 width50">
              <span>作者</span>
              <span class="light-gold">#{{ item.writerName }}</span>
            </div>
            <div class="width50 right">
              <span class="font-size-12 light-gray">更新至{{ item.chapterNumber }}话</span>
            </div>
          </div>
        </div>
      </div>
    </van-cell>
  </div>
</template>
<script>
// import Vue from 'vue'
import { formatDate } from '@/filters/filter'

export default {
  name: 'StoryTaleList',
  filters: {
    formatDate(time) {
      var date = new Date(time)

      return formatDate(date, 'yyyy-MM-dd hh:mm')
    }
  },
  props: {
    data: {
      type: Array,
      default: () => {
        return []
      }
    }
  },
  data() {
    return {}
  },
  computed: {},
  created() {},
  beforeDestroy: function() {},
  methods: {
    clickItem(e) {
      this.$emit('click', e)
    }
  }
}
</script>

<style lang="scss" scoped>
.van-cell {
  width: 345px;
  margin-left: 15px;
  height: 100px;
  background: #222629;
  border-radius: 10px 15px 15px 15px;
  padding: 10px;
  margin-top: 15px;
}
/deep/.van-cell::after {
  border-bottom: 0px solid transparent;
}

.order-img {
  .img {
    width: 80px;
    height: 80px;
    border-radius: 6px;
  }
}
.story-view {
  width: 230px;
}
.width50 {
  width: 50%;
}
</style>
