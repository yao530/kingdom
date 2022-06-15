<template>
  <div>
    <div class="title-view">
      <div class="tips" />
      <div class="title flex-row">
        <div class="font-size-16 ">{{ data.collectionTypeName }}</div>
        <div class=" mid-gray">发行{{ data.mintAmount }}份</div>
      </div>
    </div>

    <div v-for="item in data.detailDtoList" :key="item.id" class="back-groud-view bor">
      <img class="bg-img" :src="item.collectionCover" fit="cover">

      <img v-if="item.collectionStatus == 0" class="collection-status" :src="waitStatus" fit="cover">
      <img v-else-if="item.collectionStatus == 1" class="collection-status" :src="collectedStatus" fit="cover">
      <img v-else class="collection-status" :src="uncollectedStatus" fit="cover">

      <div class="bg-detail flex-row margin-left-10 center">
        <div class="bg-title white">
          <div v-if="item.collectedStatus != 1" class="font-size-16">{{ item.collectionTypeName }}</div>
          <div v-else class="font-size-16">{{ item.collectionTypeName }} #{{ item.nftId }}</div>
          <div class="font-size-12 margin-top-5">
            <span>{{ item.createTime | formatDate }}</span>
          </div>
        </div>
        <div class="price-info center">
          <div class="price white font-size-16">
            <span class="font-size-12">¥</span>
            <span class="font-sieze-14 margin-left-5">99</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import { formatDate } from '@/filters/filter'

export default {
  name: 'StoryBackGroundNft',
  filters: {
    formatDate(time) {
      var date = new Date(time)

      return formatDate(date, 'yyyy-MM-dd hh:mm')
    }
  },
  props: {
    data: {
      type: Object,
      default: () => {
        return ''
      }
    }
  },
  data() {
    return {
      waitStatus: require('@/assets/images/wait-publish-icon.png'),
      collectedStatus: require('@/assets/images/collected-icon.png'),
      uncollectedStatus: require('@/assets/images/uncollected-icon.png')
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
.width50 {
  width: 50%;
}
.bor {
  border: none;
}
.back-groud-view {
  height: 345px;
  width: 345px;
  border-radius: 15px;
  background: white;

  position: relative;
  .bg-img {
    height: 345px;
    width: 345px;
    border-radius: 15px;
  }
  .collection-status {
    height: 100px;
    width: 100px;
    position: absolute;
    position: absolute;
    z-index: 2;
    top: 0px;
    right: 0px;
    border-top-right-radius: 15px;
    // border: 2px dashed #999999;
    .icon {
      height: 88px;
      width: 88px;
    }
  }
}
.bg-detail {
  width: 295px;
  height: 50px;
  background: linear-gradient(180deg, rgba(20, 20, 22, 0.32) 0%, #141416 100%);
  box-shadow: 0px 40px 32px -24px rgba(15, 15, 15, 0.12);
  border-radius: 12px;
  backdrop-filter: blur(8px);
  position: absolute;
  z-index: 2;
  padding: 15px;
  bottom: 10px;
}
.bg-title {
  width: 250px;
}
.price-info {
  width: 80px;

  height: 24px;
  background: linear-gradient(180deg, #3aa9f5 0%, #6becfa 100%);
  border-radius: 14px;
}
//********** 提示标题样式/
.title-view {
  height: 30px;
  width: 345px;
  padding-bottom: 10px;
  padding-top: 10px;
  display: flex !important;
  flex-direction: row !important;
  align-items: center;
  .tips {
    width: 2px;
    height: 15px;
    background: #6becfa;
    border-radius: 1px;
  }
  .title {
    width: 333px;
    height: 14px;

    font-family: SourceHanSansCN-Medium, SourceHanSansCN;
    font-weight: 500;
    color: #ffffff;
    line-height: 14px;
    margin-left: 10px;
    justify-content: space-between;
  }
}
</style>
