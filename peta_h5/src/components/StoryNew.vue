<template>
  <div>
    <van-cell v-for="item in data" :key="item.id" @click="clickItem(item)">
      <div class="list-cell">
        <template>
          <div class="story-publish-info">
            <div class="info">
              <div class="avatar">
                <img class="img" :src="item.writerAvatar" fit="cover">
              </div>
              <div class="character-info">
                <span class="name">{{ item.writerName }}</span>
                <span class="title">关注1000w 科学家</span>
              </div>
            </div>
          </div>
        </template>
        <div class="story-detail">
          <span class="desc">{{ item.storySketch }}</span>
        </div>

        <!-- <div class="img_box">
      <img class="img" :src="item.storyCover"   fit="cover"/>
    </div> -->
        <!-- <template>
          <ImageCell :data="item.storyCover" />
        </template> -->
        <template>
          <div class="writer-info flex-row font-size-12">
            <div class="flex-row white width70">
              <div class="  ">
                <span class="light-gold">{{ item.storyLine }}</span>
              </div>
            </div>
            <div class="right light-gray-99 width30">
              {{ item.createTime | formatDate }}
            </div>
          </div>
        </template>
      </div>
    </van-cell>
  </div>
</template>
<script>
// import Vue from 'vue'
import { formatDate } from '@/filters/filter'
import ImageCell from '@/components/ImageCell'

export default {
  name: 'StoryNew',
  filters: {
    formatDate(time) {
      var date = new Date(time)

      return formatDate(date, 'yy-MM-dd hh:mm')
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
  padding: 0px;
  margin-top: 10px;
  border-radius: 10px;
  background: #23262f;
  .van-cell__value {
    border-radius: 10px;
  }
}
.van-cell::after {
  border-bottom: 0.02667rem solid transparent;
  margin-top: 12px;
}
.story-title {
  padding: 12px;
  color: white;
  font-size: 14px;
  font-weight: 500;
}
// .img_box{
//     width: 100%;
//     height: 300px;
//     text-align: center;
//     .img{
//       height: 300px;
//       width: 300px;
//       border-radius: 10px
//     }
//   }
.story-detail {
  width: 345px;
  padding: 10px;
  font-size: 12px;
  font-weight: 500;
  color: white;
  line-height: 17px;
  // .desc{
  //   display:-webkit-box;         //将对象作为弹性伸缩盒子模型显示。
  //   -webkit-box-orient:vertical; //从上到下垂直排列子元素（设置伸缩盒子的子元素排列方式）
  //   -webkit-line-clamp:2;        //限制行数
  //   overflow:hidden;             //超出部分隐藏
  //   text-overflow:ellipsis;      //用一个省略号代替超出的内容
  // }
}

.story-publish-info {
  padding: 12px;
  height: 40px;

  display: flex !important;
  .info {
    display: flex !important;
    flex-direction: row !important;
    align-items: center;
    .avatar {
      .img {
        height: 40px;
        width: 40px;
        border-radius: 20px;
      }
    }
    .character-info {
      font-size: 12px;
      font-weight: 500;
      color: #acaeaf;
      line-height: 18px;
      margin-left: 10px;
      display: flex !important;
      flex-direction: column !important;
      .name {
        font-size: 15px;
        font-weight: bold;
        color: #ffffff;
      }
      .title {
        font-size: 10px;
        font-weight: 400;
        color: #999999;
        margin-top: 5px;
      }
    }
  }
}
.publish {
  padding: 10px;
  font-size: 10px;
  font-weight: 400;
  color: #999999;
  display: flex !important;
  flex-direction: row !important;
  .publish-time {
    margin-left: 10px;
  }
}

.writer-info {
  padding: 12px;
  height: 20px;
  display: flex !important;

  align-items: center;
  .width30 {
    width: 30%;
  }
  .width70 {
    width: 70%;
  }
}
</style>
