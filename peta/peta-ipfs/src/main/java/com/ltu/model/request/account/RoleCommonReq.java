package com.ltu.model.request.account;

import com.ltu.model.request.CommonRequest;
import com.ltu.model.request.base.BaseIdReq;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper=true)
public class RoleCommonReq extends BaseIdReq {



    /*角色名*/
    @NotEmpty(message="角色名称不可以为空")
    private String roleName;

    /*备注*/
    private String remark;

    @ApiModelProperty(value = " role_code:角色编码 ")
    private String roleCode;
    @ApiModelProperty(value = " status:状态 0禁用 1启用 ")
    private Integer status;

}
