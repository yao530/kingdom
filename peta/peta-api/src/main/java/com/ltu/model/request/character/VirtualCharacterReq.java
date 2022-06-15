package com.ltu.model.request.character;

import com.ltu.model.request.base.BaseIdReq;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class VirtualCharacterReq extends BaseIdReq {


    @ApiModelProperty(value = " character_name:故事人物名称 ")
    private String characterName;
    @ApiModelProperty(value = " birth_date:出生时间 ")
    private Long birthDate;
    @ApiModelProperty(value = " meta_id:宇宙ID ")
    private Integer metaId;
    @ApiModelProperty(value = " sex:性别 1男 0女 ")
    private Integer sex;
    @ApiModelProperty(value = " character_avatar:故事人物头像 ")
    private String characterAvatar;
    @ApiModelProperty(value = " character_cover:故事人物背景图片 ")
    private String characterCover;
    @ApiModelProperty(value = " character_role_id:人物角色名称ID ")
    private Integer characterRoleId;

    @ApiModelProperty(value = " character_styles:故事人物风格类型 ")
    private String characterStyles;
    @ApiModelProperty(value = " character_information:故事人物信息简介 ")
    private String characterInformation;

    @ApiModelProperty(value = " user_id:所属用户ID ")
    private Integer userId;

    @ApiModelProperty(value = " art_creator_id:艺创作者ID ")
    private Integer artCreatorId;

    @ApiModelProperty(value = " status:状态 0禁用 1使用中 ")
    private Integer status;
    @ApiModelProperty(value = " remark:备注 ")
    private String remark;

    @ApiModelProperty(value = " context:图文内容 ")
    private String context;

}
