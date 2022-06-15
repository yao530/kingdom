<!-- home -->
<template>
  <div class="about-container">
    <div class="warpper">
      <Detail :data="detail" @click="toStoryTale" />

      <div v-show="detail.soldType != 3">
        <TitleDecorate :data="soldTitle" />
        <CollectionSetting :data="setting" />
      </div>

      <div v-show="List.length > 0">
        <TitleDecorate :data="createTitle" />
        <Creater :data="List" @click="clickItem" />
      </div>
      <TitleDecorate :data="artContent" />

      <div v-if="detail != null" class="content">
        <p class="h5-html" v-html="detail.context" />
      </div>
      <TitleDecorate :data="tipTitle" />

      <div class="tip margin-top-15">
        <span class="">这是购买须知</span>
      </div>
    </div>
    <div v-if="!show" class="foot-nav">
      <div class="nav-info center">
        <span class="white font-size-14">¥</span>
        <span class="price margin-left-5">{{ detail.price }}</span>
      </div>

      <div class="btn-info right">
        <div v-if="detail.soldStatus == 0" class="nav-button flex-column">
          <span class="font-size-14">{{ detail.statusTitle }}</span>
          <van-count-down
            class="font-size-12"
            :time="countTime"
            format="DD 天 HH 时 mm 分 ss 秒"
            @change="countDownFinish()"
          />
        </div>
        <div v-else-if="detail.soldStatus == 3" class="nav-button flex-column" @click="buy(detail.id)">
          <span class="font-size-14">{{ detail.statusTitle }}</span>
          <span class="font-size-12">仅剩{{ detail.mintAmount - detail.soldNumber }}</span>
        </div>
        <!-- <div v-else-if="detail.soldStatus==2" class="nav-button flex-column">
            <van-count-down class="font-size-12" :time="countTime" format="DD 天 HH 时 mm 分 ss 秒" @change="countDownFinish()" />
            <span class="font-size-14">{{}}</span>
          </div> -->
        <div v-else-if="detail.soldStatus == 4" class="nav-button flex-column">
          <span class="font-size-14">已售馨</span>
        </div>

        <div v-else-if="detail.soldStatus == 1" class="nav-button flex-column" @click="joinDrop(detail.id)">
          <span class="font-size-14">{{ detail.statusTitle }}</span>
          <van-count-down
            class="font-size-12"
            :time="countTime"
            format="DD 天 HH 时 mm 分 ss 秒"
            @change="countDownFinish()"
          />
        </div>
        <div v-else class="nav-button flex-column" @click="joinDrop(detail.id)">
          <span class="font-size-14">{{ detail.statusTitle }}</span>
          <span class="font-size-12">仅剩{{ detail.mintAmount - detail.soldNumber }}</span>
        </div>
      </div>
    </div>

    <van-overlay :show="show" @click="show = false">
      <div class="wrapper" @click.stop>
        <div class="block white">
          <div class="flex-row">
            <div class="pop-info">
              <van-image fit="cover" round width="60" height="60" :src="detail.collectionCover" />
            </div>
            <div class="flex-column margin-left-10 margin-top-5">
              <div class="font-size-16">{{ detail.collectionName }}</div>
              <div class="margin-top-5">
                <span>剩余:{{ detail.mintAmount }}份</span>
                <span class="margin-left-20">每人限购</span>
                <span class="font-weight-500 main-color margin-left-5 font-size-16">{{ detail.limitSoldNumber }}</span>
                <span class="margin-left-5">份</span>
              </div>
            </div>
          </div>
          <div class="pad-15 flex-row select margin-top-20">
            <div class="select-info font-size-14">选择数量</div>
            <div class="select-info flex-row right">
              <van-icon size="20px" name="minus margin margin-right-15" @click="minus" />
              <span class="font-size-16 margin-right-15">{{ limitSoldNumber }}</span>
              <van-icon size="20px" name="plus" @click="plus" />
            </div>
          </div>
          <div class="pad-15 margin-top-40" @click="createOrder">
            <div class="btn-buy center">
              <span class="font-size-16 font-weight-500">立即抢购</span>
              <span class="font-size-14 font-weight-500">( ¥ {{ payFee }}元 )</span>
            </div>
          </div>
        </div>
      </div>
    </van-overlay>
  </div>
</template>

<script>
// 请求接口
import { mapGetters } from 'vuex'
import Detail from '@/components/Detail'
import Creater from '@/components/Creater'
import TitleDecorate from '@/components/TitleDecorate'
import { getArtDetail, getArtSoldSettig } from '@/api/ArtCollections.js'
import { getCharactersByArt } from '@/api/VirtualCharacter.js'
import CollectionSetting from '@/components/CollectionSetting'
import { buildItem, countDownItem } from '@/filters/filter'
import { userJoinDrop } from '@/api/UserJoinDrop.js'
import { createOrder } from '@/api/Order.js'

