<template>
  <div class="margin-top-10">
    <div class="tab">
      <van-tabs v-model="active" title-active-color="#6BECFA" @click="onClickTab">
        <van-tab v-for="(item, index) in list" :key="index" :title="item.title" />
        <van-list v-model="loading" :finished="finished" finished-text="没有更多了" @load="onLoad">
          <OrderList :data="Orders" />
        </van-list>
      </van-tabs>
    </div>
  </div>
</template>

<script>
import OrderList from '@/components/OrderList'
import { getOrderList } from '@/api/Order.js'

export default {
  name: 'Order',
  components: {
    OrderList
  },
  data() {
    return {
      active: 0,
      orderStatus: 0,
      list: [
        {
          status: 0,
          title: '全部'
        },
        {
          status: 1,
          title: '待付款'
        },
        {
          status: 2,
          title: '已付款'
        },
        {
          status: 3,
          title: '已取消'
        }
      ],
      loading: false,
      finished: false,
      Orders: []
    }
  },
  computed: {},
  methods: {
    onClickTab(e) {
      this.showTag = !this.showTag
      this.orderStatus = e
      this.loading = false
      this.finished = false
      this.Orders = []
    },
    clickItem(e) {
      // params里面放置的是我们要传递过去的参数
      this.$router.push({ name: 'ArtDetail', query: { id: e.id }})
    },
    onLoad() {
      const params = { status: this.orderStatus }
      getOrderList(params).then(res => {
        res?.data?.records?.forEach(item => {
          item.finshOrder = true
        })
        this.Orders = res.data.records
        // 加载状态结束
        this.loading = false
        // 数据全部加载完成
        if (this.Orders.length >= res.data.total) {
          this.finished = true
        }
      })
    }
  }
}
</script>
<style lang="scss" scoped>
.tab {
  .van-tabs {
    /deep/.van-tabs__wrap {
      .van-tabs__line {
        height: 2px;
        background: linear-gradient(180deg, #3aa9f5 0%, #6becfa 100%);
      }
      .van-tabs__nav {
        background-color: transparent;

        .van-tab {
          color: #c2c2c2;
          font-size: 14px;
          font-family: SourceHanSansCN-Bold, SourceHanSansCN;
          line-height: 24px;
        }
        .van-tab--active {
          background: linear-gradient(to bottom, #3aa9f5 0%, #6becfa 100%);
          background-clip: text;
          -webkit-background-clip: text;
          color:transparent;
          -webkit-text-fill-color: transparent;
        }
      }
    }

    /deep/.van-tabs__content {
      .van-list {
        background: #141416;
      }
    }
  }
}
</style>
