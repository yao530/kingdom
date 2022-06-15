package com.ltu.config.shiro.dto;

import javax.validation.constraints.NotNull;
import com.ltu.model.request.CommonRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户对象
 */
@Data
@EqualsAndHashCode(callSuper = true)

public class LoginReq extends CommonRequest {
	@ApiModelProperty(required = true,value="登陆用户名：前台是手机号，管理页面是账号")
	@NotNull(message = "用户名不可以为空")
	private String username;

	@ApiModelProperty(required = true,value="登陆密码")
	@NotNull(message = "密码不可以为空")
	private String password;
	
//	@ApiModelProperty(required = true,value="登录类型1=用户登录,2=管理登录,默认等于2")
//	@NotNull(message = "密码不可以为空")
//	private int type=2;

	@ApiModelProperty(value="图形验证码，必传", notes = "图形验证码不能为空，短信验证码登陆时必传")
	private String kaptcha;

	@ApiModelProperty(value="图形验证码对应的UUID", notes = "以UUID形式获取图片验证码时必传")
	private String kaptchaUUID;
	
    @ApiModelProperty(value="微信授权登录code", notes = "微信公众号授权登录必传")
    private String code;

}
