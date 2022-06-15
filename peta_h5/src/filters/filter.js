/**
 *格式化时间
 *yyyy-MM-dd hh:mm:ss
 */
export function formatDate(time, fmt) {
  if (time === undefined || '') {
    return
  }
  const date = new Date(time)
  if (/(y+)/.test(fmt)) {
    fmt = fmt.replace(RegExp.$1, (date.getFullYear() + '').substr(4 - RegExp.$1.length))
  }
  const o = {
    'M+': date.getMonth() + 1,
    'd+': date.getDate(),
    'h+': date.getHours(),
    'm+': date.getMinutes(),
    's+': date.getSeconds()
  }
  for (const k in o) {
    if (new RegExp(`(${k})`).test(fmt)) {
      const str = o[k] + ''
      fmt = fmt.replace(RegExp.$1, RegExp.$1.length === 1 ? str : padLeftZero(str))
    }
  }
  return fmt
}

function padLeftZero(str) {
  return ('00' + str).substr(str.length)
}
/*
 * 隐藏用户手机号中间四位
 */
export function hidePhone(phone) {
  return phone.replace(/(\d{3})\d{4}(\d{4})/, '$1****$2')
}

/**
 * @param {Array} item
 * @returns {Array}
 */
export function transformNftInfo(array) {
  // eslint-disable-next-line no-unused-vars
  var List = []
  if (array.length > 0) {
    for (var i = 0; i < array.length; i++) {
      var item = array[i]
      List.push(buildItem(item))
    }
  }
  return List
}

export function buildItem(item) {
  item = setSoldType(item)
  item.storyLine = '《' + item.storyTaleTitle + '》' + ' 第' + item.chapterNumber + '话' + ' ' + item.storyTitle
  item.storyTaleTitle = '《' + item.storyTaleTitle + '》'
  item.chapterNumber = '第' + item.chapterNumber + '话'
  // eslint-disable-next-line eqeqeq
  if (item.soldStatus == 4) {
    if (item.soldType !== 1) {
      item.statusTitle = '已售馨'
    } else {
      item.statusTitle = '活动结束'
    }
    item.showCountDown = false
  } else if (item.soldStatus === 0) {
    item.statusTitle = '待开始'
    item.showCountDown = true
    if (item.soldType !== 3) {
      item.distanceTime = item.bookStartTime - Date.now()
    } else {
      item.distanceTime = item.startTime - Date.now()
    }
  // eslint-disable-next-line no-empty
  } else if (item.soldStatus === 1) {
    item.showCountDown = true
    if (item.soldType !== 3) {
      item.distanceTime = item.bookEndTime - Date.now()
    } else {
      item.distanceTime = item.endTime - Date.now()
    }
    if (item.soldType === 1) {
      item.statusTitle = '领空投'
    } else if (item.soldType === 2) {
      item.statusTitle = '预约抽签'
    } else {
      item.statusTitle = '立即抢购'
    }
    // eslint-disable-next-line no-empty
  } else if (item.soldStatus === 2) {
    item.statusTitle = '抽签公布中'
    item.showCountDown = false
  } else {
    item.statusTitle = '立即抢购'
    item.showCountDown = false
  }
  return selCollectionType(item)
}

export function selCollectionType(item) {
  const collectionType = item.collectionType
  switch (collectionType) {
    case 1:
      item.collectionTypeName = '故事脚本'
      break
    case 2:
      item.collectionTypeName = '叙事背景'
      break
    case 3:
      item.collectionTypeName = 'Avatar'
      break
    case 4:
      item.collectionTypeName = '叙事事件'
      break
    case 5:
      item.collectionTypeName = '叙事道具'
      break
    case 6:
      item.collectionTypeName = '叙事权益'
      break
    case 7:
      item.collectionTypeName = '叙事音乐'
      break
    case 8:
      item.collectionTypeName = '叙事彩蛋'
      break
  }
  return item
}

export function setSoldType(item) {
  if (item.soldType === 1) {
    item.soldTypeName = item.collectionOpenType === 2 ? '盲盒空投' : '限量空投'
  } else if (item.soldType === 2) {
    item.soldTypeName = item.collectionOpenType === 2 ? '盲盒抽签' : '预约抽签'
  } else {
    item.soldTypeName = item.collectionOpenType === 2 ? '盲盒抢购' : '限量抢购'
  }
  item.collectionOpenTypeName = item.collectionOpenType === 1 ? '普通活动' : '盲盒活动'
  return item
}

export function countDownItem(item) {
  if (item.soldStatus === 0) {
    if (item.soldType === 3) {
      item.soldStatus = 3
      item.statusTitle = '立即抢购'
    } else {
      item.soldStatus = 1
      if (item.soldType === 1) {
        item.statusTitle = '领空投'
      } else {
        item.statusTitle = '预约抽签'
      }
    }
  } else {
    item.soldStatus = 2
    if (item.soldType === 1) {
      item.statusTitle = '领空投'
    } else {
      item.statusTitle = '抽签中'
    }
  }
  console.log(item)
  return item
}
