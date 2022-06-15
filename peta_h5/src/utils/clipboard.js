/*
 * @Author: lija 904050844@qq.com
 * @Date: 2022-06-05 15:06:33
 * @LastEditors: lija 904050844@qq.com
 * @LastEditTime: 2022-06-05 15:51:45
 * @FilePath: \peta_h5\src\utils\clipboard.js
 * @Description:
 *
 * Copyright (c) 2022 by lija 904050844@qq.com, All Rights Reserved.
 */
import { Toast } from 'vant'
import Clipboard from 'clipboard'

function clipboardSuccess() {
  Toast.success('复制成功')
}

function clipboardError() {
  Toast.fail('复制失败')
}

export default function handleClipboard(text) {
  const clipboard = new Clipboard('.btn', {
    text: () => text
  })
  clipboard.on('success', () => {
    clipboardSuccess()
    clipboard.off('error')
    clipboard.off('success')
    clipboard.destroy()
  })
  clipboard.on('error', () => {
    clipboardError()
    clipboard.off('error')
    clipboard.off('success')
    clipboard.destroy()
  })
  clipboard.onClick(event)
}
