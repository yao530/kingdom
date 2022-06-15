package com.ltu.model.request.account;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class EditRoleMenuReq {


    @ApiModelProperty(value = "主键id")
    private Integer id;

    @ApiModelProperty(value = "菜单id")
    private Integer menuId;


    @ApiModelProperty(value = "菜单状态")
    private Integer authStatus;



}
