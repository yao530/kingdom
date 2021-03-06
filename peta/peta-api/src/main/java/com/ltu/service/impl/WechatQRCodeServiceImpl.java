/**
 * FileName: WechatQRCodeServiceImpl
 * Author:   Phil
 * Date:     11/21/2018 12:17 PM
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.ltu.service.impl;

import com.google.gson.JsonObject;

import com.ltu.config.wechat.util.EncodeUtils;
import com.ltu.model.request.weixin.WechatQRCode;
import com.ltu.model.request.weixin.WechatQRCodeShortUrl;

import com.ltu.service.WechatQRCodeService;
import com.ltu.util.HttpUtil;
import com.ltu.util.http.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ltu.config.wechat.WechatQRCodeConfig;
import java.util.TreeMap;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Phil
 * @create 11/21/2018 12:17 PM
 * @since 1.0
 */
@Service
@Slf4j
public class WechatQRCodeServiceImpl implements WechatQRCodeService {


    @Autowired
    private WechatQRCodeConfig WechatQRCodeConfig;

    /**
     * 创建临时带参数二维码
     *
     * @param accessToken
     * @param sceneId     场景Id
     * @return
     * @expireSeconds 该二维码有效时间，以秒为单位。 最大不超过2592000（即30天），此字段如果不填，则默认有效期为30秒。
     */
    @Override
    public String createTempTicket(String accessToken, int sceneId, int expireSeconds) {
        TreeMap<String, String> params = new TreeMap<>();
        params.put("access_token", accessToken);
        // output data
        JsonObject data = new JsonObject();
        data.addProperty("action_name", WechatQRCodeConfig.QR_SCENE);
        data.addProperty("expire_seconds", expireSeconds);
        JsonObject scene = new JsonObject();
        scene.addProperty("scene_id", sceneId);
        JsonObject actionInfo = new JsonObject();
        actionInfo.add("scene", scene);
        data.add("action_info", actionInfo);
        String result = HttpUtil.doPost(WechatQRCodeConfig.getCreateTicketUrl(), params, data.toString());
        WechatQRCode qrcode = JsonUtil.fromJson(result, WechatQRCode.class);
        return qrcode == null ? null : qrcode.getTicket();
    }

    /**
     * 创建临时带参数二维码(字符串)
     *
     * @param accessToken
     * @param sceneStr    场景Id
     * @return
     * @expireSeconds 该二维码有效时间，以秒为单位。 最大不超过2592000（即30天），此字段如果不填，则默认有效期为30秒。
     */
    @Override
    public String createTempTicket(String accessToken, String sceneStr, int expireSeconds) {
        TreeMap<String, String> params = new TreeMap<>();
        params.put("access_token", accessToken);
        // output data
        JsonObject data = new JsonObject();
        data.addProperty("action_name", WechatQRCodeConfig.QR_STR_SCENE);
        data.addProperty("expire_seconds", expireSeconds);
        JsonObject scene = new JsonObject();
        scene.addProperty("scene_str", sceneStr);
        JsonObject actionInfo = new JsonObject();
        actionInfo.add("scene", scene);
        data.add("action_info", actionInfo);
        String result = HttpUtil.doPost(WechatQRCodeConfig.getCreateTicketUrl(), params, data.toString());
        WechatQRCode qrcode = JsonUtil.fromJson(result, WechatQRCode.class);
        return qrcode == null ? null : qrcode.getTicket();
    }

    /**
     * 创建永久二维码(数字)
     *
     * @param accessToken
     * @param sceneId     场景Id
     * @return
     */
    @Override
    public String createForeverTicket(String accessToken, int sceneId) {
        TreeMap<String, String> params = new TreeMap<>();
        params.put("access_token", accessToken);
        // output data
        JsonObject data = new JsonObject();
        data.addProperty("action_name", WechatQRCodeConfig.QR_LIMIT_SCENE);
        JsonObject scene = new JsonObject();
        scene.addProperty("scene_id", sceneId);
        JsonObject actionInfo = new JsonObject();
        actionInfo.add("scene", scene);
        data.add("action_info", actionInfo);
        String result = HttpUtil.doPost(WechatQRCodeConfig.getCreateTicketUrl(), params, data.toString());
        WechatQRCode qrcode = JsonUtil.fromJson(result, WechatQRCode.class);
        return qrcode == null ? null : qrcode.getTicket();
    }

    /**
     * 创建永久二维码(字符串)
     *
     * @param accessToken
     * @param sceneStr    场景str
     * @return
     */
    @Override
    public String createForeverTicket(String accessToken, String sceneStr) {
        TreeMap<String, String> params = new TreeMap<>();
        params.put("access_token", accessToken);
        // output data
        JsonObject data = new JsonObject();
        data.addProperty("action_name", "QR_LIMIT_STR_SCENE");
        JsonObject actionInfo = new JsonObject();
        JsonObject scene = new JsonObject();
        scene.addProperty("scene_str", sceneStr);
        actionInfo.add("scene", scene);
        data.add("action_info", actionInfo);
        String result = HttpUtil.doPost(WechatQRCodeConfig.getCreateTicketUrl(), params, data.toString());
        WechatQRCode qrcode = JsonUtil.fromJson(result, WechatQRCode.class);
        return qrcode == null ? null : qrcode.getTicket();
    }

    /**
     * 获取二维码ticket后，通过ticket换取二维码图片展示
     *
     * @param accessToken
     * @param ticket
     * @param isShortUrl  是否需要展示
     * @return
     */
    @Override
    public String showQrCode(String ticket, boolean isShortUrl) {
        String url =WechatQRCodeConfig.getShowQrcodeUrl()+ EncodeUtils.urlEncode(ticket);
//        if (isShortUrl) {
//            return toShortQRCodeurl(accessToken, url);
//        }
       return url;
    }

    /**
     * 长链接转短链接
     *
     * @param accessToken
     * @param longUrl     长链接
     * @return
     */
    private String toShortQRCodeurl(String accessToken, String longUrl) {
        TreeMap<String, String> params = new TreeMap<>();
        params.put("access_token", accessToken);
        JsonObject data = new JsonObject();
        data.addProperty("action", WechatQRCodeConfig.LONG_TO_SHORT);
        data.addProperty("long_url", longUrl);
        String result = HttpUtil.doPost(WechatQRCodeConfig.getShortQrcodeUrl(),
                params, data.toString());
        WechatQRCodeShortUrl wechatQRCodeShortUrl = JsonUtil.fromJson(result, WechatQRCodeShortUrl.class);
        return wechatQRCodeShortUrl == null ? null : wechatQRCodeShortUrl.getShortUrl();
    }

}
