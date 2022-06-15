package com.ltu.model.request.character;

import com.ltu.model.request.base.BaseIdReq;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ApplyCreatorReq  {

    @ApiModelProperty(value = " invite_code:邀请码 ")
    private String inviteCode;
    @ApiModelProperty(value = " apply_setting_id:活动ID ")
    private Integer applySettingId;
    @ApiModelProperty(value = " role_id:申请创作者角色ID ")
    private Integer characterRoleId;
    @ApiModelProperty(value = " apply_character_name:申请人物昵称 ")
    private String applyCharacterName;
    @ApiModelProperty(value = " personal_specialty:个人特长 ")
    private String personalSpecialty;
    @ApiModelProperty(value = " social_platform_info:社交平台信息 ")
    private String socialPlatformInfo;
    @ApiModelProperty(value = " apply_remark:申请备注 ")
    private String applyRemark;
    @ApiModelProperty(value = " apply_image:申请备注图片 ")
    private String applyImage;

    @ApiModelProperty(value = " meta_id:宇宙ID ")
    private Integer metaId;

}
