<template>
  <div v-if="data != null">
    <div class="img_box">
      <img class="img" :src="data.collectionCover" fit="cover">
    </div>
    <!-- <template>
         <div v-show="data.soldStatus==0" class="cell-status">
             <van-count-down class="cell-status-title" :time="data.distanceTime" format="DD 天 HH 时 mm 分 ss 秒"/>
          </div>
          <div v-show="data.soldStatus==1" class="cell-status">
             <span  class="cell-time-countdown ">{{data.statusTitle}}</span>

          </div>
          <div  v-show="data.soldStatus==2" class="cell-status ">
            <span  class="cell-status-title mid-gray">已售馨</span>
         </div>
     </template> -->
    <div class="share">
      <img class="img" :src="share" fit="cover" @click="$store.dispatch('setShowShare', true)">
    </div>
    <div class="background">
      <img class="img" :src="background" fit="cover">
    </div>
    <template>
      <div class="nft-view">
        <div class="left-decorate">
          <img class="img" :src="leftDecorate">
        </div>
        <div class="title">
          <span>{{ data.collectionName }}</span>
        </div>
        <div class="right-decorate">
          <img class="img" :src="rightDecorate">
        </div>
      </div>
      <div class="creator" @click="toStoryTale">【叙事角色 糯米的趣味生活】</div>
    </template>
    <template>
      <div class="nft-label">
        <div class="label">{{ data.soldTypeName }}</div>

        <div class="label">{{ data.collectionTypeName }}</div>
        <div class="nft-amount-label">
          <div class="nft-amount-tips">限量</div>
          <div class="nft-amount amount">{{ data.mintAmount }}</div>
        </div>
      </div>
    </template>

    <template>
      <div class="nft-info bg">
        <div class="info">
          <div class="title"><span>叙事宇宙</span></div>
          <div class="title to-right white">
            <span>{{ data.metaName }}</span>
          </div>
        </div>
        <!-- <div class="info">
                <div class="title"><span>叙事角色</span></div>
                <div class="title to-right white"><span>#{{data.characterName}}</span></div>
              </div> -->
        <div class="info">
          <div class="title"><span>叙事事件</span></div>
          <div class="title to-right white">
            <span> {{ data.storyLine }}</span>
          </div>
        </div>
        <!-- <div class="info">
                <div class="title"><span>藏品类型</span></div>
                <div class="title to-right"><span>{{data.collectionTypeName}}</span></div>
              </div> -->
      </div>
    </template>

    <!-- <template>
        <div class="nft-view margin-top-12">
          <div class="left-decorate">
              <img class="img" :src="tipDecorate" />
          </div>
          <div class="title">
              <span>作品关联</span>
          </div>
          <div class="right-decorate">
              <img class="img" :src="tipDecorate" />
          </div>
        </div>
      </template>  -->

    <template />
  </div>
</template>
<script>
import Vue from 'vue'
import { CountDown } from 'vant'
import { Step, Steps } from 'vant'

Vue.use(Step)
Vue.use(Steps)
Vue.use(CountDown)

export default {
  name: 'Detail',
  components: {},
  props: {
    data: {
      type: Object,
      default: () => {
        return null
      }
    },
    list: {
      type: Array,
      default: () => {
        return []
      }
    }
  },
  data() {
    return {
      share: require('@/assets/icon/share-icon.png'),
      background: require('@/assets/icon/background-img.png'),
      tipDecorate: require('@/assets/icon/tip.png'),
      leftDecorate: require('@/assets/icon/left-decorate.png'),
      rightDecorate: require('@/assets/icon/right-decorate.png')
    }
  },
  computed: {},
  created() {},
  beforeDestroy: function() {},
  mounted() {},
  methods: {
    toStoryTale() {
      this.$emit('click')
    }
  }
}
</script>

<style lang="scss" scoped>
.margin-top-12 {
  margin-top: 12px;
}
.img_box {
  width: 345px;
  height: 345px;
  text-align: center;
  .img {
    height: 345px;
    width: 345px;
    border-radius: 10px;
  }
}
.cell-status {
  height: 25px;
  background: #000500;
  border-radius: 12px;
  opacity: 0.8;
  color: #e9e4c3;
  position: absolute;
  top: 50px;
  left: 25px;
  z-index: 2;
}
.cell-status-title {
  font-size: 12px;
  font-family: SourceHanSansCN-Regular, SourceHanSansCN;
  font-weight: 400;

  line-height: 25px;
  display: flex !important;
  justify-content: center;
  align-items: center;
  padding-left: 8px;
  padding-right: 8px;
}
.cell-time-countdown {
  padding-left: 5px;
  padding-right: 5px;
  font-size: 12px;
  font-family: SourceHanSansCN-Regular, SourceHanSansCN;
  font-weight: 400;
  color: #e9e4c3;
  line-height: 25px;
  display: flex !important;
  justify-content: center;
  align-items: center;
}
.share {
  padding: 20px;
  display: flex;
  justify-content: center;
  align-items: center;
  .img {
    height: 45px;
    width: 45px;
  }
}
.background {
  padding: 10px;
  display: flex;
  justify-content: center;
  align-items: center;
  .img {
    height: 60px;
    width: 306px;
  }
}
.nft-view {
  height: 30px;
  display: flex !important;
  flex-direction: row !important;
  justify-content: center;
  align-items: center;
  .title {
    font-size: 20px;
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
.creator {
  height: 20px;
  margin-top: 10px;
  display: flex !important;
  flex-direction: row !important;
  justify-content: center;
  align-items: center;
  font-size: 16px;
  color: white;
}
.nft-label {
  display: flex !important;
  flex-direction: row !important;
  margin-top: 15px;
  height: 25px;
  display: flex !important;
  flex-direction: row !important;
  justify-content: center;
  .label {
    text-align: center;
    width: 80px;
    height: 20px;
    margin-left: 12px;
    border-radius: 2px;
    border: 1px solid #e9e4c3;
    font-size: 12px;
    font-family: SourceHanSansCN-Regular, SourceHanSansCN;
    font-weight: 400;
    color: #e9e4c3;
    line-height: 20px;
  }
}
.nft-amount-label {
  display: flex !important;
  flex-direction: row !important;
  width: 80px;
  height: 20px;
  margin-left: 12px;

  .nft-amount-tips {
    width: 30px;
    height: 20px;
    background: #e9e4c3;
    border-radius: 2px 0px 0px 2px;
    text-align: center;
    font-size: 11px;
    font-family: SourceHanSansCN-Regular, SourceHanSansCN;
    font-weight: 400;
    color: #3f2f29;
    line-height: 20px;
    border: 1px solid #e9e4c3;
  }
  .nft-amount {
    width: 50px;
    height: 20px;
    background: #494c4c;
    border-radius: 0 2px 2px 0;
    text-align: center;
    font-size: 11px;
    font-family: SourceHanSansCN-Regular, SourceHanSansCN;
    font-weight: 400;
    color: #e0e0d5;
    line-height: 20px;
  }
  .amount {
    border: 1px solid #e9e4c3;
  }
}

.nft-info {
  width: 315px;
  border-radius: 10px;
  margin-top: 25px;
  padding: 15px 15px 0px 15px;
  font-size: 12px;
  .info {
    height: 35px;
    color: #999990;
    display: flex !important;
    flex-direction: row !important;
    .title {
      width: 30%;
    }
    .to-right {
      text-align: right;
      width: 70%;
    }
  }
}
.bg {
  background: rgba(35, 38, 47, 0.9);
}
</style>
