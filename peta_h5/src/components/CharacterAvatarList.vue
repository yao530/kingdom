<template>
  <div>
    <van-cell v-for="item in data" :key="item.id" @click="clickItem(item)">
      <div class="list-cell">
        <div class="img_box">
          <img class="img" :src="item.collectionCover" fit="cover">
        </div>
        <template>
          <div v-if="item.soldStatus == 1 || item.soldStatus == 0" class="cell-status">
            <van-count-down class="cell-time-countdown" :time="item.distanceTime" format="DD 天 HH 时 mm 分 ss 秒" />
          </div>
          <div v-else-if="item.soldStatus == 4" class="cell-status">
            <span class="cell-status-title mid-gray">{{ item.statusTitle }}</span>
          </div>
          <div v-else class="cell-status">
            <span class="cell-status-title">{{ item.statusTitle }}</span>
          </div>
        </template>
        <div class="cell-info-row">
          <div class="cell-story center">
            <span>{{ item.storyLine }}</span>
          </div>
          <div class="cell-nft-info">
            <span class="nft-title">{{ item.collectionName }}</span>
            <div class="cell-nft-label">
              <div class="nft-label">{{ item.soldTypeName }}</div>

              <div class="nft-label margin-left-12">{{ item.collectionTypeName }}</div>
              <div class="nft-amount-label">
                <div class="nft-amount-tips">限量</div>
                <div class="nft-amount">{{ item.mintAmount }}</div>
              </div>
              <div class="nft-price margin-left-12 right">
                <span class="font-size-14 font-weight-500">¥</span>
                <span class="font-size-18 font-weight-500 margin-left-5">{{ item.price }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </van-cell>
  </div>
</template>
<script>
export default {
  name: 'CharacterAvatarList',
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
  height: 440px;
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
.img_box {
  width: 100%;
  height: 350px;
  background: #23262f;
  .img {
    height: 350px;
    width: 100%;
    border-top-left-radius: 10px;
    border-top-right-radius: 10px;
  }
}
/deep/.van-image__img {
  border-radius: 10px;
}
.van-image {
  background: transparent;
}
.cell-status {
  height: 25px;
  background: #000500;
  border-radius: 12px;
  opacity: 0.8;
  color: #e9e4c3;
  position: absolute;
  top: 12px;
  left: 12px;
  z-index: 2;
}
.cell-status-title {
  font-size: 12px;
  font-family: SourceHanSansCN-Regular, SourceHanSansCN;
  font-weight: 400;

  line-height: 25px;
  display: flex !important;
  justify-content: center;
  align-items: center;
  padding-left: 8px;
  padding-right: 8px;
}
.cell-time-countdown {
  padding-left: 5px;
  padding-right: 5px;
  font-size: 12px;
  font-family: SourceHanSansCN-Regular, SourceHanSansCN;
  font-weight: 400;
  color: #e9e4c3;
  line-height: 25px;
  display: flex !important;
  justify-content: center;
  align-items: center;
}
.van-count-down {
  color: #e9e4c3;
}
.cell-info-row {
  display: flex !important;
  flex-direction: row !important;
}
.cell-story {
  background: #141414;
  opacity: 0.85;
  color: white;
  width: 352px;
  height: 45px;
  // border-radius: 0px 0px 10px 10px;
  position: absolute;
  z-index: 3;
  bottom: 90px;
}
.cell-nft-info {
  padding: 12px;
  width: 325px;
  position: absolute;

  display: flex !important;
  flex-direction: column !important;
  .nft-title {
    font-size: 16px;
    font-family: SourceHanSansCN-Medium, SourceHanSansCN;
    font-weight: 500;
    color: #ffffff;
    line-height: 26px;
  }

  .nft-price {
    width: 100px;
    align-items: center;
    color: #acaeaf;
    line-height: 21px;
    background: linear-gradient(to bottom, #3aa9f5 0%, #6becfa 100%);
    background-clip: text;
    -webkit-background-clip: text;
    color: transparent;
    -webkit-text-fill-color: transparent;
  }
  .cell-nft-label {
    display: flex !important;
    flex-direction: row !important;
    margin-top: 12px;
    .nft-label {
      text-align: center;
      width: 70px;
      height: 20px;
      border-radius: 2px;
      border: 1px solid #e9e4c3;
      font-size: 11px;
      font-family: SourceHanSansCN-Regular, SourceHanSansCN;
      font-weight: 400;
      color: #e9e4c3;
      line-height: 20px;
    }
    .nft-amount-label {
      display: flex !important;
      flex-direction: row !important;
      width: 80px;
      height: 20px;
      margin-left: 12px;
      .nft-amount-tips {
        width: 25px;
        background: #e9e4c3;
        border-radius: 2px 0px 0px 2px;
        border: 1px solid #e9e4c3;
        text-align: center;
        font-size: 11px;
        font-family: SourceHanSansCN-Regular, SourceHanSansCN;
        font-weight: 400;
        color: #3f2f29;
        line-height: 18px;
      }
      .nft-amount {
        width: 55px;
        background: #494c4c;
        border-radius: 0 2px 2px 0;
        border: 1px solid #e9e4c3;
        text-align: center;
        font-size: 11px;
        font-family: SourceHanSansCN-Regular, SourceHanSansCN;
        font-weight: 400;
        color: #e0e0d5;
        line-height: 18px;
      }
    }
  }
}
</style>
