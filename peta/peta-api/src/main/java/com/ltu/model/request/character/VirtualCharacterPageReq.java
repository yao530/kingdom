package com.ltu.model.request.character;

import com.ltu.model.request.base.BaseIdReq;
import com.ltu.model.request.base.PageReqData;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class VirtualCharacterPageReq extends PageReqData {


    @ApiModelProperty(value = " character_name:故事人物名称 ")
    private String characterName;

    @ApiModelProperty(value = " character_role_id:人物角色名称ID ")
    private Integer characterRoleId;


}
