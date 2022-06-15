package com.ltu.model.request.character;

import com.ltu.model.request.base.BaseIdReq;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class VirtualCharacterModel {


    @ApiModelProperty(value = " character_name:故事人物名称 ")
    private String characterName;
    @ApiModelProperty(value = " birth_date:出生时间 ")
    private Long birthDate;
    @ApiModelProperty(value = " birth_location:出生地 ")
    private String birthLocation;
    @ApiModelProperty(value = " sex:性别 1男 0女 ")
    private Integer sex;
    @ApiModelProperty(value = " character_avatar:故事人物头像 ")
    private String characterAvatar;
    @ApiModelProperty(value = " character_styles:故事人物风格类型 ")
    private String characterStyles;
    @ApiModelProperty(value = " character_information:故事人物信息简介 ")
    private String characterInformation;


    //其他参数

}
