package com.ltu.service;

import com.ltu.model.request.weixin.QrCodeReq;
import com.ltu.model.response.CodeResp;
import net.sf.json.JSONObject;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author vk@rongding123
 * @since 2020-08-25
 */

public interface WxPubService {

    /**
     * 获取公众号的token
     * @param
     * @return
     */
    String getWxGzhToken();

    /**
     * 获取用户的openid
     * @param
     * @return
     */
    String getUserAuths();

    CodeResp getGZHOpenIdByCode(String code);

    String getQrCode(QrCodeReq qrCodeReq);


}
