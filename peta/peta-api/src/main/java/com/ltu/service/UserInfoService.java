package com.ltu.service;

import com.ltu.domain.mp_entity.UserEntity;
import com.ltu.domain.mp_entity.UserInfoEntity;
import com.ltu.model.request.user.UpdateUserInfoReq;


/**
 * <p>
 * 用户真实信息 服务类
 * </p>
 *
 * @author 若尘
 * @since 2022-05-18
 */

public interface UserInfoService extends BaseTService<UserInfoEntity> {

    void createrUserInfo(UserEntity user, UpdateUserInfoReq userInfoReq);

    UserInfoEntity getUserInfo(Integer userId);

    UserInfoEntity getUserInfoByIdentityId(String identityId);
}
