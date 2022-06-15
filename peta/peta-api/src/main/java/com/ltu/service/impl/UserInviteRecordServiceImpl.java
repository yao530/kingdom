package com.ltu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ltu.config.shiro.ShiroUtil;
import com.ltu.config.shiro.dto.UserDto;
import com.ltu.domain.mp_entity.ArtCollectionEntity;
import com.ltu.domain.mp_entity.StoryEntity;
import com.ltu.domain.mp_entity.UserEntity;
import com.ltu.domain.mp_entity.UserInviteRecordEntity;
import com.ltu.mapper.UserInviteRecordMapper;
import com.ltu.model.request.base.PageReqData;
import com.ltu.model.request.user.InviteUserReq;
import com.ltu.model.response.base.CodeDataResp;
import com.ltu.service.UserInviteRecordService;
import com.ltu.service.UserService;
import com.ltu.util.BeanMapper;
import com.ltu.util.common.MpPageUtil;
import com.ltu.util.datetime.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 用户邀请新用户记录 服务实现类
 * </p>
 *
 * @author 若尘
 * @since 2022-05-19
 */
@Service
public class UserInviteRecordServiceImpl extends BaseServiceImpl<UserInviteRecordMapper, UserInviteRecordEntity> implements UserInviteRecordService {

    @Autowired
    UserService userService;

    public void createInviteRecord(InviteUserReq inviteUserReq){
        UserInviteRecordEntity userInviteRecord = new UserInviteRecordEntity();
        BeanMapper.copy(inviteUserReq,userInviteRecord);
        userInviteRecord.setCreateTime(DateUtils.currentSecs());
        this.saveOrUpdate(userInviteRecord);
    }


    /**
     * @desc 根据事物邀请类型 获取用户最先邀请的用户记录 10个即可
     **/
    public List<UserInviteRecordEntity> getInvitRecordsByUser(Integer userId,Integer thingId){
        PageReqData pageReq = new PageReqData();
        Page<UserInviteRecordEntity> page = MpPageUtil.getCommonPage(pageReq);
        pageReq.setSortOrder("desc");
        pageReq.setSortType("create_time");
        pageReq.setPageNumber(1);
        pageReq.setPageSize(10);
        QueryWrapper<UserInviteRecordEntity> condition = new QueryWrapper<>();
        condition.eq("user_id",userId);
        condition.eq("invite_thing_id",thingId);
        Page<UserInviteRecordEntity> userInviteRecordEntityPage = super.page(page,condition);
        return userInviteRecordEntityPage.getRecords();
    }

    public UserInviteRecordEntity getInviteRecordByInvitedUserId(Integer invitedUserId){
        QueryWrapper<UserInviteRecordEntity> condition = new QueryWrapper<>();
        condition.eq("invited_user_id",invitedUserId);
        return this.getOne(condition);
    }


}
