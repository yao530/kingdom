<template>
  <div>
    <div v-if="orderDetail.status == 2" class="pay-status flow-column">
      <div class="status center"><van-icon size="40" class="suscess-status iconfont icon-icon" /></div>
      <span class="status-title center">交易成功</span>
    </div>
    <div v-else-if="orderDetail.status == 1" class="pay-status flow-column">
      <div class="status center"><van-icon size="40" class="wait-status iconfont icon-dengdai" /></div>
      <span class="status-title center">待支付</span>
    </div>
    <div v-else class="pay-status flow-column">
      <div class="status center">
        <van-icon size="40" class="warn-status iconfont icon-cuowuguanbiquxiao-yuankuang" />
      </div>
      <span class="status-title center">已取消</span>
    </div>

    <div class="orders">
      <OrderList :data="Orders" />
    </div>

    <div class="detail">
      <div class="info">
        <div class="title"><span>订单金额</span></div>
        <div class="title to-right">¥ {{ orderDetail.payableFee }}</div>
      </div>
      <div class="info">
        <div class="title"><span>交易数量</span></div>
        <div class="title to-right">{{ orderDetail.amount }}</div>
      </div>
      <div class="info">
        <div class="title"><span>创建订单</span></div>
        <div class="title to-right">{{ orderDetail.createTime | formatDate }}</div>
      </div>
      <div class="info">
        <div class="title"><span>支付时间</span></div>
        <div class="title to-right">{{ orderDetail.updateTime | formatDate }}</div>
      </div>
      <div class="info">
        <div class="title"><span>支付方式</span></div>
        <div v-if="orderDetail.payWay == 1" class="title to-right">微信支付</div>
        <div v-else-if="orderDetail.payWay == 2" class="title to-right">支付宝支付</div>
        <div v-else-if="orderDetail.payWay == 3" class="title to-right">银行卡支付</div>
        <div v-else class="title to-right">其他支付</div>
      </div>
      <div class="info">
        <div class="title"><span>订单编号</span></div>
        <div class="title to-right">{{ orderDetail.orderNo }}</div>
      </div>
      <div class="info">
        <div class="title"><span>支付流水号</span></div>
        <div class="title to-right">{{ orderDetail.payRecordNo }}</div>
      </div>
    </div>
  </div>
</template>

<script>
import OrderList from '@/components/OrderList'
import { getOrderDetail } from '@/api/Order.js'
import { formatDate } from '@/filters/filter'

export default {
  name: 'PaySusscess',
  filters: {
    formatDate(time) {
      var date = new Date(time)

      return formatDate(date, 'yyyy-MM-dd hh:mm:ss')
    }
  },
  components: {
    OrderList
  },
  data() {
    return {
      id: '',
      orderDetail: '',
      susscess: require('@/assets/icon/pay-susscess-icon.png'),
      Orders: []
    }
  },
  computed: {},
  created() {
    this.id = this.$route.query.id
  },
  mounted() {
    this.getOrderDetail()
  },
  methods: {
    getOrderDetail() {
      const parmas = { id: this.id }
      getOrderDetail(parmas)
        .then(res => {
          if (res.code == 200) {
            res.data.finshOrder = true
            this.orderDetail = res.data
            this.Orders.push(res.data)
          }
        })
        .catch(err => console.log(err))
    }
  }
}
</script>
<style lang="scss" scoped>
.pay-status {
  margin-top: 60px;

  .status {
    width: 100%;
    height: 40px;
  }
  .icon {
    width: 40px;
    height: 40px;
  }
  .status-title {
    width: 100%;
    font-size: 16px;
    color: white;
    margin-top: 20px;
  }
}
.orders {
  margin-top: 35px;
}
.detail {
  width: 345px;
  padding: 15px;
  margin-top: 30px;
}
.info {
  height: 25px;
  color: #cccccc;
  display: flex !important;
  flex-direction: row !important;
  .title {
    width: 30%;
  }
  .to-right {
    text-align: right;
    width: 70%;
  }
}
</style>
