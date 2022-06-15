<template>
  <div class="">
    <div class="">
      <van-search
        v-model="value"
        class="van-seach"
        shape="round"
        background="#141416"
        placeholder="搜索"
      >
        <template slot="left-icon">
          <div class="white">
            <van-icon class="iconfont icon-fjdd_icon_seach" />
          </div>
        </template>
      </van-search>

      <template>
        <van-list v-model="loading" :finished="finished" finished-text="没有更多了" @load="onLoad">
          <Avatars :data="Orders" />
        </van-list>
      </template>

    </div>
  </div>
</template>

<script>

import Vue from 'vue'
// import { mapGetters } from 'vuex'
import { Search } from 'vant'
import { Icon } from 'vant'
import Avatars from '@/components/AvatarList'
Vue.use(Icon)
Vue.use(Search)

export default {
  name: 'AvatarList',
  components: {
    Avatars
  },
  data() {
    return {
      value: '',
      loading: false,
      finished: true,
      Orders: [
        {
          id: 1,
          name: '迷之科学家',
          status: 1,
          showStatus: true,
          finshOrder: true,
          title: '科学家',
          img: 'https://dieke.vshi5.com/image/2022/4/3/80e9173546d746beabf8ca402183f6d8.jpeg'
        }
      ]
    }
  },
  computed: {

  },
  methods: {
    onSeach(val) {
      console.log(val)
    },
    onLoad() {
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

}
</script>
<style lang="scss" scoped>

.van-search{
  background-color:#141416;
  padding: 15px;
}
.van-search__content{
   background-color:#141416;
   background: #29292C;
   border-radius: 5px;
   height: 40px;
    display: flex !important;
  justify-content: center;
  align-items: center;

}
.van-field__left-icon {
  color: white
}

</style>
