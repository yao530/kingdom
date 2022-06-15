package com.ltu.service;

import com.ltu.domain.mp_entity.UserInviteRecordEntity;
import com.ltu.model.request.user.InviteUserReq;
import com.ltu.model.response.base.CodeDataResp;

import java.util.List;


/**
 * <p>
 * 用户邀请新用户记录 服务类
 * </p>
 *
 * @author 若尘
 * @since 2022-05-19
 */

public interface UserInviteRecordService extends BaseTService<UserInviteRecordEntity> {

    void createInviteRecord(InviteUserReq inviteUserReq);

    List<UserInviteRecordEntity> getInvitRecordsByUser(Integer userId, Integer thingId);

    UserInviteRecordEntity getInviteRecordByInvitedUserId(Integer invitedUserId);
}
