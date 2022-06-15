<template>
  <div>
    <div class="title-view">
      <div class="tips" />
      <div class="title flex-row">
        <div class="font-size-16 font-weight-500 width50">叙事事件</div>
      </div>
    </div>
    <template>
      <div v-for="item in data" :key="item.id" class="bg" @click="clickItem(item)">
        <div class="flex-row white">
          <div class="icon" />
          <div class="color-title margin-left-10 font-size-14 font-weight-500">
            第{{ item.chapterNumber }}话 {{ item.storyTitle }}
          </div>
        </div>
        <div class="border-left line">
          <div class="bg-transparent">
            <van-cell v-for="itm in item.detailDtoList" :key="itm.id" @click="clickItem(itm)">
              <div class="white flex-row pad-10 width305">
                <div class="font-size-14 font-weight-500 width50">
                  <div class="nft-label">{{ item.collectionTypeName }}</div>
                </div>
                <div class="width50 font-size-12 right mid-gray">发行{{ itm.mintAmount }}份</div>
              </div>

              <div class="flex-row white info-view">
                <div class="margin-left-10">
                  <img class="nft-img" :src="itm.collectionCover" fit="cover">
                </div>
                <div class="margin-left-10 margin-top-5">
                  <div v-if="itm.collectionStatus == 1" class="font-size-16">
                    {{ itm.collectionName }} #{{ itm.nftId }}
                  </div>
                  <div v-else class="font-size-16">{{ itm.collectionName }}</div>
                  <div class="flex-row width250 margin-top-5">
                    <div class="width115 light-gray font-size-12">
                      <span>{{ itm.createTime | formatDate }}</span>
                    </div>
                    <div class="width115 main-color right">
                      <span class="white">x{{ itm.userMintAmount }}</span>
                      <span class="font-size-12 margin-left-10">¥</span>
                      <span class="font-sieze-14 margin-left-5">{{ itm.price }}</span>
                    </div>
                  </div>
                </div>
              </div>

              <div class="collection-status">
                <img v-if="itm.collectionStatus == 0" class="" :src="waitStatus" fit="cover">
                <img v-else-if="itm.collectionStatus == 1" class="" :src="collectedStatus" fit="cover">
                <img v-else class="" :src="uncollectedStatus" fit="cover">
              </div>
            </van-cell>
          </div>
        </div>
      </div>
    </template>
  </div>
</template>
<script>
import Vue from 'vue'
import { CountDown } from 'vant'
import { Icon } from 'vant'
import { formatDate } from '@/filters/filter'

Vue.use(Icon)
Vue.use(CountDown)

export default {
  name: 'StoryNft',
  filters: {
    formatDate(time) {
      var date = new Date(time)

      return formatDate(date, 'yyyy-MM-dd')
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
    return {
      waitStatus: require('@/assets/images/wait-publish-icon.png'),
      collectedStatus: require('@/assets/images/collected-icon.png'),
      uncollectedStatus: require('@/assets/images/uncollected-icon.png')
    }
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
.width50 {
  width: 50%;
}
.van-cell {
  height: 115px;
  width: 325px;
  padding: 0px;
  margin-top: 15px;
  border-radius: 10px;
  background: #23262f;
  .van-cell__value {
    border-radius: 10px;
  }
}
.van-cell::after {
  border-bottom: 0.02667rem solid transparent;
  margin-top: 12px;
  width: 330px;
}
.bg {
  width: 345px;
  height: auto;
  overflow: hidden;
  background: transparent;
}

.line {
  float: left;
  width: 325px;
  margin-top: -15px;
  background: transparent;
  margin-left: 4px;
  padding: 15px;
}
.border-left {
  border-left: 1px dashed #69e8f6;
}
.type-view {
  background: #e9e4c3;
}
.info-view {
  height: 60px;
}
.nft-label {
  text-align: center;
  width: 70px;
  height: 20px;
  border-radius: 4px;
  border: 1px solid #e9e4c3;
  font-size: 11px;
  font-family: SourceHanSansCN-Regular, SourceHanSansCN;
  font-weight: 400;
  color: #e9e4c3;
  line-height: 20px;
}
.icon {
  width: 8px;
  height: 8px;
  background: #69e8f6;
  border: 1px solid #69e8f6;
  border-radius: 4px;
}
.color-title {
  color: #69e7f5;
  line-height: 14px;
}
.pd-bottom-10 {
  padding-bottom: 10px;
}
.nft-img {
  height: 60px;
  width: 60px;
  border-radius: 6px;
}

.width305 {
  width: 305px;
}
.width345 {
  width: 345px;
}
.width250 {
  width: 240px;
}
.width115 {
  width: 115px;
}
.collection-status {
  height: 100px;
  width: 100px;
  position: absolute;
  top: 0px;
  right: 2px;
  z-index: 888;
  border-top-right-radius: 6px;
  // border: 2px dashed #999999;
  .icon {
    height: 88px;
    width: 88px;
  }
}
//********** 提示标题样式/
.title-view {
  height: 30px;
  width: 345px;
  padding-bottom: 10px;
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
</style>
