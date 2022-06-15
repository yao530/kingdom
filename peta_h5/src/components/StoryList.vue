<template>
  <div>
    <van-cell v-for="item in data" :key="item.id" @click="clickItem(item)">
      <div class="list-cell">
        <div class="story-title">
          <span>{{ item.storyTitle }}</span>
        </div>
        <template>
          <ImageCell :data="item.storyCover" />
        </template>

        <!-- <div class="img_box">
      <img class="img" :src="item.images[0]"   fit="cover"/>
    </div> -->
        <div class="story-detail">
          <span class="desc">{{ item.storySketch }}</span>
        </div>

        <template>
          <div class="story-publish-info flex-row center">
            <div class="info flex-row">
              <div class="avatar">
                <img class="img" :src="item.writerAvatar" fit="cover">
              </div>
              <div class="name">
                <span class="desc">{{ item.writerName }}</span>
              </div>
            </div>
            <div class="publish info right">
              {{ item.createTime | formatDate }}
            </div>

          </div>
        </template>

      </div>
    </van-cell>
  </div>
</template>
<script>

import Vue from 'vue'
import { CountDown } from 'vant'
import ImageCell from '@/components/ImageCell'
import { formatDate } from '@/filters/filter'

Vue.use(CountDown)

export default {
  name: 'StoryList',
  filters: {
    formatDate(time) {
      var date = new Date(time)

      return formatDate(date, 'yyyy-MM-dd hh:mm')
    }
  },
  components: {
    ImageCell
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
    return {
      images: [
        'https://dieke.vshi5.com/image/2022/4/3/80e9173546d746beabf8ca402183f6d8.jpeg'
      ]
    }
  },
  computed: {

  },
  created() {

  },
  beforeDestroy: function() {

  },
  methods: {
    clickItem(e) {
      this.$emit('click', e)
    }
  }
}
</script>

<style lang="scss" scoped>
.van-cell{
  padding: 0px;
  margin-top: 15px;
  border-radius: 10px;
  background: #23262F;
  .van-cell__value{
    border-radius: 10px;
  }
}
.van-cell::after{
  border-bottom: 0.02667rem solid transparent;
  margin-top: 12px
}
.story-title{
  padding: 12px;
  color: white;
  font-size: 14px;
  font-weight: 500;
}

  .story-detail{

    padding: 12px;
    font-size: 12px;
    font-family: PingFangHK-Medium, PingFangHK;
    font-weight: 500;
    color: #999999;
    line-height: 17px;
    .desc{
      display:-webkit-box;         //将对象作为弹性伸缩盒子模型显示。
      -webkit-box-orient:vertical; //从上到下垂直排列子元素（设置伸缩盒子的子元素排列方式）
      -webkit-line-clamp:2;        //限制行数
      overflow:hidden;             //超出部分隐藏
      text-overflow:ellipsis;      //用一个省略号代替超出的内容
    }

}

.story-publish-info{
  padding: 12px;
  height: 20px;
  .info{
    width: 50%;
    .avatar{
      height: 20px;
      width: 20px;
      .img{
         height: 20px;
         width: 20px;
         border-radius: 10px;
      }
    }
    .name{
      font-size: 12px;
      font-weight: 500;
      color: #ACAEAF;
      line-height: 18px;
      margin-left: 12px
    }
  }
  .publish{
    font-size: 10px;
    font-weight: 400;
    color: #999999;
  }

}

</style>
