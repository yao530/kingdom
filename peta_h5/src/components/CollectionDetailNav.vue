<!--
 * @Author: lija 904050844@qq.com
 * @Date: 2022-06-04 20:32:54
 * @LastEditors: lija 904050844@qq.com
 * @LastEditTime: 2022-06-04 22:05:38
 * @FilePath: \peta_h5\src\components\CollectionDetailNav.vue
 * @Description:
 *
 * Copyright (c) 2022 by lija 904050844@qq.com, All Rights Reserved.
-->
<template>
  <div />
</template>
<script>

import Vue from 'vue'
import { Icon } from 'vant'
Vue.use(Icon)

export default {
  name: 'CollectionDetailNav',
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

    }
  },
  computed: {

  },
  created() {

  },
  beforeDestroy: function() {

  },
  mounted() {

  },
  methods: {
    clickItem(e) {
      this.$emit('click', e)
    }
  }
}
</script>

<style lang="scss" scoped>

</style>
