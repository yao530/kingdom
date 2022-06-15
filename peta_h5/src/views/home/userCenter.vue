<template>
  <div class="pad-15">
    <div class="flex-row" >
      <div>
        <van-image fit="cover" width="50" height="50" radius="" :round="true" :src="userInfo.userAvatar" />
      </div>
      <div class="flex-column white x-center margin-left-10">
        <div class="font-size-16" width="250">{{ userInfo.userNick }}</div>
        <span v-show="userInfo.userBlockchainAddress != 'null'" width="250" class="font-size-12 margin-top-5 mid-gray ">
          {{ userInfo.userBlockchainAddress }}
        </span>
        <div v-show="userInfo.userBlockchainAddress == 'null'" class="font-size-13 mid-gray">
          编号{{ userInfo.userCode }}
        </div>
      </div>
    </div>
    <div class="margin-top-20">
      <div class="white cell flex-row center" @click="clickToAvatarList">
        <div>
          <van-icon class="iconfont icon-shouye" />
        </div>
        <div class="title font-size-14 font-weight-500">
          <span>叙事化身</span>
        </div>
        <div>
          <van-icon name="arrow" />
        </div>
      </div>
      <div class="white cell flex-row center margin-top-15" @click="clickToCollectionList">
        <div>
          <van-icon class="iconfont icon-jilu" />
        </div>
        <div class="title font-size-14 font-weight-500">
          <span>叙事藏品</span>
        </div>
        <div>
          <van-icon name="arrow" />
        </div>
      </div>
      <div class="white cell flex-row center margin-top-15" @click="clickToBox">
        <div>
          <van-icon size="25px" class="iconfont icon-baoguo_hezi" />
        </div>
        <div class="title font-size-14 font-weight-500">
          <span>我的盲盒</span>
        </div>
        <div>
          <van-icon name="arrow" />
        </div>
      </div>
      <div class="white cell flex-row center margin-top-15" @click="clickToOrder">
        <div>
          <van-icon class="iconfont icon-dingdan" />
        </div>
        <div class="title font-size-14 font-weight-500">
          <span>我的订单</span>
        </div>
        <div>
          <van-icon name="arrow" />
        </div>
      </div>

      <div v-show="!identityStatus" class="white cell flex-row center margin-top-15" @click="clickToVerifyRegist">
        <div>
          <van-icon class="iconfont icon-anquanrenzheng" />
        </div>
        <div class="title font-size-14 font-weight-500">
          <span>安全认证</span>
        </div>
        <div>
          <van-icon name="arrow" />
        </div>
      </div>
      <div class="white cell flex-row center margin-top-15" @click="clickCreatorApply">
        <div>
          <van-icon class="iconfont icon-shenqingquanxian" />
        </div>
        <div class="title font-size-14 font-weight-500">
          <span>加入我们</span>
        </div>
        <div>
          <van-icon name="arrow" />
        </div>
      </div>
      <div class="white cell flex-row center margin-top-15" @click="clickToAboutUs">
        <div>
          <van-icon class="iconfont icon-aboutme" />
        </div>
        <div class="title font-size-14 font-weight-500">
          <span>关于我们</span>
        </div>
        <div>
          <van-icon name="arrow" />
        </div>
      </div>
    </div>
  </div>
</template>

<script>
// import { mapGetters } from 'vuex'
// import { getUserInfo } from '@/api/user.js'

export default {
  name: 'UserCenter',
  components: {},
  data() {
    return {
      identityStatus: false,
      userInfo: {}
    }
  },
  computed: {},
  mounted() {
    if (localStorage.getItem('userInfo') != '') {
      const userInfo = JSON.parse(localStorage.getItem('userInfo'))
      this.userInfo = userInfo
      if (userInfo.identityStatus == 1) {
        this.identityStatus = true
      }
    }
  },
  methods: {
    // getUserInfo(){
    //      getUserInfo()
    //        .then((res) => {
    //         if(res.code==200&&res.data!='null'){
    //           this.userInfo = res.data;
    //           if(res.data.identityStatus==1)
    //             this.identityStatus=true;
    //           localStorage.setItem("userInfo", JSON.stringify(userInfo));
    //         }
    //       })
    //       .catch(() => {})
    //   },
    clickToOrder() {
      this.$router.push({ name: 'Order' })
    },
    clickToAvatarList(){
      this.$router.push({ name: 'About' })
    },
    clickToCollectionList() {
      this.$router.push({ name: 'CollectionAlbum' })
    },
    clickToAboutUs() {
      this.$router.push({ name: 'StaticTextView', params: { type: 1 }})
    },
    clickToVerifyRegist() {
      this.$router.push({ name: 'VerifyRegister' })
    },
    clickCreatorApply() {
      this.$router.push({ name: 'ApplyCharacter' })
    },
    clickToBox() {
      this.$router.push({ name: 'BlindBox' })
    }
  }
}
</script>
<style lang="scss" scoped>
.icon {
  width: 20px;
  height: 20px;
}
.y-center {
  justify-content: center;
}
.cell {
  height: 50px;
  background: #23262f;
  border-radius: 10px;
}
.title {
  width: 295px;
  margin-left: 5px;
}
.textoverflow {
  width: 150px;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
}
</style>
