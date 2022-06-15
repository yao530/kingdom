package com.ltu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ltu.config.shiro.dto.LoginReq;
import com.ltu.config.shiro.dto.SmsCodeLoginReq;
import com.ltu.domain.mp_entity.UserEntity;
import com.ltu.model.request.base.BaseIdReq;
import com.ltu.model.request.user.*;
import com.ltu.model.response.base.CodeDataResp;

import java.util.List;


/**
 * <p>
 *  服务类
 * </p>
 *
 * @author vk@rongding
 * @since 2020-08-15
 */
public interface UserService extends IService<UserEntity> {

    /**
     * 用户登录
     * @param userLoginReq
     * @return
     */
    CodeDataResp login(LoginReq userLoginReq);

    /**
     * 短信验证码登陆
     * @param userLoginReq
     * @return
     */
    CodeDataResp loginBySmsCode(SmsCodeLoginReq userLoginReq);

    /**
     * 注销登录
     * @return
     */
    CodeDataResp logout();

//    /**
//     * 用户注册
//     * @param userRegisterReq
//     * @return
//     */
//    CodeDataResp register(UserRegisterReq userRegisterReq);

    /**
     * 重置密码
     * @param req
     * @return
     */
    CodeDataResp resetPassword(UserResetPasswordReq req);

    /**
     * 忘记密码
     * @param req
     * @return
     */
    CodeDataResp forgotPassword(UserForgotPassword req);

    /**
     * 获取用户详细信息
     * @param baseIdReq
     * @return
     */
    CodeDataResp getUserInfo(BaseIdReq baseIdReq);

    UserEntity getUser(String userLoginName);

    /**
     * 更新用户信息
     * @param userReq
     * @return
     */
    CodeDataResp updateUserInfo(UpdateUserInfoReq userReq);

    /**
     * MP-获取用户列表
     * @param pageReq
     * @return
     */
    CodeDataResp getList(UserPageReq pageReq);

    /**
     * 获取创造者
     * @param
     * @return
     */
    List<UserEntity> getCreatorUsers();


    /**
     * MP-获取用户完整信息
     * @param baseIdReq
     * @return
     */
    CodeDataResp getDetail(BaseIdReq baseIdReq);

    /**
     * 通过手机号获取用户信息
     * @param userPhone
     * @return
     */
    UserEntity getUserByPhone(String userPhone);

    CodeDataResp<UserEntity> registerByWxGzh(String openId);

    CodeDataResp testOkex() throws Exception;

}
