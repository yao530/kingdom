<!--
 * @Author: lija 904050844@qq.com
 * @Date: 2022-06-04 20:32:54
 * @LastEditors: lija 904050844@qq.com
 * @LastEditTime: 2022-06-10 21:54:24
 * @FilePath: \peta_h5\src\components\ImageCell.vue
 * @Description:
 *
 * Copyright (c) 2022 by lija 904050844@qq.com, All Rights Reserved.
-->
<template>
  <div class="img_box center">
    <div v-for="item in data" :key="item.index" class="flex-row">
      <img v-show="data.length == 1" class="img" :src="item">
      <img v-show="data.length == 2 || data.length == 4" class="img-double" :src="item">
      <img v-show="data.length != 1 && data.length != 2 && data.length != 4" class="img-three" :src="item">
    </div>
  </div>
</template>
<script>
// import Vue from 'vue'

export default {
  name: 'ImageCell',
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
      cell: {
        paddingLeft: '10px',
        paddingRight: '10px'
      },
      cell1: {
        paddingLeft: '0px',
        paddingRight: '0px'
      }
    }
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
.img_box {
  background: #23262f;

  display: flex;
  flex-wrap: wrap;
  align-items: center;
  // .box{
  //    width: 320px;

  // }
  .img {
    height: 325px;
    width: 325px;
    border-radius: 10px;
    flex-shrink: 0; /*去除flex子元素宽度均分问题*/
  }
  .img-double {
    height: 150px;
    width: 150px;
    padding: 5px;
    border-radius: 10px;
    flex-shrink: 0; /*去除flex子元素宽度均分问题*/
  }
  .img-three {
    height: 95px;
    width: 95px;
    padding: 5px;
    border-radius: 10px;
    flex-shrink: 0; /*去除flex子元素宽度均分问题*/
  }
}
</style>
