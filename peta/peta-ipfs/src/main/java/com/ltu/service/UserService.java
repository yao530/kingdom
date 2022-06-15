package com.ltu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ltu.config.shiro.dto.LoginReq;
import com.ltu.config.shiro.dto.SmsCodeLoginReq;
import com.ltu.domain.mp_entity.UserEntity;
import com.ltu.model.request.base.BaseIdReq;
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
     * 获取用户详细信息
     * @param baseIdReq
     * @return
     */
    CodeDataResp getUserInfo(BaseIdReq baseIdReq);

    UserEntity getUser(String userLoginName);






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




}
