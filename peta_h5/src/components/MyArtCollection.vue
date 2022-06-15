<template>
  <div>
    <div class="img_box">
      <img class="img" :src="data.collectionCover" fit="cover">
      <div class="main-show center">
        <span class="title">{{ data.characterMainDisplay == 1 ? '主展示' : '' }}</span>
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
          <span>{{ data.collectionName }} #{{ data.nftId }}</span>
        </div>
        <div class="right-decorate">
          <img class="img" :src="rightDecorate">
        </div>
      </div>
      <div class="creator">
        由
        <div class="title">
          <span> #{{ data.artCreatorName }} </span>
        </div>
        锻造
      </div>
    </template>
    <div class="nft-label">
      <div class="label">塞伯朋克</div>
      <div class="label">末日废墟</div>
      <div class="label">分身权益</div>
    </div>

    <template>
      <div class="nft-info margin-left-15">
        <div class="flex-row font-size-14 font-weight-500 pad-12">
          <div class="mid-gray width25 mid-gray">持有玩家</div>
          <div class="width75 text-right white">{{ data.userName }}</div>
        </div>
        <div class="flex-row font-size-14 font-weight-500 pad-12">
          <div class="mid-gray width25 mid-gray">持有时间</div>
          <div class="width75 text-right white">{{ data.createTime | formatDate }}</div>
        </div>
        <div class="flex-row font-size-14 font-weight-500 pad-12">
          <div class="mid-gray width25 mid-gray">藏品编号</div>
          <div class="width75 text-right white text-overflow-2">{{ data.nftId }}</div>
        </div>
        <div class="flex-row font-size-14 font-weight-500 pad-12">
          <div class="mid-gray width25 mid-gray">合约地址</div>
          <div class="width75 text-right white text-overflow-2">{{ data.blockSmartContractAddress }}</div>
        </div>
      </div>
    </template>
  </div>
</template>
<script>
// import Vue from 'vue'
import { formatDate } from '@/filters/filter'

export default {
  name: 'MyArtCollection',
  filters: {
    formatDate(time) {
      var date = new Date(time)

      return formatDate(date, 'yyyy-MM-dd hh:mm')
    }
  },
  props: {
    data: {
      type: Object,
      default: () => {
        return []
      }
    }
  },
  data() {
    return {
      share: require('@/assets/icon/share-icon.png'),
      background: require('@/assets/icon/background-img.png'),
      leftDecorate: require('@/assets/icon/left-decorate.png'),
      rightDecorate: require('@/assets/icon/right-decorate.png')
    }
  },
  computed: {},
  created() {},
  beforeDestroy: function() {},
  mounted() {},
  methods: {
    itemClick(e) {
      this.$emit('click', e)
    }
  }
}
</script>

<style lang="scss" scoped>
.main-show {
  width: 50px;
  height: 20px;
  background: #e9e4c3;
  border-radius: 6px;
  position: absolute;
  z-index: 2;
  top: 40px;
  left: 40px;
  .title {
    font-size: 12px;
    font-weight: 500;
    color: #3f2f29;
  }
}
.img_box {
  margin-top: 20px;
  height: 325px;
  text-align: center;

  .img {
    height: 325px;
    width: 325px;
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
  height: 25px;
  display: flex !important;
  flex-direction: row !important;
  justify-content: center;
  .label {
    text-align: center;
    width: 80px;
    height: 20px;
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
  width: 345px;
  background: #222629;
  margin-top: 30px;
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
