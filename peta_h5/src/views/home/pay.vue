<template>
  <div>
    <div class="pay">
      <van-image fit="cover" width="345" height="100" radius="5" :src="backgroupImage" />
    </div>

    <div class="center status">
      <div>
        <span class="status-title">等待付款</span>
      </div>
      <div class="count center">
        <span>付款倒计时</span>
        <van-count-down class="title" :time="countDownTime" format="mm 分 ss 秒" />
      </div>
    </div>
    <OrderList :data="List" />
    <div class="pay-info">
      <span>请选中支付方式</span>
      <div v-for="item in payConfig" :key="item.id" class="flow-row pay-cell" @click="paySelect(item.id)">
        <div class="icon">
          <van-image fit="cover" width="25" height="25" :src="item.icon" />
        </div>
        <div class="pay-name">
          <span>{{ item.payName }}</span>
        </div>
        <div class="select-icon">
          <van-image v-show="payWay === item.id" fit="cover" width="20" height="20" :src="activeIcon" />
          <van-image v-show="payWay !== item.id" fit="cover" width="20" height="20" :src="normalIcon" />
        </div>
      </div>
    </div>

    <van-button class="van-button--info" round type="info" @click="pay">确认支付</van-button>
  </div>
</template>

<script>
import OrderList from '@/components/OrderList'
import { pay } from '@/api/Order.js'

export default {
  name: 'Pay',
  components: {
    OrderList
  },
  data() {
    return {
      id: '',
      payWay: 1,
      countDownTime: 0,
      normalIcon: require('@/assets/icon/normal-select.png'),
      activeIcon: require('@/assets/icon/active-select.png'),
      backgroupImage: require('@/assets/images/pay-back-image.png'),
      payConfig: [
        {
          id: 1,
          icon: require('@/assets/icon/wechat.png'),
          normalIcon: require('@/assets/icon/normal-select.png'),
          activeIcon: require('@/assets/icon/active-select.png'),
          payName: '微信',
          active: true
        },
        {
          id: 2,
          icon: require('@/assets/icon/alpay.png'),

          payName: '支付宝',
          active: false
        }
      ],
      List: [],
      order: ''
    }
  },
  computed: {},
  created() {
    this.id = this.$route.query.id
    if (localStorage.getItem('userInfo') != null || localStorage.getItem('userInfo') != 'undifined') {
      const order = JSON.parse(localStorage.getItem('orderInfo'))
      order.finshOrder = false
      this.order = order
      this.List.push(order)
      this.countDownTime = this.order.countDownTime
    }
  },
  methods: {
    paySelect(e) {
      this.payWay = e
    },
    pay() {
      const params = {
        id: this.order.id,
        artCollectionId: this.order.collectionId,
        payWay: this.payWay
      }
      pay(params)
        .then(res => {
          if (res.code == 200) {
            // 获取支付签名成功、发起支付
            this.$router.push({ name: 'PaySusscess', query: { id: this.order.id }})
          }
        })
        .catch(() => {})
    }
  }
}
</script>
<style lang="scss" scoped>
.flow-row {
  display: flex !important;
  flex-direction: row !important;
}
.pay {
  width: 345px;
  height: 100px;
  padding: 15px;
  border-radius: 5px;
}
.center {
  display: flex !important;
  justify-content: center;
  align-items: center;
}
.status {
  color: white;

  height: 100px;
  width: 345px;
  border-radius: 5px;
  margin-left: 15px;
  position: absolute;
  top: 15px;
  z-index: 2;
  display: flex !important;
  flex-direction: column !important;
  .status-title {
    font-size: 16px;
  }
}
.count {
  width: 100%;
  display: flex !important;
  flex-direction: row !important;
  margin-top: 10px;
  font-size: 14px;
}
.title {
  color: #ef9e00;
  margin-left: 10px;
}
.pay-info {
  width: 315px;
  height: 150px;
  margin-top: 15px;
  background: #222629;
  border-radius: 15px;
  margin-left: 15px;
  padding: 15px;
  color: white;
  font-weight: 500px;
  font-size: 17px;
  .pay-cell {
    height: 50px;
    display: flex !important;
    justify-content: center;
    align-items: center;
  }
  .pay-name {
    width: 265px;
    color: white;
    font-size: 14px;
    margin-left: 10px;
  }
}
.van-button {
  margin-top: 30px;
  width: 345px;
  margin-left: 15px;
  height: 44px;
  background: linear-gradient(180deg, #3aa9f5 0%, #6becfa 100%);
  border-radius: 22px;
}
</style>
