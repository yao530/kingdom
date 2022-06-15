<!--
 * @Author: lija 904050844@qq.com
 * @Date: 2022-06-08 11:29:15
 * @LastEditors: lija 904050844@qq.com
 * @LastEditTime: 2022-06-10 19:26:45
 * @FilePath: \peta_h5\src\views\home\login.vue
 * @Description:
 *
 * Copyright (c) 2022 by lija 904050844@qq.com, All Rights Reserved.
-->
<template>
  <div class="white">
    <TopBackground />
    <div class="login-view pad-20">
      <div class="font-size-18 font-weight-500">宠物王国</div>
      <div class="font-size-16 margin-top-10">宠物元宇宙藏品叙事</div>
    </div>
    <div class="margin-top-20 white pad-20">
      <van-form @submit="$l.throttle(onSubmit)">
        <van-field
          v-model="phone"
          type="tel"
          placeholder="输入手机号"
          :rules="[{ required: true, message: '请输入手机号码' }]"
          @input="onPhoneChange"
        />
        <van-field v-model="smscode" center clearable type="number" placeholder="请输入短信验证码">
          <template #button>
            <van-button size="small" type="primary" native-type="button" :disabled="isSmsSend" @click="sendSmsCode">{{
              sendBtnText
            }}</van-button>
          </template>
        </van-field>
        <div class="margin-top-100">
          <van-button
            class="submit-btn"
            round
            block
            type="info"
            native-type="submit"
          >登录</van-button>
        </div>
        <div class="margin-left-30 light-gray-99 font-size-12 margin-top-10">
          <span>未注册手机号将自动注册</span>
        </div>
        <van-checkbox v-model="isRead" icon-size="18px" checked-color="#6BECFA" class="margin-top-10">
          <span class="light-gray-99">我已阅读并同意</span>
          <span class="main-color">《用户服务协议》</span>
          <span class="main-color">《隐私协议》</span>
        </van-checkbox>
      </van-form>
    </div>
    <!-- 滑块验证码容器，默认情况下设置为隐藏状态 -->
    <button id="TencentCaptcha" data-appid="自己后台申请的" data-cbfn="callback" style="visibility: hidden" />
  </div>
</template>

<script>
import TopBackground from '@/components/TopBackground'
import { login, getSmsCode } from '@/api/user.js'

export default {
  name: 'Login',
  components: {
    TopBackground
  },
  data() {
    return {
      phone: '13060687032',
      smscode: '123456',
      inviteCode: '', // 邀请注册的唯一编码
      isSmsSend: false,
      sendBtnText: '点击发送验证码',
      timer: null,
      counter: 60,
      isRead: true,
      clickRegistInfo: false,
      clickPrivateInfo: false
    }
  },
  computed: {},
  created() {
    this.inviteCode = this.$route.query.inviteCode
  },
  methods: {
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
      if (!this.$l.test.mobile(this.phone)) {
        this.$toast('手机号错误')
        return
      }
      this.submitHandler().then(res => {
        // 调用接口，发送短信验证码
        getSmsCode({ userPhone: this.phone })
        this.isSmsSend = true
        // 开始倒计时，60s之后才能再次点击
        this.countDown()
      })
    },
    // 当手机号变化时，重置发送按钮
    onPhoneChange() {
      this.reset()
    },
    onSubmit() {
      const params = { mobilePhone: this.phone, smsCode: this.smscode, inviteCode: this.inviteCode }
      login(params)
        .then(res => {
          if (res.code == 200) {
            const user = res.data
            localStorage.setItem('token', user.token)
            this.$router.go(-1)
          }
        })
        .catch(() => {
          return this.$toast('登录异常')
        })
    },
    submitHandler() {
      return new Promise((resolve, reject) => {
        var captcha1 = new window.TencentCaptcha(
          document.getElementById('TencentCaptcha'),
          '2094508081',
          res => {
            if (res.ret == 0) {
              resolve('获取成功')
            }
          },
          { bizState: '自定义透传参数' }
        )
        captcha1.show()
      })
    }
  }
}
</script>
<style lang="scss" scoped>
.login-view {
  position: absolute;
  z-index: 2;
  top: 100px;
}
.van-cell {
  background-color: transparent;
  padding: 12px 12px 12px 0px;
  border-bottom: 1px solid #333333;
}
.van-cell::after {
  border-bottom: 0px solid transparent;
}
.van-button--primary {
  background-color: transparent;
  color: #6becfa;
  border: 1px solid transparent;
}
.submit-btn {
  width: 335px;
  height: 44px;
  background: linear-gradient(180deg, #3aa9f5 0%, #6becfa 100%);
  border-radius: 22px;
  margin-top: 10px;
}
/deep/.van-field__control {
  color: white;
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
  border: 1px solid transparent;
  text-align: left;
  padding: 0px;
  color: #6becfa;
  display: flex !important;
  align-items: center;
}
.margin-top-100 {
  margin-top: 100px;
}
.margin-left-30 {
  margin-left: 30px;
}
</style>
