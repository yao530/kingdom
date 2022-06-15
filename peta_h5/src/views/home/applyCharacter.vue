<template>
  <div class="">
    <TopBackground />
    <TopBackground />
    <div class="tiltle-view pad-20 white">
      <div class="font-size-24 font-weight-500">{{ applySetting.settingName }}</div>
      <div class="font-size-16 margin-top-10">
        <span>{{ applySetting.simpleDescription }}</span>
      </div>
      <div class="font-size-12 margin-top-20 to-right">
        <span class="light-gold">宠物王国 叙事运营部</span>
      </div>
      <div class="font-size-12 margin-top-10 to-right">
        <span class="light-gold">招募时间:2022/05/10~2022/05/30</span>
      </div>
    </div>

    <div>
      <van-form @submit="onSubmit">
        <van-field
          v-model="applyCharacterName"
          name="applyCharacterName"
          label="虚拟IP昵称"
          placeholder="虚拟角色IP昵称"
          :rules="[{ required: true, message: '请填写昵称' }]"
        />
        <van-field
          v-model="socialPlatformInfo"
          name="socialPlatformInfo"
          label="社交平台IP"
          placeholder="常用社交平台IP"
        />
        <van-field v-model="personalSpecialty" name="personalSpecialty" label="个人标签" placeholder="描述个人标签" />
        <van-field
          v-model="applyRemark"
          name="applyRemark"
          rows=""
          autosize
          label="更多介绍"
          type="textarea"
          placeholder="欢迎有创造力和热爱生活的伙伴加入我们"
        />
        <div class="role-view margin-top-15 white font-size-15">
          申请角色
          <van-cell
            v-for="item in role"
            :key="item.id"
            :title="item.characterRoleName"
            clickable
            @click="select(item.id)"
          >
            <template #right-icon>
              <van-icon size="20px" :name="selectTag == item.id ? 'checked' : 'circle'" />
            </template>
          </van-cell>
        </div>
        <div class="image-view margin-top-15">
          <div class="white font-size-15">相关材料</div>
          <van-uploader
            v-model="fileList"
            class="margin-top-15"
            :after-read="afterRead"
            :before-delete="deleteFile"
            @oversize="onOversize"
          />
        </div>

        <div>
          <van-button class="submit-btn" round block type="info" native-type="submit">提交</van-button>
        </div>
      </van-form>
    </div>
  </div>
</template>

<script>
// import { mapGetters } from 'vuex'
import TopBackground from '@/components/TopBackground'
import { upLoadFile } from '@/api/uploadFile.js'
import { getRole } from '@/api/CharacterRole.js'
import { applyCreator, getApplySetting } from '@/api/ApplyCharacter.js'

export default {
  name: 'ApplyCharacter',
  components: {
    TopBackground
  },
  data() {
    return {
      applyCharacterName: '',
      personalSpecialty: '',
      socialPlatformInfo: '',
      selectTag: 1,
      applyRemark: '',
      characterRoleId: '',
      role: [],
      urls: [],
      fileList: [],
      applySetting: ''
    }
  },
  computed: {},
  mounted() {
    this.getCharacterRoles()
    this.getSetting()
  },
  methods: {
    onOversize(file) {
      this.$toast('文件大小不能超过 500kb')
    },
    deleteFile(file) {
      const files = this.fileList

      for (var i = 0; i < files.length; i++) {
        const f = files[i]
        if (f.content == file.content) {
          console.log(i)
          this.fileList.splice(i, 1)
          this.urls.splice(i, 1)
          break
        }
      }
    },
    afterRead(e) {
      // 此时可以自行将文件上传至服务器
      const content = e.file
      const data = new FormData()
      data.append('file', content)
      upLoadFile(data)
        .then(res => {
          if (res.code == 200) {
            this.urls.push(res.url)
          }
        })
        .catch(() => {
          return this.$toast('上传失败')
        })
    },
    getCharacterRoles() {
      getRole().then(res => {
        this.role = res.data
      })
    },
    getSetting() {
      getApplySetting().then(res => {
        this.applySetting = res.data
      })
    },

    onSubmit(values) {
      let image = ''
      if (this.urls.length > 0) {
        image = this.urls.join(',')
      }
      applyCreator({
        inviteCode: '',
        characterRoleId: this.characterRoleId,
        applyCharacterName: this.applyCharacterName,
        personalSpecialty: this.personalSpecialty,
        socialPlatformInfo: this.socialPlatformInfo,
        applyRemark: this.applyRemark,
        applyImage: image,
        applySettingId: this.applySetting.id
      }).then(res => {
        this.$toast('申请成功')
        this.$router.back()
      })
    },
    select(e) {
      this.selectTag = e
      this.characterRoleId = e
    }
  }
}
</script>
<style lang="scss" scoped>
.tiltle-view {
  position: absolute;
  top: 40px;
  left: 20px;
}
.role-view {
  width: 345px;
  margin-left: 15px;
  font-weight: 350;
  border-bottom: 1px solid #ebedf0;
}
.image-view {
  width: 345px;
  margin-left: 15px;
  font-weight: 350;
}
.role-view::after {
  border-bottom: 1px solid #ebedf0;
}
.font-size-24 {
  font-size: 24px;
}
.pdl-100 {
  padding-left: 100px;
}
.to-right {
  display: flex !important;
  justify-content: right;
  align-items: center;
}
.van-cell {
  background: transparent;
  color: white;
}

/deep/.van-field__label {
  color: white;
}
/deep/.van-field__control {
  color: white;
}
// /deep/.van-cell::after {
//    border-bottom: 1px solid #ebedf0;
// }
.van-icon-checked {
  color: #6becfa;
}
.submit-btn {
  width: 335px;
  height: 44px;
  background: linear-gradient(180deg, #3aa9f5 0%, #6becfa 100%);
  border-radius: 22px;
  margin-top: 100px;
  margin-left: 15px;
}
</style>
