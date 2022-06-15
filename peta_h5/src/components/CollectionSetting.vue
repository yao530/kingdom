<template>
  <div>
    <div class="appointment-view bg white pad-10 margin-top-15 font-size-14">
      <div class="appointment-title flex-row">
        <div class="mid-gray">{{ data.soldType == 1 ? '空投时间' : '预约时间' }}</div>
        <div class="margin-left-15">{{ data.bookStartTime | formatDate }} ~ {{ data.bookEndTime | formatDate }}</div>
      </div>
      <div class="appointment-title flex-row">
        <div class="mid-gray">抽签时间</div>
        <div class="margin-left-15">{{ data.bookEndTime | formatDate }}</div>
      </div>
      <!-- <div class="appointment-title flex-row">
             <div class="mid-gray">预约流程</div>
             <div class="margin-left-15">1、预约 2、助力 3、中签 4、购买</div>
          </div> -->
      <div class="mission-view flex-row">
        <div class="mid-gray width-50">
          好友助力
          <span class="suscess-status">{{ data.joinDropEntity ? data.joinDropEntity.invitedUserNumber : '' }}</span>
          <span>/{{ data.needInvitedNumber }}</span>
        </div>
        <div class="invite-egg width-50 flex-row">
          <!-- <van-icon size="22" class="iconfont icon-caidan-" /> -->
          <div class="center light-gold font-size-12">
            {{ data.needInvited == 1 ? '需完成助力任务' : '助力人数提高中签概率' }}
          </div>
        </div>
      </div>
      <div class="mission-view">
        <div class="invite-view flex-row margin-top-20">
          <div v-for="(item, index) in data.inviteList" :key="item.id" class="flex-column center width-25">
            <div>
              <img class="avatar" :src="item.invitedUserAvatar">
            </div>
            <div v-show="index == 0">
              <span class="font-size-12 suscess-status">{{ item.status == 0 ? '待参加' : '已参加' }}</span>
            </div>
            <div v-show="index != 0 && item.status == 0">
              <span class="font-size-12 cancel-status">待助力</span>
            </div>
            <div v-show="index != 0 && item.status == 1">
              <span class="font-size-12 suscess-status">已助力</span>
            </div>
          </div>
        </div>

        <div class="result margin-top-20">
          <div class="flex-row">
            <div class="result-icon">
              <van-icon size="40px" class="iconfont icon-xiadanchoujiang" />
            </div>
            <div class="result-info flex-column margin-top-5">
              <div v-if="data.soldType == 1">
                <div v-show="data.dropType == 1" class="cancel-status font-size-12">
                  {{ data.needInvited == 1 ? '参加空投限量领取' : '参加空投完成邀请任务限量领取' }}
                </div>
                <div v-show="data.dropType == 2" class="cancel-status font-size-12">
                  {{ data.needInvited == 1 ? '参加空投限量抽签领取' : '参加空投完成邀请任务抽签' }}
                </div>
                <div v-show="data.dropType == 3" class="cancel-status font-size-12">
                  {{ data.needInvited == 1 ? '参加空投限量抽签领取' : '参加空投按邀请人数排名优先领取' }}
                </div>
                <div v-show="data.dropType == 4" class="cancel-status font-size-12">
                  {{ data.needInvited == 1 ? '参加空投限量抽签领取' : '参加空投按邀请任务完成排名限量领取' }}
                </div>
              </div>
              <div v-if="data.soldType == 2">
                <div class="cancel-status">{{ data.needInvited == 1 ? '参加预约抽签' : '参加预约抽签' }}</div>
              </div>

              <div class="font-size-12">
                <span>{{ data.totalJoinNumber }}人已参加</span>
                <span
                  v-if="data.needInvited == 0"
                  class="margin-left-10"
                >参加排名{{ data.joinDropEntity.joinDropRanking }}</span>
                <span
                  v-if="data.needInvited == 1 && data.dropType == 3"
                  class="margin-left-10"
                >邀请排名{{ data.joinDropEntity.invitedNumberRanking }}</span>
                <span
                  v-if="data.needInvited == 0 && data.dropType == 4"
                  class="margin-left-10"
                >完成排名{{ data.joinDropEntity.finshInvitedRanking }}</span>
              </div>
            </div>
            <div v-if="data.joinDropEntity" class="result-status center">
              <div class="font-size-14" />
              <div v-if="data.joinDropEntity.dropStatus == -1" class="white">待参加</div>
              <div v-else-if="data.joinDropEntity.dropStatus == 0" class="wait-status">待抽签</div>
              <div v-else-if="data.joinDropEntity.dropStatus == 1" class="suscess-status">已中签</div>
              <div v-else class="cancel-status">未中签</div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import Vue from 'vue'
import { Icon } from 'vant'
import { formatDate } from '@/filters/filter'

Vue.use(Icon)

export default {
  name: 'CollectionSetting',
  filters: {
    formatDate(time) {
      var date = new Date(time)

      return formatDate(date, 'MM-dd hh:mm')
    }
  },
  props: {
    data: {
      type: Object,
      default: () => {
        return {}
      }
    }
  },
  data() {
    return {}
  },
  computed: {},
  created() {},
  beforeDestroy: function() {},
  mounted() {},
  methods: {
    clickItem(e) {
      this.$emit('click', e)
    }
  }
}
</script>

<style lang="scss" scoped>
.bg {
  background: rgba(35, 38, 47, 0.9);
}

.appointment-view {
  border-radius: 10px;
  .appointment-title {
    height: 30px;
    padding-left: 5px;
    padding-bottom: 5px;
    padding-top: 5px;
  }
  .mission-view {
    padding-left: 5px;
    padding-bottom: 5px;
    padding-top: 5px;
  }
}
.width-25 {
  width: 25%;
}
.width-50 {
  width: 50%;
}
.text-right {
  text-align: right;
}
.invite-view {
  text-align: left;
}
.avatar {
  height: 30px;
  width: 30px;
  border-radius: 15px;
}
.result {
  padding: 10px;
  width: 300px;
  height: 50px;
  background-color: black;
  border-radius: 4px;
  display: flex !important;

  align-items: center;
  .result-icon {
    width: 45px;
    .icon {
      width: 30px;
      height: 30px;
    }
  }
  .result-info {
    width: 210px;
  }
}
.invite-egg {
  display: flex !important;
  justify-content: right;
  align-items: center;
}
</style>
