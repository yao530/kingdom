/**
 * 从富文本中给图片链接添加 random 并且返回图片数组
 * @example let [html, imgUrls] = addRandomAndGetPreviewImageUrlsFromRichText(html)
 * @param {string} html
 */
// function addRandomAndGetPreviewImageUrlsFromRichText (html) {
const addRandomAndGetPreviewImageUrlsFromRichText = html => {
  // 如果没有值的话，直接返回
  if (!html) {
    return html
  }

  let randomIndex = 0
  const imgUrls = []
  // 先匹配到 img 标签，放到 match 里
  html = html.replace(/<img[^>]*>/gim, function(match) {
    randomIndex++
    match = match.replace(/src="[^"]+"/gim, function(match) {
      // 再匹配到 src 标签 '"'
      const src = match.slice(5, -1) + '?random=' + randomIndex // 取到 src 里面的 url
      imgUrls.push(src)
      return 'src="' + src + '"'
    }).replace(/src='[^']+'/gim, function(match) {
      // 再匹配到 src 标签 "'"
      const src = match.slice(5, -1) + '?random=' + randomIndex
      return "src='" + src + "'"
    })

    return match
  })
  return [html, imgUrls]
}

module.exports = {
  addRandomAndGetPreviewImageUrlsFromRichText: addRandomAndGetPreviewImageUrlsFromRichText
}
