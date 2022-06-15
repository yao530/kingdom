<template>
  <div>
    <van-swipe ref="swipe" class="my-swipe" :show-indicators="false" lazy-render>
      <van-swipe-item v-for="item in data" :key="item.id" @click="itemClick(item.id)">
        <div class="swipe-top-view">
          <div class="title center">
            <span>{{ item.characterName }}</span>
          </div>
        </div>
        <div class="center white font-size-16">
          <span>宠物暴君</span>
        </div>
        <div class="img_box">
          <img class="img" :src="item.collectionCover" fit="cover">
          <div class="main-show center">
            <span class="title">主展示</span>
          </div>
        </div>

        <div class="share">
          <img class="img" :src="share" fit="cover">
        </div>
        <div class="background">
          <img class="img" :src="background" fit="cover">
        </div>
        <template>
          <div class="nft-view">
            <div class="left-decorate">
              <img class="img" :src="leftDecorate">
            </div>
            <div class="title">
              <span>{{ item.collectionName }} #{{ item.nftId }}</span>
            </div>
            <div class="right-decorate">
              <img class="img" :src="rightDecorate">
            </div>
          </div>
          <div class="creator">
            由
            <div class="title"># {{ item.artCreatorName }}</div>
            铸造
          </div>
        </template>

        <template>
          <div class="nft-info">
            <div class="flex-row font-size-12 font-weight-500 pad-12">
              <div class="mid-gray width25 mid-gray">故事宇宙</div>
              <div class="width75 text-right white">{{ item.metaName }}</div>
            </div>
            <div class="flex-row font-size-12 font-weight-500 pad-12">
              <div class="mid-gray width25 mid-gray">故事线</div>
              <div class="width75 text-right white">{{ item.storyTitle }}</div>
            </div>
          </div>
        </template>
      </van-swipe-item>
    </van-swipe>
  </div>
</template>
<script>
import Vue from 'vue'
import { CountDown } from 'vant'

Vue.use(CountDown)

export default {
  name: 'Avatar',
  components: {},
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
      left: require('@/assets/icon/left-arrow.png'),
      right: require('@/assets/icon/right-arrow.png'),
      share: require('@/assets/icon/share-icon.png'),
      background: require('@/assets/icon/background-img.png'),
      leftDecorate: require('@/assets/icon/left-decorate.png'),
      rightDecorate: require('@/assets/icon/right-decorate.png')
    }
  },
  computed: {},
  created() {},
  beforeDestroy: function() {},
  mounted() {
    this.$refs.swipe.swipeTo()
  },
  methods: {
    itemClick(e) {
      this.$emit('click', e)
    }
  }
}
</script>

<style lang="scss" scoped>
.my-swipe {
  margin-top: 20px;
  padding: 0px;
  border-radius: 10px;
  background: #222629;
  margin-bottom: 50px;
  /deep/.van-swipe-item {
    margin-bottom: 50px;
  }
}
.swipe-top-view {
  height: 50px;
  margin-top: 20px;
  align-items: center;
  display: flex !important;
  flex-direction: row !important;
  .swip-arrow {
    width: 100px;
    display: flex;
    justify-content: center;
    align-items: center;
  }
  .title {
    padding: 12px;
    color: white;
    width: 100%;
    height: 22px;
    font-size: 20px;
    font-family: PingFangHK-Semibold, PingFangHK;
    font-weight: 600;
    color: #ffffff;
    line-height: 22px;
  }
}
.main-show {
  width: 51px;
  height: 18px;
  background: #e9e4c3;
  border-radius: 9px;
  position: absolute;
  z-index: 2;
  top: 125px;
  left: 35px;
  .title {
    font-size: 12px;
    font-weight: 500;
    color: #3f2f29;
  }
}
.img_box {
  margin-top: 20px;
  height: 300px;
  text-align: center;

  .img {
    height: 300px;
    width: 300px;
    border-radius: 10px;
    box-shadow: 2px 2px 2px #e9e4c3;
  }
}
.share {
  padding: 20px;
  display: flex;
  justify-content: center;
  align-items: center;
  .img {
    height: 32px;
    width: 32px;
  }
}
.background {
  padding: 10px;
  display: flex;
  justify-content: center;
  align-items: center;
  .img {
    height: 60px;
    width: 306px;
  }
}
.nft-view {
  height: 30px;
  display: flex !important;
  flex-direction: row !important;
  justify-content: center;
  align-items: center;
  .title {
    font-size: 18px;
    font-family: PingFangHK-Semibold, PingFangHK;
    font-weight: 600;
    color: #ffffff;
  }
  .left-decorate {
    margin-right: 5px;
    .img {
      height: 20px;
      width: 20px;
    }
  }
  .right-decorate {
    margin-left: 5px;
    .img {
      height: 20px;
      width: 20px;
    }
  }
}
.creator {
  height: 20px;
  display: flex !important;
  flex-direction: row !important;
  justify-content: center;
  align-items: center;
  font-size: 12px;
  color: white;
  .title {
    color: #3cabf5;
  }
}
.nft-label {
  display: flex !important;
  flex-direction: row !important;
  margin-top: 12px;
  height: 50px;

  display: flex !important;
  flex-direction: row !important;
  justify-content: center;
  .label {
    text-align: center;
    width: 330px;
    margin-left: 12px;
    border-radius: 2px;
    border: 1px solid #e9e4c3;
    font-size: 12px;
    font-family: SourceHanSansCN-Regular, SourceHanSansCN;
    font-weight: 400;
    color: #e9e4c3;
    line-height: 20px;
  }
}
.nft-info {
  width: 315px;
  background: #141416;
  margin-top: 20px;
  margin-left: 15px;
  border-radius: 10px;
  white-space: normal;
  word-break: break-all;
  word-wrap: break-word;
  .width25 {
    width: 25%;
  }
  .width75 {
    width: 75%;
  }
  .text-right {
    text-align: right;
  }
}
</style>
