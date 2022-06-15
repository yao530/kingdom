<template>
  <div>
    <div class="nft-view margin-top-15">
      <div class="left-decorate">
        <img class="img" :src="tipDecorate">
      </div>
      <div class="title">
        <span>{{ data }}</span>
      </div>
      <div class="right-decorate">
        <img class="img" :src="tipDecorate">
      </div>
    </div>
  </div>
</template>
<script>
// import Vue from 'vue'

export default {
  name: 'TitlDecorate',
  components: {},
  props: {
    data: {
      type: String,
      default: () => {
        return ''
      }
    }
  },
  data() {
    return {
      tipDecorate: require('@/assets/icon/tip.png')
    }
  },
  computed: {},
  created() {},
  beforeDestroy: function() {},
  mounted() {},
  methods: {
    itemClick(e) {
      this.$emit('click', e)
    }
  }
}
</script>

<style lang="scss" scoped>
.margin-top-12 {
  margin-top: 12px;
}

.nft-view {
  height: 30px;
  display: flex !important;
  flex-direction: row !important;
  justify-content: center;
  align-items: center;
  .title {
    font-size: 18px;
    font-family: PingFangHK-Semibold, PingFangHK;
    font-weight: 600;
    color: #ffffff;
  }
  .left-decorate {
    margin-right: 5px;
    .img {
      height: 20px;
      width: 20px;
    }
  }
  .right-decorate {
    margin-left: 5px;
    .img {
      height: 20px;
      width: 20px;
    }
  }
}
</style>
