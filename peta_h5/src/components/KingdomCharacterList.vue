<template>
  <div>
    <van-cell v-for="item in data" :key="item.id" @click="clickItem(item)">
      <div class="list-cell">
        <div class="img_box">
          <img class="img" :src="item.collectionCover" fit="cover">
        </div>
        <template>
          <div v-show="item.soldStatus == 0" class="cell-status cell-time-countdown">
            <van-count-down class="cell-status-title" :time="item.time" format="DD 天 HH 时 mm 分 ss 秒" />
          </div>
          <div v-show="item.soldStatus == 1" class="cell-status">
            <span class="cell-status-title">{{ item.statusTitle }}</span>
          </div>
          <div v-if="item.status == 2" class="cell-status">
            <span class="cell-status-title mid-gray">{{ item.statusTitle }}</span>
          </div>
        </template>

        <div class="cell-flow-view">
          <div class="cell-info-row">
            <div class="cell-nft-name">
              <span>{{ item.collectionName }}</span>
            </div>
            <div class="cell-nft-info">
              <div class="cell-nft-label">
                <div class="nft-label">{{ item.soldTypeName }}</div>

                <div class="nft-label margin-left-12">{{ item.collectionTypeName }}</div>
                <div class="nft-amount-label">
                  <div class="nft-amount-tips">限量</div>
                  <div class="nft-amount">{{ item.mintAmount }}</div>
                </div>
              </div>
            </div>
          </div>
          <div class="white cell-story right">
            <span class="font-size-12">¥</span>
            <span class="font-size-18 margin-left-5">99</span>
          </div>
        </div>
      </div>
    </van-cell>
  </div>
</template>
<script>
import Vue from 'vue'
import { CountDown } from 'vant'
import { Icon } from 'vant'

Vue.use(Icon)
Vue.use(CountDown)

export default {
  name: 'KingdomCharacterList',
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
  height: 345px;
  width: 345px;
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
  height: 345px;
  background: #23262f;

  // opacity: 0.3;
  .img {
    height: 345px;
    width: 100%;
    border-top-left-radius: 10px;
    border-top-right-radius: 10px;
  }
}
.cell-status {
  height: 25px;
  color: #e9e4c3;
  background: #000500;
  border-radius: 12px;
  opacity: 0.8;
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
.cell-flow-view {
  width: 295px;
  height: 60px;
  background: linear-gradient(180deg, rgba(20, 20, 22, 0.32) 0%, #141416 100%);
  box-shadow: 0px 40px 32px -24px rgba(15, 15, 15, 0.12);
  border-radius: 12px;
  backdrop-filter: blur(8px);
  padding: 15px;
  margin-left: 10px;
  position: absolute;
  bottom: 10px;
  z-index: 2;
  display: flex !important;
  flex-direction: row !important;
  color: white;
  justify-content: center;
  align-items: center;
  .cell-info-row {
    display: flex !important;
    flex-direction: column !important;
    width: 250px;
    .cell-nft-name {
      height: 26px;
      font-size: 17px;
      font-family: SourceHanSansCN-Medium, SourceHanSansCN;
      font-weight: 500;
      color: #fcfcfd;
      line-height: 26px;
      text-shadow: 0px 40px 32px rgba(15, 15, 15, 0.12);
    }
  }
}
.cell-story {
  width: 75px;
  .price {
    display: flex !important;
    justify-content: center;
    align-items: center;
    padding-left: 32px;
  }
}

.cell-story-title {
  background: linear-gradient(180deg, #3aa9f5 0%, #6becfa 100%);
  border-radius: 14px;
  font-size: 11px;
  font-family: SourceHanSansCN-Regular, SourceHanSansCN;
  font-weight: 400;
  width: 80px;
  display: flex !important;
  justify-content: center;
  align-items: center;
}

.cell-nft-info {
  display: flex !important;
  flex-direction: column !important;
  .nft-title {
    font-size: 12px;
    font-family: SourceHanSansCN-Medium, SourceHanSansCN;
    font-weight: 500;
    color: #ffffff;
    line-height: 26px;
  }

  .nft-price {
    width: 80px;
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
      width: 60px;
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
      width: 70px;
      height: 20px;
      margin-left: 12px;
      .nft-amount-tips {
        width: 30px;
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
        width: 40px;
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
