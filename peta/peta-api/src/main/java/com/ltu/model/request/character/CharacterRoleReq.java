package com.ltu.model.request.character;

import com.ltu.model.request.base.BaseFilterPageReq;
import com.ltu.model.request.base.BaseIdReq;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CharacterRoleReq extends BaseIdReq {

    @ApiModelProperty(value = " character_role_type:人物角色类型 ")
    private Integer characterRoleType;
    @ApiModelProperty(value = " character_role_name:人物角色名称 ")
    private String characterRoleName;
    @ApiModelProperty(value = " character_number:人物数量 ")
    private Integer characterNumber;
    @ApiModelProperty(value = " status:状态 0禁用 1使用中 ")
    private Integer status;
    @ApiModelProperty(value = " remark:备注 ")
    private String remark;
    @ApiModelProperty(value = " role_id:管理后台角色ID，用于登录后台 ")
    private Integer roleId;


}
