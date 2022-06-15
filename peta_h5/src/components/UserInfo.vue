<template>
  <div>
    <template>
      <div class="nft-view">

        <div class="title">
          <span>{{ data.characterName }}</span>
        </div>
      </div>
      <div class="creator">
        <span class="font-size-14">{{ data.characterRoleName }}</span>
      </div>
    </template>
    <div class="img_box">
      <img class="img" :src="data.characterAvatar" fit="cover">
    </div>

    <div class="info">
      <!-- <div class="padding">
          <span class="title">职位:</span>
          <span class="content">{{data.characterRoleName}}</span>
        </div> -->
      <div class="flex-row padding">
        <div>
          <span class="title">出生宇宙:</span>
          <span class="content">{{ data.metaName }}</span>
        </div>
        <div class="to-left">
          <span class="title">生日:</span>
          <span class="content">{{ data.birthDate | formatDate }}</span>
        </div>
      </div>

      <div class="padding">
        <span class="title">角色标签:</span>
        <span class="content">{{ data.characterStyles }}</span>
      </div>
      <div class="padding">
        <span class="content">{{ data.characterInformation }}</span>
      </div>
    </div>

    <template>
      <div class="flow-info flex-row ">
        <div class="width25 center  border-line">
          <div class="flex-column center">
            <span class="number">{{ data.publishAvatarNumber }}</span>
            <span class="tip">分身数量</span>
          </div>
        </div>
        <div class="width25 center  border-line">
          <div class="flex-column center">
            <span class="number">{{ data.totalFocusNumber }}</span>
            <span class="tip">关注数量</span>
          </div>
        </div>
        <div class="width25 center">
          <div class="flex-column center">
            <span class="number">{{ data.characterPower }}</span>
            <span class="tip">全网粉丝</span>
          </div>
        </div>
        <div class="width25 center">
          <div class="center">
            <van-button icon="plus" type="primary">关注</van-button>
          </div>
        </div>
      </div>
    </template>

  </div>
</template>
<script>

import Vue from 'vue'
import { CountDown } from 'vant'
import { formatDate } from '@/filters/filter'
Vue.use(CountDown)

export default {
  name: 'UserInfo',
  filters: {
    formatDate(time) {
      var date = new Date(time)

      return formatDate(date, 'yyyy-MM-dd hh:mm:ss')
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
      left: require('@/assets/icon/left-arrow.png'),
      right: require('@/assets/icon/right-arrow.png'),
      share: require('@/assets/icon/share-icon.png'),
      background: require('@/assets/icon/background-img.png'),
      leftDecorate: require('@/assets/icon/left-decorate.png'),
      rightDecorate: require('@/assets/icon/right-decorate.png')
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
    itemClick(e) {
      this.$emit('click', e)
    }
  }
}
</script>

<style lang="scss" scoped>

.img_box{
   width: 100%;
    margin-top: 20px;
    height: 325px;
    text-align: center;

    .img{
      height: 325px;
      width: 325px;
      border-radius: 10px;
    }
}
.nft-view{
  height: 30px;
  display: flex !important;
  flex-direction: row !important;
  justify-content: center;
  align-items: center;
  .title{
    font-size: 18px;
    font-family: PingFangHK-Semibold, PingFangHK;
    font-weight: 600;
    color: #FFFFFF;

  }
}
.creator{
  height: 20px;
  display: flex !important;
  flex-direction: row !important;
  justify-content: center;
  align-items: center;
  font-size: 12px;
  color: white;
  .title{
    color: #3CABF5
  }
}
.info{
  width: 335px;
  padding: 10px;
  margin-left: 10px;
  font-size: 12px;
  .title{
    color: #999999;
  }
  .content{
    color: white;
    margin-left: 10px;
  }
  .to-left{
    margin-left: 25px;
  }
  .padding{
    padding: 5px;
  }
}
.width25{
  width: 25%;
}
.flow-info{
  color: white;
  width: 365px;
  margin-top: 25px;
}
.tip{
  color: #62676D;
  margin-top: 5px
}
.number{
  font-size: 13px;
  font-weight: 600;
}
.border-line{
  border-right:1px solid#62676D ;
}
.van-button{
  width: 80px;
  height: 28px;
  font-size: 14px;
  background: #FA4059;
  border-radius: 48px;
  border: #FA4059
}
</style>
