package com.ltu.model.response.dto;

   
import com.ltu.domain.mp_entity.UserInviteRecordEntity;
import com.ltu.domain.mp_entity.UserJoinDropEntity;
import com.ltu.model.request.base.BaseIdReq;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class CollectionSettingDetailDto extends BaseIdReq {

    @ApiModelProperty(value = " collection_id:故事小说id ")
    private Integer collectionId;
    @ApiModelProperty(value = " sold_type:销售状态 1空投 2抢购 3预约购买 4定向投放 ")
    private Integer soldType;
    @ApiModelProperty(value = " need_invited:是否需要邀请 0是 1否 ")
    private Integer needInvited;
    @ApiModelProperty(value = " need_invited_number:需要邀请人数 ")
    private Integer needInvitedNumber;
    @ApiModelProperty(value = " drop_type:获得类型 1、参加即可 2、按抽签 3、按邀请排名 4、按优先完成邀请时间 ")
    private Integer dropType;
    @ApiModelProperty(value = " drop_auth_avatar_ids:需持有的分身nft类型ids ")
    private String dropAuthAvatarIds;
    @ApiModelProperty(value = " limit_sold_number:限制购买数量 默认1 ")
    private Integer limitSoldNumber;
    @ApiModelProperty(value = " price:价格 ")
    private Integer price;
    @ApiModelProperty(value = " join_user_auth_type:可参加空投用户类型 1所有用户 2活动时间内新增用户 3活动时间内登录用户  ")
    private Integer joinUserAuthType=1;

    @ApiModelProperty(value = " publish_status:上架状态 0待上架 1上架中 2已下架")
    private Integer publishStatus;

    @ApiModelProperty(value = " start_time:创建时间 ")
    private Long startTime;
    @ApiModelProperty(value = " end_time:创建时间 ")
    private Long endTime;

    @ApiModelProperty(value = " book_start_time:预约/空投开始时间 ")
    private Long bookStartTime;
    @ApiModelProperty(value = " book_end_time:预约/空投结束时间 ")
    private Long bookEndTime;
    @ApiModelProperty(value = " total_join_number:参加活动人数 ")
    private Integer totalJoinNumber;

    @ApiModelProperty(value = "collection_open_type:藏品获得类型 1普通获得 2盲盒获得")
    private Integer collectionOpenType=1;

    private UserJoinDropEntity joinDropEntity;

    private List<UserInviteRecordEntity> inviteList;
}
