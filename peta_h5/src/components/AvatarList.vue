<template>
  <div>
    <van-cell v-for="item in data" :key="item.id" @click="clickItem(item)">
      <div class="order-cell flow-row">

        <div class="order-img">
          <img class="img" :src="item.img" fit="cover">
        </div>
        <div class="flow-column">
          <div class="info flow-row">
            <div class="order flow-column">
              <span class="name">科幻太空狗</span>
              <div>
                <span class="title light-gold">#糯米的趣味生活</span>
                <span class="title"> 的分身叙事</span>
              </div>

            </div>

          </div>

          <div class="flow-column info">
            <span class="price">¥99</span>
            <span class="sale-time ">作品编号 #9299 / 10000</span>
          </div>

        </div>

      </div>
    </van-cell>
  </div>
</template>
<script>

// import Vue from 'vue'

export default {
  name: 'AvatarList',
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
  methods: {
    clickItem(e) {
      this.$emit('click', e)
    }
  }
}
</script>

<style lang="scss" scoped>
.flow-row{
  display: flex !important;
  flex-direction: row !important;
}
.flow-column{
  display: flex !important;
  flex-direction: column !important;
}
.center{
   display: flex !important;
   justify-content: center;
   align-items: center;
}
.van-cell{
  width: 345px;
  margin-left: 15px;
  height: 120px;
  background: #222629;
  border-radius: 10px 15px 15px 15px;
  padding: 10px;

}
.order-cell{
  color: white;
  height: 120px;
}
.order-img{
  .img{
    width: 100px;
    height: 100px;
    border-radius: 6px;
  }
}
.info{
  margin-left: 10px;
}
.order{
  width: 180px;
  .name{
    font-size: 16px;
  }
  .title{
    font-size: 12px;
  }

}
 .price{
    margin-top: 8px;
    font-size: 14px;
    font-weight: 600;
    color: #6BECFA;
  }
  .sale-time{
    color: #CCCCCC;
    font-size: 12px;
  }
  .nft-number{
    margin-top: 25px;
  }
.order-status{
   font-size: 12px;
   font-weight: 500;
   width: 40px;
}
.suscess-status{
  color: #86D038;
}
.cancel-status{
  color: #999999;
}
.wait-status{
  color: #E6920F;
}

</style>
