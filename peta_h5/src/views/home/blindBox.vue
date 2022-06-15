<template>
  <div class="">
    <div class="tab">
      <van-tabs v-model="active" title-active-color="#6BECFA" @click="onClickTab">
        <van-tab v-for="(item, index) in tbs" :key="index" :title="item.title" />
        <van-list v-model="loading" :finished="finished" finished-text="没有更多了" @load="onLoad">
          <div v-for="itm in List" :key="itm.id" class="flex-column" @click="itmClick(itm)">
            <div class="box pad-15 flex-column white">
              <div class="flex-row">
                <div class="">
                  <van-image fit="cover" width="60" height="60" radius="6" :round="false" :src="itm.collectionCover" />
                </div>
                <div class="margin-left-10 margin-top-10">
                  <div class="font-size-16 flex-row">
                    <div class="width180">{{ itm.collectionName }}</div>
                    <div class="font-size-12 width50 wait-status padding-left-12">{{ itm.statusTitle }}</div>
                  </div>
                  <div class="font-size-14 margin-top-5 mid-gray">
                    <span>{{ itm.createTime | formatDate }}</span>
                  </div>
                </div>
              </div>
              <div v-if="itm.status == 0" class="right open-view flex-row margin-top-5">
                <div class="open center font-size-14" @click="transferBox(itm)">转赠</div>
                <div class="open center margin-left-15 font-size-14" @click="openBox(itm)">拆开</div>
              </div>
            </div>
          </div>
        </van-list>
      </van-tabs>
    </div>
    <van-popup v-model="show">
      <div class="open-box">
        <div>
          <van-image fit="cover" width="300" height="300" radius="10" :round="false" :src="clickItm.collectionCover" />
        </div>
        <div v-if="transferCollection" class="share-view margin-top-20 flex-row">
          <div class="  ">
            <van-image fit="cover" width="50" height="50" radius="10" :round="false" :src="clickItm.collectionCover" />
          </div>
          <div class="white flex-column x-center margin-left-10">
            <span class="font-size-16">{{ clickItm.collectionName }}</span>
            <span class="font-size-12">2022-06-01 12:00前扫码领取有效</span>
          </div>
        </div>

        <div
          v-if="!transferCollection"
          class="open-btn white center margin-left-25 margin-top-40 font-size-16 font-weight-500"
          @click="open(clickItm)"
        >
          确认拆开盲盒
        </div>
        <div
          v-else
          class="open-btn white center margin-left-25 margin-top-40 font-size-16 font-weight-500"
          @click="open(clickItm)"
        >
          保存分享图片
        </div>
        <div class="center flex-column" @click="shutDown(clickItm)">
          <div class="center margin-top-40">
            <van-icon size="50" class="white iconfont icon-cuowuguanbiquxiao-yuankuang" />
          </div>
        </div>
      </div>
    </van-popup>
  </div>
</template>

<script>
// import { mapGetters } from 'vuex'
import { getBoxs, openBox } from '@/api/UserBlindBox.js'
import { formatDate } from '@/filters/filter'

