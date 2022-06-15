<!--
 * @Author: lija 904050844@qq.com
 * @Date: 2022-06-04 20:32:54
 * @LastEditors: lija 904050844@qq.com
 * @LastEditTime: 2022-06-09 14:22:11
 * @FilePath: \peta_h5\src\views\home\staticTextView.vue
 * @Description:
 *
 * Copyright (c) 2022 by lija 904050844@qq.com, All Rights Reserved.
-->
<template>
  <div class="pad-15 white">
    <p class="h5-html" v-html="content" />
  </div>
</template>

<script>
export default {
  name: 'StaticTextView',
  components: {},
  data() {
    return {
      content:
        '<p>多多岛,故事开启了！你们知道吗？好的哈哈到家回电话记得很久很久的结婚的结婚登记还记得很久很的机会记得很久回到家喝酒记得好好的回电话<img src="https://dieke.vshi5.com/image/2022/4/10/c1e8581ec6894bafb6f9472726e05f96.jpg"></p>'
    }
  },
  computed: {},
  mounted() {
    this.content = this.content.replace(
      /<img/gi,
      '<img style="max-width:100%;height:auto;display:inline-block;margin:10px auto;"'
    )
  },
  methods: {}
}
</script>
<style lang="scss" scoped></style>
