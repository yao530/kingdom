/**
 * FileName: AuthServiceImpl
 * Author:   Phil
 * Date:     11/21/2018 12:13 PM
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.ltu.service.impl;

import com.google.gson.JsonSyntaxException;
import com.ltu.config.wechat.ResultState;
import com.ltu.config.wechat.WechatAccountConfig;
import com.ltu.config.wechat.auth.BasicAuthParam;
import com.ltu.config.wechat.auth.WechatAuthConfig;
import com.ltu.config.wechat.auth.response.AccessToken;
import com.ltu.constant.CommonConstant;
import com.ltu.domain.mp_entity.UserEntity;
import com.ltu.model.response.base.CodeDataResp;
import com.ltu.service.UserService;
import com.ltu.service.WechatAuthService;
import com.ltu.util.HttpUtil;
import com.ltu.util.common.StrUtils;
import com.ltu.util.http.HttpPostUtil;
import com.ltu.util.http.JsonUtil;
import com.ltu.util.redis.RedistUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ltu.config.wechat.auth.response.AuthAccessToken;
import com.ltu.config.wechat.auth.response.AuthUserInfo;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Phil
 * @create 11/21/2018 12:13 PM
 * @since 1.0
 */
@Service
@Slf4j
public class WechatAuthServiceImpl implements WechatAuthService {

    @Resource
    RedistUtil redisUtils;

    @Resource
    WechatAuthConfig wechatAuthConfig;

    @Autowired
    UserService userService;
    @Resource
    private WechatAccountConfig wechatAccountConfig;

    private final static String GZH_TOKEN="GZH:";

    /**
     * 获取授权凭证token
     *
     * @return 授权凭证token
     */
    @Override
    public String getAccessToken() {
        String accessToken = (String) redisUtils.getValue(GZH_TOKEN);

        if (StrUtils.isTrimNull(accessToken)) {
            Map<String, String> map = new TreeMap<>();
            map.put("appid", wechatAccountConfig.getAppId());
            map.put("secret", wechatAccountConfig.getAppSecret());
            map.put("grant_type", "client_credential");
            String json = HttpUtil.doGet(wechatAuthConfig.getGetAccessTokenUrl(), map);
            AccessToken bean = JsonUtil.fromJson(json, AccessToken.class);
            if (bean != null) {
                accessToken = bean.getAccessToken();
                log.info("从微信服务器获取的授权凭证{}", accessToken);
                redisUtils.setValue(GZH_TOKEN, accessToken, 60 * 120L);
                log.info("从微信服务器获取的token缓存到Redis");
            }
        }
        return accessToken;
    }

    /**
     * 获取授权请求url
     *
     * @param
     * @param
     * @return
     * @throws Exception
     */
    @Override
    public String getAuthUrl() {
        String url = CommonConstant.GET_USER_CODE+"appid="+wechatAccountConfig.getAppId()+"&redirect_uri="+
                CommonConstant.GZH_AUTHORIZER_CALLBACK+"&response_type=code&scope=snsapi_base&state="+StrUtils.getRandomNumberStr(6)+"#wechat_redirect";
        return url;
    }

    /**
     * 获取网页授权凭证
     *
     * @param
     * @param
     * @return
     */
    @Override
    public CodeDataResp<UserEntity> getAuthAccessToken(String code) {
        String   preAuthCodeUrl=CommonConstant.GET_USER_OPENID+"appid="+wechatAccountConfig.getAppId()+"&secret="+wechatAccountConfig.getAppSecret()+"&code="+code+"&grant_type=authorization_code";
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

        return CodeDataResp.valueOfFailed("获取公众号粉丝授权失败");
    }

    /**
     * 刷新网页授权验证
     *
     * @param basic 参数
     * @param url   请求路径
     * @return 新的网页授权验证
     */
    @Override
    public AuthAccessToken refreshAuthAccessToken(BasicAuthParam basic, String url) {
        try {
            String result = HttpUtil.doGet(wechatAuthConfig.getRefreshOauthTokenUrl(), basic.getParams());
            return JsonUtil.fromJson(result, AuthAccessToken.class);
        } catch (Exception e) {
            log.debug("error" + e.getMessage());
        }
        return null;
    }

    /**
     * 通过网页授权获取用户信息
     *
     * @param accessToken 网页授权接口调用凭证
     * @param openid      用户唯一标识
     * @return 用户信息
     */
    @Override
    public AuthUserInfo getAuthUserInfo(String accessToken, String openid) {
        // 通过网页授权获取用户信息
        Map<String, String> params = new HashMap<>();
        params.put("access_token", accessToken);
        params.put("openid", openid);
        String result = HttpUtil.doGet(wechatAuthConfig.getSnsUserinfoUrl(), params);
        try {
            return JsonUtil.fromJson(result, AuthUserInfo.class);
        } catch (JsonSyntaxException e) {
            log.debug("transfer exception");
        }
        return null;
    }

    /**
     * 检验授权凭证（access_token）是否有效
     *
     * @param accessToken 网页授权接口调用凭证
     * @param openid      用户唯一标识
     * @return { "errcode":0,"errmsg":"ok"}表示成功 { "errcode":40003,"errmsg":"invalid
     * openid"}失败
     */
    @Override
    public ResultState authToken(String accessToken, String openid) {
        Map<String, String> params = new HashMap<>();
        params.put("access_token", accessToken);
        params.put("openid", openid);
        String jsonResult = HttpUtil.doGet(
                wechatAuthConfig.getCheckSnsAuthStatusUrl(), params);
        return JsonUtil.fromJson(jsonResult, ResultState.class);
    }
}
