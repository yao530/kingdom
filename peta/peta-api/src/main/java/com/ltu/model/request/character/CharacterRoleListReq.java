package com.ltu.model.request.character;

import com.ltu.model.request.base.BaseIdReq;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CharacterRoleListReq  {

    @ApiModelProperty(value = " character_role_type:人物角色类型 ")
    private Integer characterRoleId;


}