export default {
  name: 'ArtDetail',
  components: {
    Detail,
    Creater,
    TitleDecorate,
    CollectionSetting
  },
  data() {
    return {
      id: '',
      setting: {},
      joinDropRecord: {},
      show: false,
      soldTitle: '限时活动',
      createTitle: '创作者们',
      artContent: '作品详情',
      tipTitle: '购买须知',
      detail: {},
      countTime: 0,
      countter: 0,
      limitSoldNumber: 1,
      payFee: 0,
      List: [],
      inviteList: [
        {
          id: 1,
          status: 1,
          avatar: 'https://dieke.vshi5.com/image/2022/4/3/80e9173546d746beabf8ca402183f6d8.jpeg'
        },
        {
          id: 2,
          status: 1,
          avatar: 'https://dieke.vshi5.com/image/2022/4/3/80e9173546d746beabf8ca402183f6d8.jpeg'
        },
        {
          id: 3,
          status: 0,
          avatar: 'https://dieke.vshi5.com/image/2022/4/3/80e9173546d746beabf8ca402183f6d8.jpeg'
        },
        {
          id: 4,
          status: 0,
          avatar: 'https://dieke.vshi5.com/image/2022/4/3/80e9173546d746beabf8ca402183f6d8.jpeg'
        }
      ]
    }
  },
  computed: {
    ...mapGetters(['userName'])
  },
  created() {
    this.id = this.$route.query.id
  },
  mounted() {
    this.getArtDetail()
    this.getCharactersByArt()
    this.getSoldSetting()
  },
  methods: {
    minus() {
      if (this.limitSoldNumber > 1) {
        this.limitSoldNumber--
        this.payFee = this.limitSoldNumber * this.detail.price
      }
    },
    plus() {
      if (this.limitSoldNumber < this.setting.limitSoldNumber) {
        this.limitSoldNumber++
        this.payFee = this.limitSoldNumber * this.detail.price
      } else {
        this.$toast('超过购买次数')
      }
    },
    getArtDetail() {
      getArtDetail({ id: this.id }).then(res => {
        res.data.context = res?.data?.context?.replaceAll(
          '<img',
          '<img style="max-width:100%;height:auto;display:inline-block;object-fit: cover;"'
        )
        this.detail = buildItem(res.data)
        this.payFee = this.detail.price
        this.countTime = 30000
      })
    },
    getCharactersByArt() {
      getCharactersByArt({ id: this.id }).then(res => {
        this.List = res.data
      })
    },
    getSoldSetting() {
      getArtSoldSettig({ id: this.id }).then(res => {
        this.soldTitle = res.data.soldType == 1 ? '限量空投' : '预约抽签'
        this.setting = res.data
        this.joinDropRecord = res.data.joinDropEntity
      })
    },
    toStoryTale() {
      this.$router.push({ name: 'StoryDetail', query: { id: this.detail.id }})
    },
    clickItem(e) {
      this.$router.push({ name: 'CharacterDetail', query: { id: e.id }})
    },
    // 倒计时结束
    countDownFinish() {
      if (this.countter * 1000 == this.countTime) {
        this.detail = countDownItem(this.detail)
      } else {
        this.countter++
      }
    },
    joinDrop(e) {
      const params = { id: this.setting.id }
      userJoinDrop(params)
        .then(res => {
          if (res.code == 200) {
            this.joinDropRecord = res.data
            this.$toast('参加活动成功')
          } else {
            this.$toast(res.message)
          }
        })
        .catch(err => {
          this.$toast(err.message)
        })
    },
    buy(e) {
      this.show = true
      this.payFee = this.payFee * this.limitSoldNumber
    },
    // 下单购买
    createOrder() {
      const params = {
        id: this.detail.id,
        payNumber: this.limitSoldNumber
      }
      createOrder(params).then(res => {
        const order = res.data
        localStorage.setItem('orderInfo', JSON.stringify(order))
        this.$router.push({ name: 'Pay', query: { id: res.data.id }})
      })
    }
  }
}
</script>
<style lang="scss" scoped>
.about-container {
  box-sizing: border-box;
  .warpper {
    padding: 15px;
    padding-bottom: 100px;
  }
}
.cell-status {
  height: 25px;
  background: #000500;
  border-radius: 12px;
  opacity: 0.8;
  color: #e9e4c3;
  position: absolute;
  top: 12px;
  left: 12px;
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
  width: 80px;
  font-size: 12px;
  font-family: SourceHanSansCN-Regular, SourceHanSansCN;
  font-weight: 400;
  color: #e9e4c3;
  line-height: 25px;
  display: flex !important;
  justify-content: center;
  align-items: center;
}
/deep/.van-count-down {
  color: #e9e4c3;
}
.content {
  width: 345px;
  background: #23262f;
  border-radius: 10px;
  margin-top: 15px;
  .story-info {
    padding-top: 12px;
    height: 25px;
    text-align: center;
    color: white;
  }
  .story-title {
    font-size: 16px;
  }
  .story-section {
    font-size: 14px;
  }
}
.tip {
  font-size: 12px;
  font-weight: 500;
  padding: 15px;
  border-radius: 10px;
  color: #999999;
  line-height: 14px;
  background: #23262f;
}
.h5-html {
  width: 100%;
  color: white;
  font-size: 12px;
  ::v-deep {
    img {
      width: 100%;
      display: block;
    }
  }
}

.foot-nav {
  width: 100%;
  height: 65px;
  background: linear-gradient(180deg, rgba(20, 20, 22, 0.32) 0%, #141416 100%);
  backdrop-filter: blur(10px);
  position: fixed;
  bottom: 0px;
  z-index: 10;
  padding: 12px;
  display: flex !important;
  flex-direction: row !important;
  align-items: center;
  .price {
    color: white;
    font-size: 20px;
  }
  .nav-info {
    width: 120px;
  }
  .btn-info {
    width: 230px;
  }
  .nav-button {
    width: 200px;
    height: 40px;
    color: white;
    background: linear-gradient(180deg, #3aa9f5 0%, #6becfa 100%);
    border-radius: 20px;
    display: flex !important;
    justify-content: center;
    align-items: center;
  }
}

.block {
  width: 345px;
  height: 250px;
  background-color: #141416;
  position: absolute;
  bottom: 0px;
  z-index: 22;
  padding: 15px;
}
.pop-info {
  width: 60px;
  height: 60px;
}
.select {
  display: flex !important;
  align-items: center;
  .select-info {
    width: 50%;
  }
}
.btn-buy {
  border-radius: 20px;
  height: 40px;
  background: linear-gradient(180deg, #3aa9f5 0%, #6becfa 100%);
}
</style>
