package com.ltu.model.response.dto;

   
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserBaseInfoItem {


    @ApiModelProperty(value="用户头像")
    private String userAvatar;

    @ApiModelProperty(value="用户名称")
    private String userNick;

    @ApiModelProperty(value = " mobile_phone:账号手机号 ")
    private String mobilePhone;

    @ApiModelProperty(value="用户编号")
    private String userCode;
    @ApiModelProperty(value = " status:状态 0禁用 1启用 ")
    private Integer status=1;
    private Integer userType;
    @ApiModelProperty(value = " identity_status:实名验证状态 0未实名 1已验证 ")
    private Integer identityStatus=0;

    @ApiModelProperty(value="登录token", notes = "暂未用到")
    private String token;

    @ApiModelProperty(value = "个人区块链地址 ")
    private String userBlockchainAddress;

}
