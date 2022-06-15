package com.ltu.model.request.account;

import com.ltu.model.request.CommonRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper=true)
public class MenuReq extends CommonRequest {

    @ApiModelProperty(value = "主键")
    private Integer id;

    @ApiModelProperty(value = "菜单地址")
    @NotEmpty(message = "菜单地址不能为空")
    private String menuUrl;

    @ApiModelProperty(value = "菜单名称")
    @NotEmpty(message = "菜单名称不能为空")
    private String menuName;

    @ApiModelProperty(value = " menu_code:菜单编码 ")
    private String menuCode;

    @ApiModelProperty(value = "菜单等级", notes = "新建必传1或2")
    private Integer level;

    @ApiModelProperty(value = "父菜单")
    private String parentId;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "是否启用")
    private Integer status;

    @ApiModelProperty(value = "菜单编码")
    private String menuIcon;

    @ApiModelProperty(value = "菜单编码")
    private Integer parentMenuId;


}
