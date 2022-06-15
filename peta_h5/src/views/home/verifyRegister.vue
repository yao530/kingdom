<template>
  <div class="pad-15 margin-top-15">
    <div class="title">
      <span>填写实名认证信息</span>
    </div>

    <div class="margin-top-20 white">
      <van-form @submit="onSubmit">
        <van-field v-model="mobilePhone" name="mobilePhone" readonly label="注册手机号" />
        <van-field v-model="smsCode" name="smsCode" center clearable type="number" placeholder="请输入短信验证码">
          <template #button>
            <van-button size="small" type="primary" native-type="button" :disabled="isSmsSend" @click="sendSmsCode">{{
              sendBtnText
            }}</van-button>
          </template>
        </van-field>
        <van-field
          v-model="realName"
          name="realName"
          placeholder="请输入您的真实姓名"
          :rules="[{ required: true, message: '请输入真实姓名' }]"
        />
        <van-field
          v-model="identityId"
          name="identityId"
          placeholder="请输入真实姓名对应的身份证号"
          :rules="[{ required: true, message: '请输入身份证' }]"
        />
        <div class="flex-column white center margin-top-20">
          <span class="font-size-16 font-weight-500">身份证正面</span>
          <span class="font-size-12 light-gray-99 margin-top-5">(身份证正面图片需清晰可见)</span>
        </div>

        <div class="center margin-top-10">
          <van-uploader
            v-model="frontFile"
            max-count="1"
            :max-size="500 * 1024"
            :upload-icon="require('@/assets/images/identity-front.png')"
            :after-read="uploadIDcardFront"
            :before-delete="deleteFrontFile"
            @oversize="onOversize"
          />
        </div>
        <div class="flex-column white center margin-top-20">
          <span class="font-size-16 font-weight-500">身份证反面</span>
          <span class="font-size-12 light-gray-99 margin-top-5">(身份证反面图片需清晰可见)</span>
        </div>

        <div class="center margin-top-10">
          <van-uploader
            v-model="backFile"
            max-count="1"
            :max-size="500 * 1024"
            :upload-icon="require('@/assets/images/identity-front.png')"
            :after-read="uploadIDcardBack"
            :before-delete="deleteBackFile"
            @oversize="onOversize"
          />
        </div>
        <div>
          <van-button class="submit-btn" round block type="info" native-type="submit">提交</van-button>
        </div>
      </van-form>
      <van-checkbox v-model="isRead" icon-size="18px" checked-color="#6BECFA" class="margin-top-10">
        <span class="light-gray-99">我已阅读并同意</span>
        <span class="main-color">《用户服务协议》</span>
        <span class="main-color">《隐私协议》</span>
      </van-checkbox>
    </div>
  </div>
</template>

<script>
// import { mapGetters } from 'vuex'
import { verifyUserInfo } from '@/api/user.js'
import { upLoadFile } from '@/api/uploadFile.js'

