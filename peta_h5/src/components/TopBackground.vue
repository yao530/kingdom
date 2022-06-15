<!--
 * @Author: lija 904050844@qq.com
 * @Date: 2022-06-04 20:32:54
 * @LastEditors: lija 904050844@qq.com
 * @LastEditTime: 2022-06-10 21:57:22
 * @FilePath: \peta_h5\src\components\TopBackground.vue
 * @Description:
 *
 * Copyright (c) 2022 by lija 904050844@qq.com, All Rights Reserved.
-->
<template>
  <div class="">
    <div class="bg-img-box">
      <!-- 背景图片：动态绑定宽高 -->
      <img class="bg-img" :src="backgroundImage">
      <div class="nav-title">
        <div class="text"><slot name="header" /></div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'TopBackground',
  data() {
    return {
      // 顶部导航背景图片配置
      background: {
        // 背景图片地址 $cdn+'/QmbxQPM7DAc55a51AUWAXuLh9ZR52pBZeVJoCrUXJ5njqT'
        backgroundImage: 'url(' + require('@/assets/images/top-back-image.png') + ')',
        // 背景图片是否重复
        backgroundRepeat: 'no-repeat',
        // 背景图片大小
        backgroundSize: 'cover',
        // 背景颜色
        backgroundColor: '#141416',
        // 背景图片位置
        backgroundPosition: 'center top'
      },
      title: '',
      backgroundImage: require('@/assets/images/top-back-image.png')
    }
  },
  computed: {
    // 处理设备宽高
  },
  created() {},
  beforeDestroy: function() {},
  methods: {}
}
</script>

<style scoped>
.bg-img-box {
  width: 100%;
  height: 160px;
}
.bg-img {
  width: 100%;
  height: 160px;
}
.nav-title {
  width: 100%;
  height: 66px;
  font-size: 18px;
  font-weight: 600;
  color: #ffffff;
  line-height: 25px;
  display: table;
  text-align: center; /*增加的一行代码*/
}
.text {
  display: table-cell;
  vertical-align: middle;
}
</style>
