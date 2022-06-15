package com.ltu.model.request.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class InviteUserReq {

    @ApiModelProperty(value = " user_id:用户ID ")
    private Integer userId;
    @ApiModelProperty(value = " invited_user_id:被邀请用户ID ")
    private Integer invitedUserId;
    @ApiModelProperty(value = " invited_user_name:被邀请用户名称 ")
    private String invitedUserName;
    @ApiModelProperty(value = " invited_user_avatar:被邀请用户头像 ")
    private String invitedUserAvatar;

    @ApiModelProperty(value = "事物邀请码")
    private String inviteUniqueCode;

}
