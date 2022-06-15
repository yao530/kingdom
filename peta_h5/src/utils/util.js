/**
 * Created by Sunnie on 19/06/04.
 */

/**
 * @param {string} item
 * @returns {string}
 */

export function transformNftInfo(item) {
  // eslint-disable-next-line eqeqeq
  if (item.soldStatus == 0) {
    item.statusTitle = '待售'
    item.showCountDown = true
    item.time = 30 * 60 * 60 * 1000
  // eslint-disable-next-line no-empty
  } else if (item.soldStatus === 1) {
    item.statusTitle = '售卖中'
    item.showCountDown = false
    item.time = null
  // eslint-disable-next-line no-empty
  } else {
    item.statusTitle = '已售馨'
    item.showCountDown = false
    item.time = null
  }

  return item
}