export default {
  name: 'BlindBox',
  filters: {
    formatDate(time) {
      var date = new Date(time)

      return formatDate(date, 'yyyy-MM-dd hh:mm')
    }
  },
  components: {},
  data() {
    return {
      active: 0,
      show: false,
      openCollection: false,
      transferCollection: false,
      List: [],
      users: [
        {
          id: 1,
          avatar: 'https://dieke.vshi5.com/image/2022/5/5/07e6b97c41e64047a61df9790ea38026.jpeg',
          name: '银行用户一'
        }
      ],
      selectTag: 0,
      clickItm: '',
      loading: false,
      finished: false,
      pageSize: 10,
      paseNumber: 1,
      tbs: [
        {
          status: 0,
          title: '待开启'
        },
        {
          status: 1,
          title: '已开启'
        },
        {
          status: 2,
          title: '已转赠'
        }
      ],
      normalIcon: require('@/assets/icon/normal-select.png'),
      activeIcon: require('@/assets/icon/active-select.png')
    }
  },
  computed: {},
  methods: {
    onClickTab(e) {
      this.active = e
      this.paseNumber = 1
      this.loading = false
      this.finished = false
    },
    itmClick(e) {
      if (e.status == 1) {
        this.$router.push({ name: 'MyArtDetail', query: { id: e.collectionId }})
      }
    },
    onLoad() {
      const parmas = { pageSize: this.pageSize, paseNumber: this.paseNumber, status: this.active }
      getBoxs(parmas)
        .then(res => {
          if (res.code == 200) {
            res.data.records.forEach(item => {
              if (item.status == 0) item.statusTitle = '待开启'
              else if (item.status == 1) item.statusTitle = '已开启'
              else item.statusTitle = '已转赠'
            })
            this.List = res.data.records
            // 加载状态结束
            this.loading = false
            // 数据全部加载完成
            if (this.List.length >= res.data.total) {
              this.finished = true
            }
          }
          this.petList = res.data.records
        })
        .catch(() => {})
    },
    openBox(e) {
      console.log(e.id)
      this.show = true
      this.openCollection = true
      this.transferCollection = false
      this.clickItm = e
    },
    open(e) {
      if (e.status == 0) {
        const parmas = { id: e.id }
        openBox(parmas)
          .then(res => {
            if (res.code == 200) {
              this.$toast('开启成功')
            }
          })
          .catch(() => {})
      }
    },
    transferBox(e) {
      console.log(e.id)
      this.show = true
      this.transferCollection = true
      this.openCollection = false
      this.clickItm = e
    },
    shutDown(e) {
      this.show = false
      this.openCollection = false
      this.transferCollection = false
    }
  }
}
</script>
<style lang="scss" scoped>
.tab {
  .van-tabs {
    /deep/.van-tabs__wrap {
      .van-tabs__line {
        height: 2px;
        background: linear-gradient(180deg, #3aa9f5 0%, #6becfa 100%);
      }
      .van-tabs__nav {
        background-color: transparent;

        // .van-tab {
        //   color: #c2c2c2;
        //   font-size: 14px;
        //   font-family: SourceHanSansCN-Bold, SourceHanSansCN;

        //   line-height: 24px;
        // }
        // .van-tab--active {
        //   background: linear-gradient(to bottom, #3aa9f5 0%, #6becfa 100%);
        //   background-clip: text;
        //   -webkit-background-clip: text;
        //   color:transparent;
        //   -webkit-text-fill-color: transparent;
        // }
      }
    }

    /deep/.van-tabs__content {
      .van-list {
        background: #141416;
      }
    }
  }
}
.box {
  width: 315px;
  margin-left: 15px;
  margin-top: 15px;

  background: #222629;
  border-radius: 5px;
}
.open-view {
  .open {
    width: 80px;
    height: 25px;
    background: transparent;
    border-radius: 12.5px;
    text-align: left;
    padding: 0px;

    background: linear-gradient(180deg, #3aa9f5 0%, #6becfa 100%);
    display: flex !important;
    align-items: center;
  }
}
.width180 {
  width: 200px;
}
.width50 {
  width: 50px;
}
.open-box {
  position: relative;
}
.open-btn {
  height: 40px;
  width: 250px;
  border-radius: 20px;
  background: linear-gradient(180deg, #3aa9f5 0%, #6becfa 100%);
  z-index: 2025;
}

.van-popup--center {
  top: 45%;
  background-color: transparent;
  width: 300px;
  border-radius: 10px;
}
.van-popup {
  overflow-y: hidden;
}
.transfer-view {
  width: 300px;
  background-color: #23262f;
}
.share-view {
  position: absolute;
  z-index: 3000;
  top: 210px;
  left: 20px;
}
</style>
