package com.ltu.service.impl;


import com.ltu.config.wechat.WechatQRCodeConfig;
import com.ltu.config.wechat.util.EncodeUtils;
import com.ltu.constant.CommonConstant;

import com.ltu.model.request.weixin.CodePostReq;
import com.ltu.model.request.weixin.QrCodeReq;
import com.ltu.model.request.weixin.SceneIdReq;
import com.ltu.model.request.weixin.SceneReq;
import com.ltu.model.response.CodeResp;
import com.ltu.service.WechatQRCodeService;
import com.ltu.service.WxPubService;

import com.ltu.util.common.StrUtils;
import com.ltu.util.http.HttpPostUtil;
import com.ltu.util.redis.RedistUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author vk@rongding123
 * @since 2020-08-25
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WxPubServiceImpl implements WxPubService {

	@Value("${gzhInfo.appId}")
	private String appId;
	@Value("${gzhInfo.appSecret}")
	private String appKey;

    private final RedistUtil redistUtil;

    private final String GZH_TOKEN = "GZH_TOKEN:";
    private final Long GZH_TOKEN_REFRESH_TIME = Long.valueOf(CommonConstant.DEFAULT_LOGIN_EXPIRE_TIME);

    @Autowired
    UserServiceImpl userService;

    @Autowired
    WechatQRCodeService wechatQRCodeService;

    /**
     * 获取公众号的token
     * @param
     * @return
     */
    public String getWxGzhToken(){

        String token =(String) redistUtil.getValue(GZH_TOKEN);
        if (StrUtils.isTrimNull(token)){
            String url = CommonConstant.GZH_TOKEN_URL+"appid="+appId+"&secret="+appKey;
            String   str= HttpPostUtil.sendPost(url,"");
            net.sf.json.JSONObject result=	net.sf.json.JSONObject.fromObject(str);
            if (result!=null&&result.containsKey("access_token")){
                token = result.getString("access_token");
                redistUtil.setValue(GZH_TOKEN,token,GZH_TOKEN_REFRESH_TIME);
            }
            return "";
        }
        return token;
    }

    /**
     * 获取用户的code的链接
     * @param
     * @return
     */
    public String getUserAuths(){

        String url = CommonConstant.GET_USER_CODE+"appid="+appId+"&redirect_uri="+
                CommonConstant.GZH_AUTHORIZER_CALLBACK+"&response_type=code&scope=snsapi_base&state="+StrUtils.getRandomNumberStr(6)+"#wechat_redirect";

        return url;
    }



    /**
     * @time:2018年9月17日
     * @author:若尘
     * @param
     * @Description:获取公众号粉丝的openId
     * @param:@param AuthorizerInfo  AuthorizerInfo
     * @param:@return
     * @return:String
     */
    public CodeResp getGZHOpenIdByCode(String code) {

        String   preAuthCodeUrl=CommonConstant.GET_USER_OPENID+"appid="+appId+"&secret="+appKey+"&code="+code+"&grant_type=authorization_code";
        String   str= HttpPostUtil.sendPost(preAuthCodeUrl,"");
        net.sf.json.JSONObject result=	net.sf.json.JSONObject.fromObject(str);
        if(result.containsKey("openid")){
            String openId = result.getString("openid");
            if (!StrUtils.isTrimNull(openId)){
               return userService.registerByWxGzh(openId);
            }

        }else{
            System.out.println("获取公众号粉丝授权失败："+str);
            log.info("获取公众号粉丝授权失败："+str);
        }
       return CodeResp.valueOfSuccess();
    }

    /**
     * 获取公众号业务的临时二维码
     * @param
     * @return
     */
    public  String getQrCode(QrCodeReq qrCodeReq){

        JSONObject object = new JSONObject();
        object.put("scene_str",qrCodeReq.getSceneId());
        JSONObject info = new JSONObject();
        info.put("scene",object);
        JSONObject jsonObject = new net.sf.json.JSONObject();
        jsonObject.put("expire_seconds",qrCodeReq.getExpireTime());
        jsonObject.put("action_name","QR_STR_SCENE");
        jsonObject.put("action_info",info);

        String str= HttpPostUtil.sendPost(CommonConstant.GET_QR_CODE+getWxGzhToken(),jsonObject.toString());
        JSONObject result=	net.sf.json.JSONObject.fromObject(str);
        if (result!=null&&result.containsKey("ticket")){
            String ticket = result.getString("ticket");

            return wechatQRCodeService.showQrCode(ticket,false);
        }
        return "";
    }

    private String buildQrCodePostJson(String sceneIds, Long expireTime){

        CodePostReq codePostReq = new CodePostReq();
        codePostReq.setAction_name("QR_STR_SCENE");
        codePostReq.setExpire_seconds(expireTime);


        SceneIdReq sceneIdReq = new SceneIdReq();
        sceneIdReq.setScene_str(sceneIds);
        SceneReq sceneReq = new SceneReq();
        sceneReq.setScene(sceneIdReq);
        codePostReq.setAction_info(sceneReq);




        return com.alibaba.fastjson.JSON.toJSONString(codePostReq);
    }

    /**
     * @time:2018年9月17日
     * @author:若尘
     * @param
     * @Description:公众号发送模板消息  附带小程序链接
     * @param:@param AuthorizerInfo  AuthorizerInfo（包含当前公众号的令牌）
     * @param:@return
     * @return:String
     */
    public  String   sendMessageModel(String paramStr){

//        // 拼装创建菜单的url
//        //String token =  PropertiesUtils.getValue("Gzh_access_token");
//        String token = (String) wxTokenService.getCacheValue(3134l);//获取token
//        String s = HttpClientUtil.sendHTTPRequest("https://api.weixin.qq.com/cgi-bin/getcallbackip?access_token="+token,null, HTTPMethod.METHOD_GET);
//        JSONObject object =JSONObject.fromObject(s);
//        System.out.print("当前的token的状态是-------"+object);
//        System.out.print("当前的token是-------"+token);
//        if (!StrUtil.isTrimNull(token))
//        {
//            String url = CommonDir.GZH_MODEL_MESSAGE+token;
//            // 调用接口创建菜单
//            String  str= HttpPostUtil.sendPost (url, paramStr);
//            JSONObject jsonObject =JSONObject.fromObject(str);
//            if (0 != jsonObject.getInt("errcode")) {
//                log.error("发送模板消息失败,错误消息如下："+str);
//            }
//
//            return  str;
//        }

        return "fail";
    }

}
