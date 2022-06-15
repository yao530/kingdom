<!--
 * @Author: lija 904050844@qq.com
 * @Date: 2022-06-04 20:32:54
 * @LastEditors: lija 904050844@qq.com
 * @LastEditTime: 2022-06-10 21:53:40
 * @FilePath: \peta_h5\src\components\Creater.vue
 * @Description:
 *
 * Copyright (c) 2022 by lija 904050844@qq.com, All Rights Reserved.
-->
<template>
  <div>
    <van-cell v-for="item in data" :key="item.id" @click="clickItem(item)">
      <div class="cell">
        <div><img class="img" :src="item.characterAvatar"></div>

        <div class="creater-info">
          <div class="title">{{ item.characterRoleName }}</div>
          <div class="name">{{ item.characterName }}</div>
        </div>

        <div>
          <van-button icon="plus" type="primary" @click="focus(item)">关注</van-button>
        </div>
      </div>
    </van-cell>
  </div>
</template>
<script>
// import Vue from 'vue'

export default {
  name: 'Creater',
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
  mounted() {},
  methods: {
    clickItem(e) {
      this.$emit('click', e)
    }
  }
}
</script>

<style lang="scss" scoped>
.cell {
  display: flex !important;
  flex-direction: row !important;
  justify-content: center;
  align-items: center;
  font-family: PingFangHK-Medium, PingFangHK;
  font-weight: 500;
  color: #cccccc;
  line-height: 16px;
  .img {
    width: 44px;
    height: 44px;
    border-radius: 22px;
  }
  .creater-info {
    margin-left: 10px;
    width: 60%;
  }
  .name {
    height: 16px;
    font-size: 11px;
  }
  .title {
    font-size: 11px;
  }
}
.van-button {
  width: 88px;
  height: 28px;
  background: #fa4059;
  border-radius: 48px;
  border: #fa4059;
}
.van-cell {
  width: 345px;
  height: 70px;
  background: #23262f;
  border-radius: 10px;
  margin-top: 12px;
  display: flex !important;
  justify-content: center;
  align-items: center;
}
.van-cell::after {
  border-bottom: 0 solid transparent;
}
</style>
