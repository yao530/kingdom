package com.ltu.controller;

import com.ltu.config.shiro.ShiroUtil;
import com.ltu.config.shiro.dto.LoginReq;
import com.ltu.config.shiro.dto.SmsCodeLoginReq;
import com.ltu.model.request.user.UserForgotPassword;
import com.ltu.model.request.user.UserRegisterReq;
import com.ltu.model.request.user.UserResetPasswordReq;
import com.ltu.model.request.weixin.QrCodeReq;
import com.ltu.model.response.CodeResp;
import com.ltu.model.response.base.CodeDataResp;
import com.ltu.model.response.dto.AccountLoginDto;
import com.ltu.model.response.dto.UserBaseInfoItem;
import com.ltu.model.response.dto.UserInfoItem;
import com.ltu.service.*;
import com.ltu.util.BeanMapper;
import com.ltu.util.common.StrUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;

@RestController
@RequestMapping("/login")
@Api(tags = "A-登录注册重置")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LoginController {
    private final UserService userService;

    private final AccountService accountService;

    private final WxPubService wxPubService;





    @ApiOperation(value="G:登录", notes = "用户名密码登录")
    @RequestMapping(value="/login", method= RequestMethod.POST)
    public CodeDataResp<UserInfoItem> login(@Valid @RequestBody LoginReq userLoginReq) {
        return BeanMapper.copyRespDataItem(userService.login(userLoginReq), UserBaseInfoItem.class.getName());
    }

    @ApiOperation(value="G:短信验证码登录", notes = "已注册用户登录，未注册用户自动注册后登陆")
    @RequestMapping(value="/loginBySmsCode", method= RequestMethod.POST)
    public CodeDataResp<UserInfoItem> loginBySmsCode(@Valid @RequestBody SmsCodeLoginReq userLoginReq) {
        return BeanMapper.copyRespDataItem(userService.loginBySmsCode(userLoginReq), UserBaseInfoItem.class.getName());
    }

    @ApiOperation(value="G:后台登录", notes = "用户名密码登录")
    @RequestMapping(value="/loginByAccount", method= RequestMethod.POST)
    public CodeDataResp<AccountLoginDto> loginByAccount(@Valid @RequestBody LoginReq userLoginReq) {
        return accountService.login(userLoginReq);
    }

    @ApiOperation(value="W:获取微信code授权地址", notes = "获取微信code授权地址")
    @RequestMapping(value="/getwxAuthUrl", method= RequestMethod.POST)
    public CodeDataResp getwxAuthUrl(@RequestBody QrCodeReq qrCodeReq) {

        return CodeDataResp.valueOfSuccess(wxPubService.getQrCode(qrCodeReq));
    }

    @ApiOperation(value="G:wx登录", notes = "wx登录")
    @RequestMapping(value="/loginByWx", method= RequestMethod.POST)
    public CodeDataResp loginByWx(@Valid @RequestBody LoginReq userLoginReq) {
            if (StrUtils.isTrimNull(userLoginReq.getCode()))
                return CodeDataResp.valueOfFailed("用户code不能为空");

        return CodeDataResp.valueOfSuccess(wxPubService.getGZHOpenIdByCode(userLoginReq.getCode()));
    }


    @ApiOperation(value="G:测试okex", notes = "用户名密码登录")
    @RequestMapping(value="/test", method= RequestMethod.POST)
    public CodeDataResp<UserInfoItem> test() throws Exception{
        return userService.testOkex();
    }

}
