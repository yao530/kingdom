package com.ltu.config.shiro.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.baomidou.mybatisplus.annotation.TableField;

@Data
public class SmsCodeLoginReq {

    @ApiModelProperty(required = true,value="登陆用户名：前台是手机号，管理页面是账号")
    @NotNull(message = "用户名不可以为空")
    private String mobilePhone;

    @ApiModelProperty(required = true, value="短信验证码")
    private String  smsCode;
    
    @ApiModelProperty(value="微信授权code，登录时必传",required = true)
    @NotNull
    @NotEmpty
    private String wxCode;

	@ApiModelProperty(value = " user_nick:wx用户昵称 ")
	private String userNick;

	@ApiModelProperty(value = " user_avatar:wxlogo ")
	private String userAvatar;

    @ApiModelProperty(value = " 用户邀请码")
    private String inviteCode;

    @ApiModelProperty(value = "事物邀请码")
    private String secretCode;
    
}
