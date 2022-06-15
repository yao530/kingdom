<template>
  <div>
    <van-cell v-for="item in data" :key="item.id" @click="clickItem(item)">
      <div class="order-cell flow-row">
        <div class="order-img">
          <img class="img" :src="item.collectionCover" fit="cover">
        </div>
        <div class="cell-status">
          <span class="cell-status-title light-gold">{{ item.statusTitle }}</span>
        </div>
        <div class="flow-column">
          <div class="info flow-row">
            <div class="order flow-column">
              <span class="font-size-16">{{ item.collectionName }} </span>
              <span class="light-gray-99 font-size-12">{{ item.storyLine }}</span>
            </div>
          </div>

          <div class="flow-column info">
            <span class="price">¥99</span>
            <div class="cell-nft-label">
              <div v-show="item.soldType == 1" class="nft-label">空投活动</div>
              <div v-show="item.soldType == 2" class="nft-label">限量抢购</div>
              <div v-show="item.soldType == 3" class="nft-label">预约抽签</div>

              <div class="nft-label margin-left-10">{{ item.collectionTypeName }}</div>

              <div class="nft-amount-label">
                <div class="nft-amount-tips">限量</div>
                <div class="nft-amount">{{ item.mintAmount }}</div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </van-cell>
  </div>
</template>
<script>
// import Vue from 'vue'

export default {
  name: 'CharacterLive',
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
.flow-row {
  display: flex !important;
  flex-direction: row !important;
}
.flow-column {
  display: flex !important;
  flex-direction: column !important;
}
.center {
  display: flex !important;
  justify-content: center;
  align-items: center;
}
.van-cell {
  width: 345px;
  margin-left: 15px;
  height: 120px;
  background: #222629;
  border-radius: 10px 15px 15px 15px;
  padding: 10px;
  margin-top: 15px;
}
/deep/.van-cell::after {
  border-bottom: 0px solid transparent;
}
.order-cell {
  color: white;
}
.order-img {
  .img {
    width: 100px;
    height: 100px;
    border-radius: 6px;
  }
}
.cell-status {
  height: 20px;
  background: #000500;
  border-radius: 12px;
  opacity: 0.8;
  position: absolute;
  top: 5px;
  left: 5px;
  z-index: 2;
}
.cell-status-title {
  font-size: 12px;
  font-family: SourceHanSansCN-Regular, SourceHanSansCN;
  font-weight: 400;
  line-height: 20px;
  display: flex !important;
  justify-content: center;
  align-items: center;
  padding-left: 8px;
  padding-right: 8px;
}
.info {
  margin-left: 10px;
}

.price {
  margin-top: 5px;
  font-size: 14px;
  font-weight: 600;
  color: #6becfa;
}
.sale-time {
  color: #cccccc;
  font-size: 12px;
}
.nft-number {
  margin-top: 25px;
}
.order-status {
  font-size: 12px;
  font-weight: 500;
  width: 40px;
}
.cell-nft-label {
  display: flex !important;
  flex-direction: row !important;
  .nft-label {
    text-align: center;
    width: 55px;
    height: 16px;
    border-radius: 2px;
    border: 1px solid #e9e4c3;
    font-size: 11px;
    font-family: SourceHanSansCN-Regular, SourceHanSansCN;
    font-weight: 400;
    color: #e9e4c3;
    line-height: 16px;
  }
  .nft-amount-label {
    display: flex !important;
    flex-direction: row !important;
    width: 70px;
    height: 18px;
    margin-left: 10px;
    .nft-amount-tips {
      width: 25px;
      height: 18px;
      background: #e9e4c3;
      border-radius: 2px 0px 0px 2px;

      text-align: center;
      font-size: 11px;
      font-family: SourceHanSansCN-Regular, SourceHanSansCN;
      font-weight: 400;
      color: #3f2f29;
      line-height: 18px;
    }
    .nft-amount {
      width: 45px;
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
</style>