export default {
  name: 'VerifyRegister',
  components: {},
  data() {
    return {
      userInfo: '',
      mobilePhone: '',
      smsCode: '',
      isSmsSend: false,
      sendBtnText: '点击发送验证码',
      timer: null,
      counter: 60,
      identityId: '',
      realName: '',
      frontFile: [],
      backFile: [],
      frontUrl: '',
      backUrl: '',
      isRead: true,
      clickRegistInfo: false,
      clickPrivateInfo: false
    }
  },
  computed: {},
  created() {
    this.userInfo = JSON.parse(localStorage.getItem('userInfo'))
    this.mobilePhone = this.userInfo.mobilePhone
  },
  methods: {
    uploadIDcardFront(e) {
      const content = e.file
      const data = new FormData()
      data.append('file', content)
      upLoadFile(data)
        .then(res => {
          if (res.code == 200) {
            this.frontUrl = res.data.url
          }
        })
        .catch(() => {
          return this.$toast('上传失败')
        })
    },
    uploadIDcardBack(e) {
      const content = e.file
      const data = new FormData()
      data.append('file', content)
      upLoadFile(data)
        .then(res => {
          if (res.code == 200) {
            this.backUrl = res.data.url
          }
        })
        .catch(() => {
          return this.$toast('上传失败')
        })
    },

    /**
     * 重置倒计时
     */
    reset() {
      this.isSmsSend = false
      this.sendBtnText = '点击发送验证码'
      if (this.timer) {
        clearInterval(this.timer)
        this.counter = 60
        this.timer = null
      }
    },
    /**
     * 倒计时
     */
    countDown() {
      this.timer = setInterval(() => {
        this.sendBtnText = `(${this.counter}秒)后重新发送`
        this.counter--
        if (this.counter < 0) {
          this.reset()
        }
      }, 1000)
    },
    /**
     * 发送验证码
     */
    sendSmsCode() {
      // 判断手机号是否已经输入
      if (!this.phone) {
        this.$notify('请输入手机号')
        return false
      }
      // 判断手机号是否符合要求
      if (this.phone.length !== 11) {
        this.$notify('请输入11位手机号')
        return false
      }
      // 调用接口，发送短信验证码
      this.isSmsSend = true
      // 开始倒计时，60s之后才能再次点击
      this.countDown()
    },
    // // 当手机号变化时，重置发送按钮
    // onPhoneChange () {
    //   this.reset()
    // },
    onSubmit(val) {
      verifyUserInfo({
        smsCode: val.smsCode,
        identityFrontUrl: this.frontUrl,
        identityBackUrl: this.backUrl,
        realName: val.realName,
        identityId: val.identityId
      })
        .then(res => {
          if (res.code == 200) {
            this.$toast('提交认证中')
          }
        })
        .catch(err => {
          this.$toast(err.message)
        })
    },

    readRegistInfo() {},
    readPrivateInfo() {},
    onOversize(file) {
      this.$toast('文件大小不能超过 500kb')
    },
    deleteFrontFile(file) {
      this.frontFile = []
      this.frontUrl = ''
    },
    deleteBackFile(file) {
      this.backFile = []
      this.backUrl = ''
    }
  }
}
</script>
<style lang="scss" scoped>
.title {
  height: 26px;
  font-size: 26px;
  font-family: SourceHanSansCN-Medium, SourceHanSansCN;
  font-weight: 500;
  color: #ffffff;
  line-height: 39px;
}
.van-cell {
  background-color: transparent;
  padding: 12px 12px 12px 0px;
  border-bottom: 0.02667rem solid #333333;
}
.van-cell::after {
  border-bottom: 0px solid transparent;
}
.van-button--primary {
  background-color: transparent;
  color: #6becfa;
  border: 0.02667rem solid transparent;
}
.submit-btn {
  width: 345px;
  height: 44px;
  background: linear-gradient(180deg, #3aa9f5 0%, #6becfa 100%);
  border-radius: 22px;
  margin-top: 80px;
}
/deep/.van-field__control {
  color: white;
}
/deep/ .van-uploader__upload,
/deep/ .van-image,
/deep/ .van-uploader__preview-image {
  width: 300px;
  height: 180px;
}
/deep/ .van-uploader__upload-text {
  margin-top: 8px;
  color: #969799;
  font-size: 12px;
  position: absolute;
  bottom: 0;
  width: 100%;
  height: 30px;
  line-height: 30px;
  background: #1da4f2;
  color: #fff;
  text-align: center;
}
/deep/ .van-uploader__upload,
/deep/ .van-image {
  background: transparent;
  border-radius: 10px;
  overflow: hidden;
}
/deep/ .van-icon__image {
  width: 300px;
  height: 180px;
  -o-object-fit: contain;
  object-fit: contain;
}
.van-icon-checked {
  color: #6becfa;
}
.height-30 {
  height: 30px;
}
.btn {
  width: 100px;
  height: 30px;
  background: transparent;
  border: 0.02667rem solid transparent;
  text-align: left;
  padding: 0px;
  color: #6becfa;
  display: flex !important;
  align-items: center;
}
</style>
