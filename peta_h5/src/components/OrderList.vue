<template>
  <div>
    <van-cell v-for="item in data" :key="item.id" @click="clickItem(item)">
      <div class="order-cell flex-row">
        <div class="order-img">
          <img class="img" :src="item.collectionCover" fit="cover">
        </div>
        <div class="flex-column">
          <div class="info flex-row">
            <div class="order flex-column">
              <span class="name">{{ item.collectionName }}</span>
              <span class="title">{{ item.characterName }}</span>
            </div>
            <div v-show="item.finshOrder" class="order-status">
              <span v-show="item.status == 2" class="suscess-status">已付款</span>
              <span v-show="item.status == 1" class="wait-status">待付款</span>
              <span v-show="item.status == 4 || item.status == 3" class="cancel-status">已取消</span>
            </div>
          </div>

          <div class="flex-column info">
            <div v-show="!item.finshOrder" class="price">¥ {{ item.payableFee }}</div>
            <div v-show="!item.finshOrder" class="sale-time order-no">{{ item.orderNo }}</div>
            <div v-show="item.finshOrder && item.status == 2">
              <div class="sale-time margin-top-5">{{ item.createTime | formatDate }}</div>
              <div v-if="item.nftId > 0" class="sale-time">作品编号:#{{ item.nftId }}</div>
              <div v-else class="font-size-12 wait-status">作品铸造中</div>
            </div>
            <div v-show="item.finshOrder && item.status != 2">
              <div class="price">¥ {{ item.payableFee }}</div>
              <div class="sale-time order-no">{{ item.createTime | formatDate }}</div>
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
  name: 'OrderList',
  filters: {
    formatDate(time) {
      var date = new Date(time)

      return formatDate(date, 'yyyy-MM-dd hh:mm:ss')
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
  margin-top: 15px;
  height: 120px;
  background: #222629;
  border-radius: 10px 15px 15px 15px;
  padding: 10px;
}
.van-cell::after {
  border-bottom: 0px solid #ebedf0;
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
.info {
  margin-left: 10px;
}
.order {
  width: 180px;
  .name {
    font-size: 16px;
  }
  .title {
    font-size: 12px;
    color: #e5e5e5;
  }
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

.order-status {
  font-size: 12px;
  font-weight: 500;
  width: 40px;
}
.order-no {
  height: 30px;
}
</style>
