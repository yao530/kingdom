package com.ltu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ltu.domain.mp_entity.UserEntity;
import com.ltu.domain.mp_entity.UserInfoEntity;

import com.ltu.mapper.UserInfoMapper;
import com.ltu.model.request.user.UpdateUserInfoReq;
import com.ltu.service.UserInfoService;
import com.ltu.util.BeanMapper;
import com.ltu.util.datetime.DateUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户真实信息 服务实现类
 * </p>
 *
 * @author 若尘
 * @since 2022-05-18
 */
@Service
public class UserInfoServiceImpl extends BaseServiceImpl<UserInfoMapper, UserInfoEntity> implements UserInfoService {


    public void createrUserInfo(UserEntity user, UpdateUserInfoReq userInfoReq){
        UserInfoEntity userInfo = new UserInfoEntity();
        userInfo.setUserId(user.getId());
        BeanMapper.copy(userInfoReq,userInfo);
        userInfo.setCreateTime(DateUtils.currentSecs());
        super.saveOrUpdate(userInfo);
    }

    public UserInfoEntity getUserInfo(Integer userId){
        QueryWrapper<UserInfoEntity> condition = new QueryWrapper<>();
        condition.eq("user_id",userId);
        return super.getOne(condition);
    }

    public UserInfoEntity getUserInfoByIdentityId(String identityId){
        QueryWrapper<UserInfoEntity> condition = new QueryWrapper<>();
        condition.eq("identity_id",identityId);
        return super.getOne(condition);
    }
}
