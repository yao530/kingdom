package com.ltu.model.request.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserUpdateReq {

    @ApiModelProperty(value="修改必传")
    private String userId;

    @ApiModelProperty(value="用户姓名，必传")
    private String nickname;

    @ApiModelProperty(value="手机号，必传")
    private String userPhone;

    @ApiModelProperty(value="密码，必传")
    private String password;

    @ApiModelProperty(value="企业名称，必传")
    private String companyName;

    @ApiModelProperty(value="用户头像，选传")
    private String userAvatar;

    @ApiModelProperty(value="简介，选传")
    private String description;

    @ApiModelProperty(value = "用户类型 1普通客户 2内部员工")
    private Integer userType;

    @ApiModelProperty(value = "性别")
    private String sex;

    @ApiModelProperty(value = "所在地区")
    private String area;

    @ApiModelProperty(value = "状态，默认1，0禁用，1启用")
    private Integer status;

    @ApiModelProperty(value = "角色id，可以多个")
    private String roleIds;

    @ApiModelProperty(value = "职位")
    private String position;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "角色名称，不用传，系统自动获取")
    private String roleNames;
}
