<!--
 * @Author: lija 904050844@qq.com
 * @Date: 2022-06-04 20:32:54
 * @LastEditors: lija 904050844@qq.com
 * @LastEditTime: 2022-06-10 22:54:52
 * @FilePath: \peta_h5\src\components\TabBar.vue
 * @Description:
 *
 * Copyright (c) 2022 by lija 904050844@qq.com, All Rights Reserved.
-->
<template>
  <div>
    <van-tabbar v-model="active" fixed route active-color="#6BECFA" @change="handleChange">
      <van-tabbar-item v-for="(item, index) in data" :key="index" :to="item.to" :icon="item.icon">
        <span :class="defaultActive === index ? active : ''">{{ item.title }}</span>
        <template slot="icon" slot-scope="props">
          <img :src="props.active ? item.active : item.normal">
        </template>
      </van-tabbar-item>
    </van-tabbar>
  </div>
</template>
<script>
export default {
  name: 'TabBar',
  props: {
    defaultActive: {
      type: Number,
      default: 0
    },
    data: {
      type: Array,
      default: () => {
        return []
      }
    }
  },
  data() {
    return {
      active: this.defaultActive
    }
  },
  methods: {
    handleChange(value) {
      this.$emit('change', value)
    }
  }
}
</script>

<style scoped>
.active_tab img {
  width: 26px;
  height: 26px;
}

.van-tabbar {
  background-color: #141416;
}
.van-tabbar-item {
  font-size: 11px;
  font-family: PingFangHK-Medium, PingFangHK;
  font-weight: 500;
  color: #c2c2c2;
  border: #141416;
  background-color: #141416;
}
.van-hairline--top-bottom::after {
  border-width: 0 0;
}
/* .van-tabbar-item--active {
  background: linear-gradient(to bottom, #3aa9f5 0%, #6becfa 100%);
  background-clip: text;
  -webkit-background-clip: text;
  color: transparent;
  -webkit-text-fill-color: transparent;
} */
</style>
