package com.ltu.model.response.dto;

   
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserBaseInfoItem {


    @ApiModelProperty(value="用户头像")
    private String userAvatar;

    @ApiModelProperty(value="用户名称")
    private String userNick;

    @ApiModelProperty(value="用户编号")
    private String userCode;


    @ApiModelProperty(value="登录token", notes = "暂未用到")
    private String token;

    @ApiModelProperty(value = "个人区块链地址 ")
    private String blockchainAddress;

}
