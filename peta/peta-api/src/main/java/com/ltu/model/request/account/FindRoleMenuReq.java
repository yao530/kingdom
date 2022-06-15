package com.ltu.model.request.account;

import com.ltu.model.request.CommonRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper=true)
public class FindRoleMenuReq extends CommonRequest {

    @ApiModelProperty(value = "角色id")
    @NotNull(message="角色id不可以为空")
    protected Integer roleId;

    @ApiModelProperty(value = "菜单id", notes = "多个id用英文逗号隔开，每次都传完整，即有多少个就传多少个")
    @NotEmpty(message="角色菜单不可以为空")
    private String menuIds;
}
