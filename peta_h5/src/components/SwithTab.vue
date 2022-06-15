<!--
 * @Author: lija 904050844@qq.com
 * @Date: 2022-06-08 11:29:15
 * @LastEditors: lija 904050844@qq.com
 * @LastEditTime: 2022-06-10 22:54:45
 * @FilePath: \peta_h5\src\components\SwithTab.vue
 * @Description:
 *
 * Copyright (c) 2022 by lija 904050844@qq.com, All Rights Reserved.
-->
<template>
  <div class="tab">
    <van-tabs v-model="active" title-active-color="#6BECFA" @click="onClickTab">
      <van-tab v-for="(item, index) in data" :key="index" :title="item" />
    </van-tabs>
  </div>
</template>

<script>
export default {
  name: 'SwithTab',
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
      active: 0
    }
  },
  computed: {},
  methods: {
    onClickTab(e) {
      this.$emit('click', e)
    },
    clickItem(e) {
      // params里面放置的是我们要传递过去的参数
      this.$router.push({ name: 'ArtDetail', params: { id: e.id }})
    }
  },
  loadOrders() {
    // 异步更新数据
    // setTimeout 仅做示例，真实场景中一般为 ajax 请求
    setTimeout(() => {
      for (let i = 0; i < 10; i++) {
        this.Orders.push(this.Orders.length + 1)
      }
      // 加载状态结束
      this.loading = false

      // 数据全部加载完成
      if (this.Orders.length >= 40) {
        this.finished = true
      }
    }, 1000)
  }
}
</script>
<style lang="scss" scoped>
.tab {
  .van-tabs {
    /deep/.van-tabs__wrap {
      .van-tabs__line {
        background: linear-gradient(180deg, #3aa9f5 0%, #6becfa 100%);
      }
      .van-tabs__nav {
        background-color: transparent;

        .van-tab {
          color: #c2c2c2;
          font-size: 14px;
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
        padding-top: 20px;
      }
    }
  }
}
</style>
